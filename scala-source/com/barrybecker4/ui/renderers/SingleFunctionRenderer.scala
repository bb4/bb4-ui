/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.renderers

import com.barrybecker4.math.Range
import com.barrybecker4.math.function.Function
import AbstractFunctionRenderer._
import java.awt._


object SingleFunctionRenderer {
  private val LINE_COLOR = new Color(0, 0, 0)
}

/**
  * This class draws a specified function.
  * y values for every point on the x axis.
  * @param function the function to plot.
  * @author Barry Becker
  */
class SingleFunctionRenderer(var function: Function) extends AbstractFunctionRenderer {

  /** Draw the cartesian function */
  override def paint(g: Graphics): Unit = {
    if (g == null) return
    val g2 = g.asInstanceOf[Graphics2D]
    val yRange = getRange
    val maxHeight = yRange.getExtent
    val scale = (height - 2.0 * MARGIN) / maxHeight
    clearBackground(g2)
    val numPoints = getNumXPoints
    g2.setColor(SingleFunctionRenderer.LINE_COLOR)

    for (i <- 0 until numPoints) {
      val x = i.toDouble / numPoints
      drawLine(g2, scale, (MARGIN + i).toFloat, function.getValue(x))
    }
    drawDecoration(g2, yRange)
  }

  override protected def getRange: Range = {
    var range = new Range
    val numPoints = getNumXPoints
    for (i <- 0 until numPoints) {
      val x = i.toDouble / numPoints
      range = range.add(function.getValue(x))
    }
    range
  }
}
