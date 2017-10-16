// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import javax.swing.plaf.ButtonUI
import javax.swing.plaf.basic.BasicButtonUI
import java.awt._
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D


/**
  * GradientButton with a gradient background
  * Standard GradientButton that shows a vertical gradient on it. Default colors from the UIManager
  * @author Barry Becker
  */
@SerialVersionUID(0L)
class GradientButton() extends JButton with MouseListener {
  /** color at the top of the button. */
  private var gradientStartColor: Color = _
  /** color at the bottom of the button. */
  private var gradientEndColor: Color = _
  private var mousedOver = false
  private val myUI = new CustomUI
  commonDefaultInit()

  /** Constructor. default to colors from the UIManager */
  def this(text: String) {
    this()
    commonDefaultInit()
    this.setText(text)
  }

  def this(text: String, icon: Icon) {
    this()
    commonDefaultInit()
    this.setText(text)
    this.setIcon(icon)
  }

  /**
    * @param startColor the color at the top of the button
    * @param endColor   the color at the bottom of the button
    */
  def this(startColor: Color, endColor: Color) {
    this()
    gradientStartColor = startColor
    gradientEndColor = endColor
    setUI(myUI)
  }

  private def commonDefaultInit() = {
    val c = UIManager.getColor("Button.background")
    gradientStartColor = c.brighter
    gradientEndColor = c
    addMouseListener(this)
    setUI(myUI)
  }

  /** Don't let anyone change the UI object. */
  override def setUI(b: ButtonUI): Unit = {super.setUI(myUI)}

  /** Set starting gradient color */
  def setStartColor(pStartColor: Color): Unit = { gradientStartColor = pStartColor}

  /*** Set ending gradient color */
  def setEndColor(pEndColor: Color): Unit = { gradientEndColor = pEndColor}

  override def mouseClicked(e: MouseEvent): Unit = {}
  override def mousePressed(e: MouseEvent): Unit = {}
  override def mouseReleased(e: MouseEvent): Unit = {}
  override def mouseEntered(e: MouseEvent): Unit = {
    mousedOver = true
    this.repaint()
  }

  override def mouseExited(e: MouseEvent): Unit = {
    mousedOver = false
    this.repaint()
  }

  /**
    * Custom Button UI class that paints a gradient background on the button
    * before text or an icon is painted on the button.
    */
  private class CustomUI extends BasicButtonUI {
    /** if the button has an icon, add the gradient background */
    override protected def paintText(g: Graphics, c: JComponent, textRect: Rectangle, text: String): Unit = {
      if (c.isInstanceOf[GradientButton] && (c.asInstanceOf[AbstractButton].getIcon == null)) addGradientBackground(g)
      super.paintText(g, c, textRect, text)
    }

    override protected def paintIcon(g: Graphics, c: JComponent, iconRect: Rectangle): Unit = {
      if (c.isInstanceOf[GradientButton] && (c.asInstanceOf[AbstractButton].getIcon != null)) addGradientBackground(g)
      super.paintIcon(g, c, iconRect)
    }

    /** Does the work of actually drawing the gradient background. */
    private def addGradientBackground(g: Graphics) = {
      val g2D = g.asInstanceOf[Graphics2D]
      val width = getSize().width
      val height = getSize().height
      val rtow = createGradient(height)
      var opacity = if (mousedOver) 1.0f
      else 0.75f
      if (!isEnabled) opacity = 0.6f
      g2D.setComposite(// SRC_OVER
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity))
      g2D.setPaint(rtow)
      g2D.fill(new Rectangle2D.Double(0, 0, width, height))
    }

    private def createGradient(height: Double) = {
      val origin = new Point2D.Double(0.0, 0.0)
      val end = new Point2D.Double(0.0, height)
      var startColor = gradientStartColor
      var endColor = gradientEndColor
      startColor = if (mousedOver) startColor.brighter
      else startColor
      endColor = if (mousedOver) endColor.brighter
      else endColor
      if (isSelected) new GradientPaint(origin, endColor, end, startColor)
      else  new GradientPaint(origin, startColor, end, endColor)
    }
  }
}