/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.sliders


/**
  * @author Barry Becker
  */
trait SliderChangeListener {

  /**
    * @param slider the slider that was moved.
    */
  def sliderChanged(slider: LabeledSlider): Unit
}
