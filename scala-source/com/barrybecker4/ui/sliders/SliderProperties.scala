/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.sliders

/**
  * Immutable slider properties.
  * Everything a slider needs to initialize.
  * @param name name of the slider
  * @param minValue minimum possible value
  * @param maxValue maximum possible value
  * @param initialValue stating value
  * @param scale resolution. A bigger number means more increments.
  * @author Barry Becker
  */
class SliderProperties(var name: String, var minValue: Double, var maxValue: Double,
                       var initialValue: Double, var scale: Double = 1.0) {

  def this(name: String, minValue: Int, maxValue: Int, initialValue: Int) {
    this(name, minValue, maxValue, initialValue, 1.0)
  }

  def getName: String = name
  def getMinValue: Double = minValue
  def getMaxValue: Double = maxValue
  def getInitialValue: Double = initialValue
  def getScale: Double = scale
}
