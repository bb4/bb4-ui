// Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.sliders

import com.barrybecker4.common.format.FormatUtil

import java.awt.{BorderLayout, Component, Dimension}
import javax.swing.{BorderFactory, BoxLayout, JLabel, JPanel, JSlider, SwingConstants}
import javax.swing.event.{ChangeEvent, ChangeListener}
import com.barrybecker4.ui.sliders.rangeslider.RangeSlider


class LabeledRangeSlider(labelText: String, lowerValue: Double, var upperValue: Double, min: Double, max: Double)
  extends LabeledSlider(labelText, lowerValue, min, max) {

  val highPos = getPositionFromValue(upperValue)

  private val rangeSlider = RangeSlider(0, resolution)
  rangeSlider.setName(labelText)
  rangeSlider.setPaintTicks(true)
  rangeSlider.setPaintTrack(true)
  rangeSlider.addChangeListener(this)

  override def getSlider: JSlider = rangeSlider

  def getUpperValue: Double = getValueFromPosition(rangeSlider.getUpperValue)

  def setUpperValue(v: Double): Unit = {
    rangeSlider.setUpperValue(getPositionFromValue(v))
  }

  override def setEnabled(enable: Boolean): Unit = {
    rangeSlider.setEnabled(enable)
  }


  override def toString: String = { //noinspection HardCodedStringLiteral
    "Slider " + labelText + " min=" + min + " max=" + max +
      "  lowerValue=" + getValue + " upperValue=" + getUpperValue + " ratio=" + ratio
  }
}
