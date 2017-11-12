// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.legend

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.ui.util.ColorMap
import javax.swing._
import java.awt._
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener


/**
  * Use the controls within this edit bar to edit the color legend.
  * Does not show is isEditable is false.
  * Not static so we can call methods in the owning legend class.
  * @author Barry Becker
  */
object LegendEditBar {
  private[legend] val MARGIN = 5
  private val EDIT_BAR_BG = new Color(255, 255, 255, 180)
  private val MARKER_SIZE = 6
  private val MARKER_HALF_SIZE = 3
  private val MARKER_STROKE = new BasicStroke(0.5f)
}

class LegendEditBar private[legend](var cmap: ColorMap, var owner: Component)
  extends JPanel with MouseListener with MouseMotionListener {

  private var ratio = .0
  private var dragIndex = -1
  private var dragPosition = 0
  addMouseListener(this)
  addMouseMotionListener(this)

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponents(g)
    val g2 = g.asInstanceOf[Graphics2D]
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setColor(LegendEditBar.EDIT_BAR_BG)
    g2.fillRect(LegendEditBar.MARGIN, 0, getWidth - 2 * LegendEditBar.MARGIN, LegendEditBar.MARKER_SIZE + 4)
    ratio = (getWidth - 2 * LegendEditBar.MARGIN).toDouble / cmap.getValueRange
    g2.setStroke(LegendEditBar.MARKER_STROKE)
    for (i <- 0 until cmap.getNumValues) {
      if (dragIndex != i) {
        val x = getPositionForValue(cmap.getValue(i))
        drawMarker(cmap.getColor(i), x, g2)
      }
    }
    if (dragIndex > 0) drawMarker(cmap.getColor(dragIndex), dragPosition, g2)
  }

  /** Draw a little triangular marker for the draggable control point. */
  private def drawMarker(c: Color, xpos: Int, g2: Graphics2D) = {
    g2.setColor(c)
    val xpoints = Array(xpos - LegendEditBar.MARKER_HALF_SIZE, xpos + LegendEditBar.MARKER_HALF_SIZE, xpos)
    val ypoints = Array(1, 1, LegendEditBar.MARKER_SIZE + 2)
    val triangle = new Polygon(xpoints, ypoints, 3)
    g2.fillPolygon(triangle)
    g2.setColor(Color.BLACK)
    //g2.drawPolygon(triangle);
    g2.drawLine(xpoints(1), ypoints(1), xpoints(2), ypoints(2))
  }

  private def getValueForPosition(x: Int) = (x.toDouble - LegendEditBar.MARGIN) / ratio + cmap.getMinValue

  private def getPositionForValue(v: Double) = (LegendEditBar.MARGIN + ratio * (v - cmap.getMinValue)).toInt

  /** @return -1 if no control index under the given x pos  */
  private def getControlIndex(xpos: Int) = {
    val v = getValueForPosition(xpos)
    val i = cmap.getClosestIndexForValue(v)
    val diff = Math.abs(xpos - getPositionForValue(cmap.getValue(i)))
    if (diff <= LegendEditBar.MARKER_HALF_SIZE + 1) i
    else -1
  }

  /** @return the index at or to the left of xpos */
  private def getLeftControlIndex(xpos: Int) = {
    val v = getValueForPosition(xpos)
    cmap.getLeftIndexForValue(v)
  }

  override def mouseClicked(e: MouseEvent): Unit = {
    val xpos = e.getX
    val index = getControlIndex(xpos)
    if (e.getButton == MouseEvent.BUTTON3) { // delete on right click
      if (index != -1) cmap.removeControlPoint(index)
    }
    else if (e.getClickCount > 1) {
      val oldColor = cmap.getColorForValue(getValueForPosition(xpos))
      val newControlColor = JColorChooser.showDialog(this, AppContext.getLabel("NEW_POINT_PATH"), oldColor)
      if (newControlColor != null) {
        if (index == -1) { // add a new control point and marker here if no point is double clicked on.
          cmap.insertControlPoint(getLeftControlIndex(xpos) + 1, getValueForPosition(xpos), newControlColor)
        }
        else { // get a new color for this control point  double clicked on
          cmap.setColor(index, newControlColor)
        }
        owner.repaint()
      }
    }
  }

  override def mousePressed(e: MouseEvent): Unit = {
    val index = getControlIndex(e.getX)
    if (index > 0 && index < (cmap.getNumValues - 1)) { // we are dragging the control point.
      // Note: can't drag the first and last control points.
      dragIndex = index
      dragPosition = e.getX
    }
  }

  override def mouseReleased(e: MouseEvent): Unit = { // dropped
    updateDrag(e.getX)
    dragIndex = -1
    owner.repaint()
  }

  override def mouseEntered(e: MouseEvent): Unit = {}
  override def mouseExited(e: MouseEvent): Unit = {}

  private def updateDrag(xpos: Int) = {
    if (dragIndex > 0) {
      val v = getValueForPosition(xpos)
      if (v < cmap.getValue(dragIndex + 1) && v > cmap.getValue(dragIndex - 1)) {
        cmap.setValue(dragIndex, v)
        dragPosition = xpos
        //repaint();
        paint(getGraphics)
      }
    }
  }

  override def mouseDragged(e: MouseEvent): Unit = { updateDrag(e.getX) }
  override def mouseMoved(e: MouseEvent): Unit = {}
}
