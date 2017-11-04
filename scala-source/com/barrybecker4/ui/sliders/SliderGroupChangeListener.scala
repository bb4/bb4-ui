/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.sliders

/**
  * @author Barry Becker
  */
trait SliderGroupChangeListener {

  /**
    * @param sliderIndex index of the slider that changed
    * @param sliderName  name of slider that was moved.
    * @param value       the new value
    */
  def sliderChanged(sliderIndex: Int, sliderName: String, value: Double): Unit
}
