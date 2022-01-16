// Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.sliders

import com.barrybecker4.common.format.FormatUtil

import java.awt.{BorderLayout, Component, Dimension}
import javax.swing.{BorderFactory, BoxLayout, JLabel, JPanel, JSlider, SwingConstants}
import javax.swing.event.{ChangeEvent, ChangeListener}
import com.barrybecker4.ui.sliders.rangeslider.RangeSlider


class LabeledRangeSlider(labelText: String, initialLowerValue: Double, var initialUpperValue: Double, min: Double, max: Double)
  extends LabeledSlider(labelText, initialLowerValue, min, max) {

  private var lastLowerValue = initialLowerValue
  private var lastUpperValue = initialUpperValue

  init()

  override protected def init() = {
    super.init()

    val rangeSlider = getSlider.asInstanceOf[RangeSlider]
    updateSliderValues()
    rangeSlider.addChangeListener(this)
  }

  private def getRangeSlider: RangeSlider = getSlider.asInstanceOf[RangeSlider]

  override protected def createSlider(): JSlider = RangeSlider(0, resolution)

  def getUpperValue: Double = getValueFromPosition(getRangeSlider.getUpperValue)

  def setUpperValue(v: Double): Unit =
    getRangeSlider.setUpperValue(getPositionFromValue(v))

  override def setEnabled(enable: Boolean): Unit =
    getRangeSlider.setEnabled(enable)

  override protected def updateSliderValues(): Unit = {
    getRangeSlider.setValue(getPositionFromValue(initialLowerValue))
    getRangeSlider.setUpperValue(getPositionFromValue(initialUpperValue))
  }

  override protected def getLabelText = {
    val value =
      if (showAsInteger) Integer.toString(getValue.toInt)
      else FormatUtil.formatNumber(getValue)
    val upperValue =
      if (showAsInteger) Integer.toString(getUpperValue.toInt)
      else FormatUtil.formatNumber(getUpperValue)

    labelText + ": [" + value + ", " + upperValue + "]"
  }

  override def stateChanged(e: ChangeEvent): Unit = {
    val lowerValue = getValue
    val upperValue = getUpperValue
    if (lowerValue != lastLowerValue || upperValue != lastUpperValue) {
      label.setText(getLabelText)
      for (listener <- listeners) {
        listener.sliderChanged(this)
      }
      lastLowerValue = lowerValue
      lastUpperValue = upperValue
    }
  }

  override def toString: String = { //noinspection HardCodedStringLiteral
    "Slider " + labelText + " min=" + min + " max=" + max +
      "  lowerValue=" + getValue + " upperValue=" + getUpperValue + " ratio=" + ratio
  }
}
