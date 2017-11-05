/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import java.awt.Color


/**
  * This class maps numbers to colors.
  * The colors can include opacities and will get interpolated
  * The colormap can be dynamically changed by adding and removing control points.
  *
  * Give a list of (increasing) values and colors to map to.
  * The 2 arrays must be of the same magnitude.
  * Colors will be linearly interpolated as appropriate
  *
  * @param initialValues a monotonically increasing sequence of numbers.
  * @param initialColors a corresponding set of colors to map to.
  * @author Barry Becker
  */
class ColorMap(initialValues: Array[Double], initialColors: Array[Color]) {

  private var values = initialValues
  private var colors = initialColors
  check(values != null, "values was null")
  check(colors != null, "colors was null")
  check(values.nonEmpty, "values was empty")
  check(colors.nonEmpty, "colors was empty")
  // should also assert that the values are increasing
  check(values.length == colors.length, "there must be as many values as colors")

  private def check(thingToCheck: Boolean, failMessage: String): Unit = {
    if (!thingToCheck) throw new IllegalArgumentException(failMessage)
  }

  def getColorForValue(value: Int): Color = getColorForValue(value.toDouble)

  /**
    * @param value numeric value to get a color for from the continuous map.
    * @return color that corresponds to specified value.
    */
  def getColorForValue(value: Double): Color = {
    val len = getNumValues
    if (value <= values(0)) return colors(0)
    if (value >= values(len - 1)) return colors(len - 1)
    var i = 1
    while ( {
      i < len && value > values(i)
    }) i += 1
    if (i == len) return colors(len - 1)
    val x = i.toDouble - 1.0 + (value - values(i - 1)) / (values(i) - values(i - 1))
    interpolate(x)
  }

  /**
    * Set the opacity for all the colors in the map.
    * If they were set independently before, than that information will be lost.
    * @param alpha the new opacity value (0 is totally transparent, 255 is totally opaque).
    */
  def setGlobalAlpha(alpha: Int): Unit = {
    colors = colors.map(c => new Color(c.getRed, c.getGreen, c.getBlue, alpha))
  }

  /**
    * I don't think we should get a race condition because the static rgb variables are only used in this
    * class and this method is synchronized. I want to avoid creating the rgb arrays each time the method is called.
    * @param x value to return color for.
    * @return interpolated color
    */
  private def interpolate(x: Double) = {
    val i = x.toInt
    val delta = x - i.toDouble
    val rgba: Array[Float] = new Array[Float](4)
    val rgba1: Array[Float] = new Array[Float](4)
    colors(i).getComponents(rgba)
    colors(i + 1).getComponents(rgba1)
    new Color((rgba(0) + delta * (rgba1(0) - rgba(0))).toFloat,
              (rgba(1) + delta * (rgba1(1) - rgba(1))).toFloat,
              (rgba(2) + delta * (rgba1(2) - rgba(2))).toFloat,
              (rgba(3) + delta * (rgba1(3) - rgba(3))).toFloat)
  }

  def getMinValue: Double = values(0)
  def getMaxValue: Double = values(getNumValues - 1)
  def getValueRange: Double = getMaxValue - getMinValue
  def getValue(index: Int): Double = values(index)
  def getColor(index: Int): Color = colors(index)
  def getNumValues: Int = values.length
  def setColor(index: Int, newColor: Color): Unit = {colors(index) = newColor}

  def setValue(index: Int, value: Double): Unit = {
    if (index > 0) assert(value >= values(index - 1),
      "Can't set value=" + value + " that is less than " + values(index - 1))
    if (index < getNumValues - 1) assert(value <= values(index + 1),
      "Can't set value=" + value + " that is greater than " + values(index + 1))
    values(index) = value
  }

  def insertControlPoint(index: Int, value: Double, color: Color): Unit = {
    values = values.take(index) ++ Array(value) ++ values.drop(index)
    colors = colors.take(index) ++ Array(color) ++ colors.drop(index)
  }

  def removeControlPoint(index: Int): Unit = {
    values = values.take(index) ++ values.drop(index + 1)
    colors = colors.take(index) ++ colors.drop(index + 1)
  }

  /**
    * Given a value, return the closest control index.
    * @return closest index looking to left or right.
    */
  def getClosestIndexForValue(value: Double): Int = {
    val len = getNumValues
    if (value <= values(0)) return 0
    if (value >= values(len - 1)) return len - 1
    var i = 1

    while (i < len && value > values(i))
      i += 1
    if (i == len) return len - 1
    if (value - values(i - 1) > values(i) - value) i
    else i - 1
  }

  /**
    * Given a value, return the control index to the left of value.
    * @return closest index just to the left.
    */
  def getLeftIndexForValue(value: Double): Int = {
    val len = getNumValues
    assert(value >= values(0))
    if (value >= values(len - 2)) return len - 2
    var i = 1
    while (value > values(i)) i += 1
    i - 1
  }
}