/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.ui.sliders.properties

/**
  * Slider properties.
  * Everything a slider needs to initialize.
  *
  * @param name name of the slider
  * @param minValue minimum possible value
  * @param maxValue maximum possible value
  * @param scale resolution. A bigger number means more increments.
  * @author Barry Becker
  */
abstract case class Properties(var name: String, var minValue: Double, var maxValue: Double, var scale: Double = 1.0) {

  def getName: String = name
  def getMinValue: Double = minValue
  def getMaxValue: Double = maxValue
  def getScale: Double = scale
}         