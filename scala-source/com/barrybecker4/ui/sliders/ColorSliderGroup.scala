/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.sliders

import com.barrybecker4.common.app.AppContext
import javax.swing._
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import java.awt._


object ColorSliderGroup {
  private val RED = "RED"
  private val GREEN = "GREEN"
  private val BLUE = "BLUE"
}

/**
  * A color swatch and r,g,b sliders to control its color.
  * @author Barry Becker
  */
class ColorSliderGroup() extends JPanel with ChangeListener {
  val bl = new BoxLayout(this, BoxLayout.Y_AXIS)
  setLayout(bl)
  setBorder(BorderFactory.createEtchedBorder)
  private val red = new JLabel(getColorLabel(ColorSliderGroup.RED) + '0', SwingConstants.LEFT)
  private val green = new JLabel(getColorLabel(ColorSliderGroup.GREEN) + '0', SwingConstants.LEFT)
  private val blue = new JLabel(getColorLabel(ColorSliderGroup.BLUE) + '0', SwingConstants.LEFT)
  val redPanel: JPanel = createColorLabelPanel(red)
  val greenPanel: JPanel = createColorLabelPanel(green)
  val bluePanel: JPanel = createColorLabelPanel(blue)

  private var colorListener: ColorChangeListener = _
  private var redSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0)
  redSlider.addChangeListener(this)
  private var greenSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0)
  greenSlider.addChangeListener(this)
  private var blueSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0)
  blueSlider.addChangeListener(this)

  private var swatch = new JPanel
  swatch.setBorder(BorderFactory.createMatteBorder(2, 20, 2, 20, this.getBackground))
  add(swatch)
  add(redPanel)
  add(redSlider)
  add(greenPanel)
  add(greenSlider)
  add(bluePanel)
  add(blueSlider)
  updateSwatch()

  private def getColorLabel(key: String) = AppContext.getLabel(key) + " : "

  private def createColorLabelPanel(label: JLabel) = {
    val p = new JPanel(new BorderLayout)
    p.add(label, BorderLayout.WEST)
    p.add(new JPanel, BorderLayout.CENTER)
    p
  }

  def setColorChangeListener(listener: ColorChangeListener): Unit = {
    colorListener = listener
    updateSwatch()
  }

  def updateSwatch(): Unit = {
    val color = new Color(redSlider.getValue, greenSlider.getValue, blueSlider.getValue)
    if (colorListener != null) colorListener.colorChanged(color)
    swatch.setBackground(color)
    swatch.setOpaque(true)
    swatch.repaint()
  }

  /**
    * One of the sliders has moved.
    * @param e change event
    */
  override def stateChanged(e: ChangeEvent): Unit = {
    val src = e.getSource.asInstanceOf[JSlider]
    if (src eq redSlider) red.setText(getColorLabel(ColorSliderGroup.RED) + redSlider.getValue)
    else if (src eq greenSlider) green.setText(getColorLabel(ColorSliderGroup.GREEN) + greenSlider.getValue)
    else if (src eq blueSlider) blue.setText(getColorLabel(ColorSliderGroup.BLUE) + blueSlider.getValue)
    updateSwatch()
  }
}
