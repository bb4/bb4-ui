// Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.sliders.properties

import com.barrybecker4.ui.sliders.properties.Properties


/**
  * @param initialValue stating value
  */
class RangeSliderProperties(name: String, minValue: Double, maxValue: Double,
                       var lowValue: Double, val highValue: Double, scale: Double = 1.0) 
  extends Properties(name, minValue, maxValue, scale) {

  def this(name: String, minValue: Int, maxValue: Int, lowValue: Int, highValue: Int) = {
    this(name, minValue, maxValue, lowValue, highValue, 1.0)
  }

  def getLowerValue: Double = lowValue
  def getUpperValue: Double = highValue
}
