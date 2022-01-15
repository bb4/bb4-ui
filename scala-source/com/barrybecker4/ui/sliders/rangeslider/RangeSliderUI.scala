// Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.sliders.rangeslider

import com.barrybecker4.ui.sliders.rangeslider.RangeSlider
import com.barrybecker4.ui.sliders.rangeslider.RangeSliderUI.{RANGE_COLOR, THUMB_COLOR, THUMB_LINE_COLOR}

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.Shape
import java.awt.event.MouseEvent
import java.awt.geom.Ellipse2D
import javax.swing.{JComponent, JSlider, SwingUtilities}
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.plaf.basic.BasicSliderUI
import javax.swing.SwingConstants.*

object RangeSliderUI {
  private val RANGE_COLOR = new Color(120, 110, 200)
  private val THUMB_LINE_COLOR = new Color(50, 10, 100)
  private val THUMB_COLOR = new Color(200, 180, 255)
}

/** UI delegate for the RangeSlider component.  RangeSliderUI paints two thumbs,
  * one for the lower value and one for the upper value.
  *
  * Derived from https://github.com/ernieyu/Swing-range-slider/blob/master/src/slider/RangeSliderUI.java
  */
class RangeSliderUI(val b: RangeSlider) extends BasicSliderUI(b) {

  private var upperThumbRect: Rectangle = _
  private var upperThumbSelected = false
  private var lowerDragging = false
  private var upperDragging = false

  /** Installs this UI delegate on the specified component.
    */
  override def installUI(c: JComponent): Unit = {
    upperThumbRect = Rectangle()
    super.installUI(c)
  }

  override protected def createTrackListener(slider: JSlider) = RangeTrackListener()
  override protected def createChangeListener(slider: JSlider) = RangeChangeHandler()

  override protected def calculateThumbSize(): Unit = {
    super.calculateThumbSize() // Call superclass method for lower thumb size.
    upperThumbRect.setSize(thumbRect.width, thumbRect.height)
  }

  /** Updates the locations for both thumbs.
    */
  override protected def calculateThumbLocation(): Unit = {
    super.calculateThumbLocation() // Call superclass method for lower thumb location.

    if (slider.getSnapToTicks) {
      val upperValue = slider.getValue + slider.getExtent
      var snappedValue = upperValue
      val majorTickSpacing = slider.getMajorTickSpacing
      val minorTickSpacing = slider.getMinorTickSpacing
      var tickSpacing = 0
      if (minorTickSpacing > 0) tickSpacing = minorTickSpacing
      else if (majorTickSpacing > 0) tickSpacing = majorTickSpacing
      if (tickSpacing != 0) { // If it's not on a tick, change the value
        if ((upperValue - slider.getMinimum) % tickSpacing != 0) {
          val temp = (upperValue - slider.getMinimum).toFloat / tickSpacing.toFloat
          val whichTick = temp.round
          snappedValue = slider.getMinimum + (whichTick * tickSpacing)
        }
        if (snappedValue != upperValue) slider.setExtent(snappedValue - slider.getValue)
      }
    }
    calculateUpperThumbLocation()
  }

  private def calculateUpperThumbLocation(): Unit = {
    if (slider.getOrientation == HORIZONTAL) {
      val upperPosition = xPositionForValue(slider.getValue + slider.getExtent)
      upperThumbRect.x = upperPosition - (upperThumbRect.width / 2)
      upperThumbRect.y = trackRect.y
    }
    else {
      val upperPosition = yPositionForValue(slider.getValue + slider.getExtent)
      upperThumbRect.x = trackRect.x
      upperThumbRect.y = upperPosition - (upperThumbRect.height / 2)
    }
  }

  override protected def getThumbSize = new Dimension(12, 12)

  /** Paints the slider.  The selected thumb is always painted on top of the other thumb
    */
  override def paint(g: Graphics, c: JComponent): Unit = {
    super.paint(g, c)
    val clipRect = g.getClipBounds
    if (upperThumbSelected) { // Paint lower thumb first, then upper thumb.
      if (clipRect.intersects(thumbRect)) paintLowerThumb(g)
      if (clipRect.intersects(upperThumbRect)) paintUpperThumb(g)
    }
    else { // Paint upper thumb first, then lower thumb.
      if (clipRect.intersects(upperThumbRect)) paintUpperThumb(g)
      if (clipRect.intersects(thumbRect)) paintLowerThumb(g)
    }
  }

  override def paintTrack(g: Graphics): Unit = {
    super.paintTrack(g)
    val trackBounds = trackRect
    if (slider.getOrientation == HORIZONTAL) { // Determine position of selected range by moving from the middle
      val lowerX = thumbRect.x + (thumbRect.width / 2)
      val upperX = upperThumbRect.x + (upperThumbRect.width / 2)
      val cy = (trackBounds.height / 2) - 2
      g.translate(trackBounds.x, trackBounds.y + cy)
      g.setColor(RANGE_COLOR)
      g.fillRect(lowerX - trackBounds.x, 0, upperX - lowerX, 4)
      g.translate(-trackBounds.x, -(trackBounds.y + cy))
    }
    else {
      val lowerY = thumbRect.y + (thumbRect.height / 2)
      val upperY = upperThumbRect.y + (upperThumbRect.height / 2)
      val cx = (trackBounds.width / 2) - 2
      g.translate(trackBounds.x + cx, trackBounds.y)
      g.setColor(RANGE_COLOR)
      g.fillRect(0, lowerY - trackBounds.y, 4, upperY - lowerY)
      g.translate(-(trackBounds.x + cx), -trackBounds.y)
    }
  }

  /** Overrides superclass method to do nothing.  Thumb painting is handled
    * within the <code>paint()</code> method.
    */
  override def paintThumb(g: Graphics): Unit = {}

  private def paintLowerThumb(g: Graphics): Unit = paintThumb(g, thumbRect)
  private def paintUpperThumb(g: Graphics): Unit = paintThumb(g, upperThumbRect)

  private def paintThumb(g: Graphics, knobBounds: Rectangle): Unit = {
    val w = knobBounds.width
    val h = knobBounds.height

    val g2d = g.create.asInstanceOf[Graphics2D]
    val thumbShape = createThumbShape(w - 1, h - 1)

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.translate(knobBounds.x, knobBounds.y)
    g2d.setColor(THUMB_COLOR)
    g2d.fill(thumbShape)
    g2d.setColor(THUMB_LINE_COLOR)
    g2d.draw(thumbShape)
    g2d.dispose()
  }

  private def createThumbShape(width: Int, height: Int) = new Ellipse2D.Double(0, 0, width, height)

  /** Called when the upper thumb is dragged to repaint the slider.
    * The <code>setThumbLocation()</code> method performs the same task for the lower thumb.
    */
  private def setUpperThumbLocation(x: Int, y: Int): Unit = {
    val upperUnionRect = new Rectangle
    upperUnionRect.setBounds(upperThumbRect)
    upperThumbRect.setLocation(x, y)
    SwingUtilities.computeUnion(upperThumbRect.x, upperThumbRect.y, upperThumbRect.width, upperThumbRect.height, upperUnionRect)
    slider.repaint(upperUnionRect.x, upperUnionRect.y, upperUnionRect.width, upperUnionRect.height)
  }

  /** Move the selected thumb in the specified direction by a block increment (Page Up or Down keys)
    */
  override def scrollByBlock(direction: Int): Unit = {
    var blockIncrement = (slider.getMaximum - slider.getMinimum) / 10
    if (blockIncrement <= 0 && slider.getMaximum > slider.getMinimum) blockIncrement = 1
    val delta = blockIncrement * (if (direction > 0) BasicSliderUI.POSITIVE_SCROLL
    else BasicSliderUI.NEGATIVE_SCROLL)
    adjustThumb(delta)
  }

  /** Move the selected thumb in the specified direction by a unit increment (using arrow keys)
    */
  override def scrollByUnit(direction: Int): Unit = {
    val delta = 1 * (if (direction > 0) BasicSliderUI.POSITIVE_SCROLL
    else BasicSliderUI.NEGATIVE_SCROLL)
    adjustThumb(delta)
  }

  private def adjustThumb(delta: Int): Unit = {
    if (upperThumbSelected) {
      val oldValue = slider.asInstanceOf[RangeSlider].getUpperValue
      slider.asInstanceOf[RangeSlider].setUpperValue(oldValue + delta)
    }
    else {
      val oldValue = slider.getValue
      slider.setValue(oldValue + delta)
    }
  }

  /** Listener to handle model change events.  This calculates the thumb locations
    * and repaints the slider if the value change is not caused by dragging a thumb.
    */
  class RangeChangeHandler extends ChangeListener {
    override def stateChanged(arg0: ChangeEvent): Unit = {
      if (!lowerDragging && !upperDragging) {
        calculateThumbLocation()
        slider.repaint()
      }
    }
  }

  /** Listener to handle mouse movements in the slider track.
    */
  class RangeTrackListener extends TrackListener {
    override def mousePressed(e: MouseEvent): Unit = {
      if (!slider.isEnabled)
        return
      currentMouseX = e.getX
      currentMouseY = e.getY
      if (slider.isRequestFocusEnabled) slider.requestFocus()
      // If the upper thumb is selected (last one dragged), then check its position first;
      // otherwise check the position of the lower thumb first.
      var lowerPressed = false
      var upperPressed = false

      if (upperThumbSelected || slider.getMinimum == slider.getValue) {
        if (upperThumbRect.contains(currentMouseX, currentMouseY))
          upperPressed = true
        else if (thumbRect.contains(currentMouseX, currentMouseY))
          lowerPressed = true
      } else {
        if (thumbRect.contains(currentMouseX, currentMouseY))
          lowerPressed = true
        else if (upperThumbRect.contains(currentMouseX, currentMouseY))
          upperPressed = true
      }

      // Handle lower thumb pressed.
      if (lowerPressed) {
        offset = getOffset(thumbRect)
        upperThumbSelected = false
        lowerDragging = true
        return
      }
      lowerDragging = false

      if (upperPressed) {
        offset = getOffset(upperThumbRect)
        upperThumbSelected = true
        upperDragging = true
        return
      }
      upperDragging = false
    }

    private def getOffset(thumbRect: Rectangle): Int = {
      slider.getOrientation match {
        case VERTICAL => currentMouseY - thumbRect.y
        case HORIZONTAL => currentMouseX - thumbRect.x
      }
    }

    override def mouseReleased(e: MouseEvent): Unit = {
      lowerDragging = false
      upperDragging = false
      slider.setValueIsAdjusting(false)
      super.mouseReleased(e)
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      if (!slider.isEnabled)
        return

      currentMouseX = e.getX
      currentMouseY = e.getY
      if (lowerDragging) {
        slider.setValueIsAdjusting(true)
        moveLowerThumb()
      }
      else if (upperDragging) {
        slider.setValueIsAdjusting(true)
        moveUpperThumb()
      }
    }

    override def shouldScroll(direction: Int) = false


    /**
      * Moves the location of the lower thumb, and sets its corresponding
      * value in the slider.
      */
    private def moveLowerThumb(): Unit = {
      var thumbMiddle = 0
      slider.getOrientation match {
        case VERTICAL =>
          val halfThumbHeight = thumbRect.height / 2
          var thumbTop = currentMouseY - offset
          var trackTop = trackRect.y
          var trackBottom = trackRect.y + (trackRect.height - 1)
          val vMax = yPositionForValue(slider.getValue + slider.getExtent)
          // Apply bounds to thumb position.
          if (drawInverted) trackBottom = vMax
          else trackTop = vMax
          thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight)
          thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight)
          setThumbLocation(thumbRect.x, thumbTop)
          thumbMiddle = thumbTop + halfThumbHeight
          slider.setValue(valueForYPosition(thumbMiddle))
        case HORIZONTAL =>
          val halfThumbWidth = thumbRect.width / 2
          var thumbLeft = currentMouseX - offset
          var trackLeft = trackRect.x
          var trackRight = trackRect.x + (trackRect.width - 1)
          val hMax = xPositionForValue(slider.getValue + slider.getExtent)
          if (drawInverted) trackLeft = hMax
          else trackRight = hMax
          thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth)
          thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth)
          setThumbLocation(thumbLeft, thumbRect.y)
          thumbMiddle = thumbLeft + halfThumbWidth
          slider.setValue(valueForXPosition(thumbMiddle))
        case _ =>
      }
    }

    /**
      * Moves the location of the upper thumb, and sets its corresponding
      * value in the slider.
      */
    private def moveUpperThumb(): Unit = {
      var thumbMiddle = 0
      slider.getOrientation match {
        case VERTICAL =>
          val halfThumbHeight = thumbRect.height / 2
          var thumbTop = currentMouseY - offset
          var trackTop = trackRect.y
          var trackBottom = trackRect.y + (trackRect.height - 1)
          val vMin = yPositionForValue(slider.getValue)
          if (drawInverted) trackTop = vMin
          else trackBottom = vMin
          thumbTop = Math.max(thumbTop, trackTop - halfThumbHeight)
          thumbTop = Math.min(thumbTop, trackBottom - halfThumbHeight)
          setUpperThumbLocation(thumbRect.x, thumbTop)
          thumbMiddle = thumbTop + halfThumbHeight
          slider.setExtent(valueForYPosition(thumbMiddle) - slider.getValue)
        case HORIZONTAL =>
          val halfThumbWidth = thumbRect.width / 2
          var thumbLeft = currentMouseX - offset
          var trackLeft = trackRect.x
          var trackRight = trackRect.x + (trackRect.width - 1)
          val hMin = xPositionForValue(slider.getValue)
          if (drawInverted) trackRight = hMin
          else trackLeft = hMin
          thumbLeft = Math.max(thumbLeft, trackLeft - halfThumbWidth)
          thumbLeft = Math.min(thumbLeft, trackRight - halfThumbWidth)
          setUpperThumbLocation(thumbLeft, thumbRect.y)
          thumbMiddle = thumbLeft + halfThumbWidth
          slider.setExtent(valueForXPosition(thumbMiddle) - slider.getValue)
        case _ =>
      }
    }
  }
}