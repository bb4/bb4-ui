// Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.sliders.rangeslider

import javax.swing.{JSlider, SwingConstants}


/**
  * An extension of JSlider to select a range of values using two thumb controls.
  * The thumb controls are used to select the lower and upper value of a range
  * with predetermined minimum and maximum values.
  *
  * <p>Note that RangeSlider makes use of the default BoundedRangeModel, which
  * supports an inner range defined by a value and an extent.  The upper value
  * returned by RangeSlider is simply the lower value plus the extent.</p>
  *
  * Derived from https://github.com/ernieyu/Swing-range-slider/blob/master/src/slider/RangeSlider.java
  */
class RangeSlider(min: Int = 0, max: Int = 100) extends JSlider(min, max) {

  setOrientation(SwingConstants.HORIZONTAL)

  /** Overrides the superclass method to install the UI delegate to draw two thumbs.
    */
  override def updateUI(): Unit = {
    setUI(RangeSliderUI(this))
    updateLabelUIs()
  }

  /** @return lower value in the range.
    */
  override def getValue: Int = super.getValue

  override def setValue(lowerValue: Int): Unit = {
    val oldValue = getValue
    if (oldValue == lowerValue) return
    // Compute new value and extent to maintain upper value.
    val oldExtent = getExtent
    val newValue = Math.min(Math.max(getMinimum, lowerValue), oldValue + oldExtent)
    val newExtent = oldExtent + oldValue - newValue
    // Set new value and extent, and fire a single change event.
    getModel.setRangeProperties(newValue, newExtent, getMinimum, getMaximum, getValueIsAdjusting)
  }

  def getUpperValue: Int = getValue + getExtent

  def setUpperValue(upperValue: Int): Unit = {
    val lowerValue = getValue
    val newExtent = Math.min(Math.max(0, upperValue - lowerValue), getMaximum - lowerValue)
    // Set extent to set upper value.
    setExtent(newExtent)
  }
}