/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.uber.components

import com.barrybecker4.ui.sliders.SliderGroup
import com.barrybecker4.ui.sliders.properties.{Properties, SliderProperties}
import com.barrybecker4.ui.uber.components.ParameterSliders.*

object ParameterSliders {
  private val MAX_ROOM_WIDTH_SLIDER = "Max Width"
  private val MAX_ROOM_HEIGHT_SLIDER = "Max Height"
  private val PERCENT_FILLED_SLIDER = "Percent Filled"
  private val CONNECTIVITY_SLIDER = "Room connectivity"
  private val MAX_ASPECT_RATIO_SLIDER = "Max aspect ratio"

  private val GENERAL_SLIDER_PROPS: Array[Properties] = Array(
    new SliderProperties(MAX_ROOM_WIDTH_SLIDER, 5, 60, 40, 1),
    new SliderProperties(PERCENT_FILLED_SLIDER, 10, 100, 100, 1),
    new SliderProperties(CONNECTIVITY_SLIDER, 0.1, 1.0, 0.5, 100),
    new SliderProperties(MAX_ASPECT_RATIO_SLIDER, 1.0, 5.0, 1.0, 1000),
  )
}

class ParameterSliders extends SliderGroup(GENERAL_SLIDER_PROPS)  {

  def onSliderChanged(sliderIndex: Int, sliderName: String, value: Double): Unit = {
    sliderName match {
      case MAX_ROOM_WIDTH_SLIDER => println(s"$MAX_ROOM_WIDTH_SLIDER: ${value.toInt}")
      case MAX_ROOM_HEIGHT_SLIDER => println(s"$MAX_ROOM_HEIGHT_SLIDER: ${value.toInt}")
      case PERCENT_FILLED_SLIDER => println(s"$PERCENT_FILLED_SLIDER: ${value.toInt}")
      case CONNECTIVITY_SLIDER => println(s"$CONNECTIVITY_SLIDER: ${value.toFloat}")
      case MAX_ASPECT_RATIO_SLIDER => println(s"$MAX_ASPECT_RATIO_SLIDER: ${value.toFloat}")
      case _ => throw new IllegalArgumentException("Unexpected slider: " + sliderName)
    }
  }
}
