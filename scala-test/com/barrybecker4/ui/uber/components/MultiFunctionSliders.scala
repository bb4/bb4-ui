/* Copyright by Barry G. Becker, 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber.components

import com.barrybecker4.ui.sliders.{SliderGroup, SliderProperties}
import com.barrybecker4.ui.uber.components.MultiFunctionSliders.*

import java.awt.Dimension

object MultiFunctionSliders {
  private val NUM_POINTS_PER_PIXEL = "Num points per pixel"

  private val SLIDER_PROPS = Array(
    new SliderProperties(NUM_POINTS_PER_PIXEL, 1, 10, 1, 1),
  )
}

class MultiFunctionSliders extends SliderGroup(SLIDER_PROPS)  {

  this.setPreferredSize(new Dimension(600, 50))
  
  def onSliderChanged(sliderIndex: Int, sliderName: String, value: Double): Unit = {
    sliderName match {
      case NUM_POINTS_PER_PIXEL => println(s"$NUM_POINTS_PER_PIXEL: ${value.toInt}")
      case _ => throw new IllegalArgumentException("Unexpected slider: " + sliderName)
    }
  }
}
