/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.renderers

import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.function.Function
import java.awt._
import AbstractFunctionRenderer._
import RenderingHints._


object MultipleFunctionRenderer {
  /** default color for the line series shown in the chart */
  private val DEFAULT_SERIES_COLOR = new Color(0, 10, 200, 40)
}

/**
  * This class draws a specified function.
  * @param functions the functions to plot. Functions that provide y values for every point on the x axis.
  * @author Barry Becker
  */
class MultipleFunctionRenderer(var functions: Seq[Function]) extends AbstractFunctionRenderer {
  private var lineColors: Seq[Color] = _
  private var useAntialiasing: Boolean = true
  private var seriesColor: Color = MultipleFunctionRenderer.DEFAULT_SERIES_COLOR

  /** Constructor that assumes no scaling and allows separate line colors.
    * @param functions  the functions to plot.
    * @param lineColors line colors corresponding to functions
    */
  def this(functions: Seq[Function], lineColors: Seq[Color]) {
    this(functions)
    this.lineColors = lineColors
    assert(this.functions.size == this.lineColors.size, "There must be as many line colors as functions")
  }

  /** Update the currently shown functions
    * @param functions the functions to plot.
    */
  def setFunctions(functions: Seq[Function]): Unit = {
    this.functions = functions
    lineColors = null
  }

  /** Update the currently shown functions
    * @param functions  the functions to plot.
    * @param lineColors line colors corresponding to functions
    */
  def setFunctions(functions: Seq[Function], lineColors: Seq[Color]): Unit = {
    this.functions = functions
    this.lineColors = lineColors
  }

  def setUseAntialiasing(use: Boolean): Unit = {
    useAntialiasing = use
  }

  /** draw the cartesian functions as series in the chart */
  override def paint(g: Graphics): Unit = {
    if (g == null) return
    val g2 = g.asInstanceOf[Graphics2D]
    g2.setRenderingHint(KEY_ANTIALIASING, if (useAntialiasing) VALUE_ANTIALIAS_ON else VALUE_ANTIALIAS_OFF)
    val yRange = getRange
    drawFunctions(g2, yRange)
    drawDecoration(g2, yRange)
  }

  /** @param defaultColor series color to use */
  def setSeriesColor(defaultColor: Color): Unit = {
    seriesColor = defaultColor
  }

  /** @param yRange the y axis range*/
  private def drawFunctions(g2: Graphics2D, yRange: Range): Unit = {
    val maxHeight = yRange.getExtent
    val scale = (height - 2.0 * MARGIN) / maxHeight
    val zeroHeight = -yRange.min
    clearBackground(g2)
    g2.setColor(seriesColor)
    val numPoints = getNumXPoints
    for (f <- functions.indices) {
      if (lineColors != null) g2.setColor(lineColors(f))
      var lastY = 0.0
      for (i <- 0 to numPoints) {
        val x = i.toDouble / numPoints
        val y = functions(f).getValue(x) + zeroHeight
        drawConnectedLine(g2, scale, LEFT_MARGIN + i, y, LEFT_MARGIN + i - 1, lastY)
        lastY = y
      }
    }
  }

  override protected def getRange: Range = {
    var range = new Range
    val numPoints = getNumXPoints
    for (i <- 0 until numPoints) {
      val x = i.toDouble / numPoints
      for (func <- functions) {
        range = range.add(func.getValue(x))
      }
    }
    range
  }
}