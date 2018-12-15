/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.renderers

import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.function.Function
import java.awt._
import AbstractFunctionRenderer._
import RenderingHints._
import MultipleFunctionRenderer._


object MultipleFunctionRenderer {
  /** default color for the line series shown in the chart */
  private val DEFAULT_SERIES_COLOR = new Color(0, 10, 200, 40)

  /** Normalize the height based on the y extent of the right half of the chart by default */
  private val DEFAULT_RIGHT_NORMALIZE_PCT: Int = 50
}

/**
  * This class draws a specified set of functions.
  * @param functions the functions to plot. Functions that provide y values for every point on the x axis.
  * @param lineColors line colors corresponding to functions
  * @author Barry Becker
  */
class MultipleFunctionRenderer(var functions: Seq[Function],
                               var lineColors: Option[Seq[Color]] = None) extends AbstractFunctionRenderer {

  assert(lineColors.isEmpty || this.functions.size == this.lineColors.get.size,
    "There must be as many line colors as functions")
  private var rightNormalizePct: Double = DEFAULT_RIGHT_NORMALIZE_PCT
  private var useAntialiasing: Boolean = true
  private var seriesColor: Color = MultipleFunctionRenderer.DEFAULT_SERIES_COLOR

  def this(functions: Seq[Function]) { this(functions, None) }

  def setUseAntialiasing(use: Boolean): Unit = { useAntialiasing = use }

  def setRightNormalizePercent(pct: Int): Unit = {
    assert(pct > 0 && pct <= 100)
    rightNormalizePct = pct
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
      if (lineColors.isDefined) g2.setColor(lineColors.get(f))
      var lastY = 0.0
      val function = functions(f)
      val domain = function.getDomain
      val ext = domain.getExtent
      for (i <- 0 to numPoints) {
        val x = domain.min + ext * (i.toDouble / numPoints)
        val y = function.getValue(x) + zeroHeight
        drawConnectedLine(g2, scale, LEFT_MARGIN + i, y, LEFT_MARGIN + i - 1, lastY)
        lastY = y
      }
    }
  }

  /** Draw x axis labels using the domain from the function being rendered. */
  override protected def drawXAxisLabels(g2: Graphics2D): Unit = {
    if (functions.isEmpty) return
    val xRange = functions.head.getDomain
    val ext = xRange.getExtent
    val (cutPoints, cutPointLabels) = getNiceCutpointsAndLabels(xRange, width / 80)

    val chartWidth = width - xOffset - LEFT_MARGIN - MARGIN
    for (i <- cutPoints.indices) {
      val label = cutPointLabels(i)
      val xPos = (xOffset + LEFT_MARGIN - 3 + chartWidth * (cutPoints(i) - xRange.min) / ext).toFloat
      g2.drawString(label, xPos - 2, height - MARGIN + 15)
    }

    val eps = xRange.getExtent * 0.05
    // draw origin if 0 is in range
    if (0 < (xRange.max - eps) && 0 > (xRange.min + eps)) {
      val originX = (xOffset + LEFT_MARGIN + chartWidth * Math.abs(xRange.min) / ext).toInt
      g2.setColor(ORIGIN_LINE_COLOR)
      g2.drawLine(originX, height - MARGIN, originX, MARGIN)
    }
  }

  override protected def getRange: Range = {
    if (functions.isEmpty) Range(0, 1)
    else {
      var range = new Range
      val numPoints = getNumXPoints
      val domain = functions.head.getDomain
      val ext = domain.getExtent

      val start: Int = Math.max(1, numPoints * rightNormalizePct / 100.0).toInt
      for (i <- start until numPoints) {
        val x = ext * i.toDouble / numPoints + domain.min
        for (func <- functions) {
          range = range.add(func.getValue(x))
        }
      }
      range
    }
  }
}
