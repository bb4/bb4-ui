/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT*/
package com.barrybecker4.ui.renderers

import com.barrybecker4.common.format.DefaultNumberFormatter
import com.barrybecker4.common.format.INumberFormatter
import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.cutpoints.CutPointGenerator
import java.awt._
import AbstractFunctionRenderer._


/**
  * This class draws a specified function.
  * @author Barry Becker
  */
object AbstractFunctionRenderer {
  private val BACKGROUND_COLOR = new Color(255, 255, 255)
  private val LABEL_COLOR = Color.BLACK
  private val ORIGIN_LINE_COLOR = new Color(20, 0, 0, 120)
  private[renderers] val LEFT_MARGIN = 75
  private[renderers] val MARGIN = 40
  private val NUM_Y_LABELS = 10
}

abstract class AbstractFunctionRenderer {
  private var width = 0
  protected var height = 0
  private var xOffset = 0
  private var yOffset = 0
  private var formatter: INumberFormatter = new DefaultNumberFormatter

  def setSize(width: Int, height: Int): Unit = {
    this.width = width
    this.height = height
  }

  def setPosition(xOffset: Int, yOffset: Int): Unit = {
    this.xOffset = xOffset
    this.yOffset = yOffset
  }

  /**
    * Provides customer formatting for the x axis values.
    * @param formatter a way to format the x axis values
    */
  def setXFormatter(formatter: INumberFormatter): Unit = {
    this.formatter = formatter
  }

  /** draw the cartesian function */
  def paint(g: Graphics): Unit
  protected def getRange: Range
  private[renderers] def getNumXPoints = width - MARGIN

  private[renderers] def drawDecoration(g2: Graphics2D, yRange: Range): Unit = {
    g2.setColor(LABEL_COLOR)
    g2.drawRect(xOffset, yOffset, width, height)
    drawAxes(g2)
    drawAxisLabels(g2, yRange)
  }

  private def drawAxes(g2: Graphics2D): Unit = { // left y axis
    g2.drawLine(xOffset + LEFT_MARGIN - 1, yOffset + height - MARGIN,
                xOffset + LEFT_MARGIN - 1, yOffset + MARGIN)
    // x axis
    g2.drawLine(xOffset + LEFT_MARGIN - 1, yOffset + height - MARGIN - 1,
                xOffset + LEFT_MARGIN - 1 + width, yOffset + height - MARGIN - 1)
  }

  /** Draw y axis labels. The x-axis doesn't really need labels because it is always [0 - 1]. */
  private def drawAxisLabels(g2: Graphics2D, yRange: Range): Unit = {
    val metrics = g2.getFontMetrics
    // draw nice number labels.
    val cutPointGenerator = new CutPointGenerator
    cutPointGenerator.setUseTightLabeling(true)
    //System.out.println("range = " + yRange + " yOffset=" + yOffset);
    val ext = yRange.getExtent
    if (ext.isNaN) return
    val cutpoints = cutPointGenerator.getCutPoints(yRange, NUM_Y_LABELS)
    val cutpointLabels = cutPointGenerator.getCutPointLabels(yRange, NUM_Y_LABELS)
    val chartHt = height - yOffset - MARGIN - MARGIN
    for (i <- cutpoints.indices) {
      //System.out.println("cp = " + cutpoints[i] +"  label = " + cutpointLabels[i]);
      val label = cutpointLabels(i)
      //FormatUtil.formatNumber(cutpoints[i]);
      val labelWidth = metrics.stringWidth(label)
      val yPos = (yOffset + MARGIN + Math.abs(yRange.max - cutpoints(i)) / ext * chartHt).toFloat
      g2.drawString(label, xOffset + LEFT_MARGIN - labelWidth - 3, yPos + 5)
    }

    val eps = yRange.getExtent * 0.05
    // draw origin if 0 is in range
    if (0 < (yRange.max - eps) && 0 > (yRange.min + eps)) {
      val originY = (yOffset + MARGIN + Math.abs(yRange.max) / ext * chartHt).toFloat
      //g2.drawString("0", xOffset + LEFT_MARGIN - 15, originY + 5);
      g2.setColor(ORIGIN_LINE_COLOR)
      g2.drawLine(xOffset + LEFT_MARGIN - 1, originY.toInt,
                  xOffset + LEFT_MARGIN - 1 + width, originY.toInt)
    }
  }

  /** draw line composed of points */
  private[renderers] def drawLine(g2: Graphics2D, scale: Double, xpos: Float, ypos: Double): Unit = {
    val h = scale * ypos
    val top = (height - h - MARGIN).toInt
    g2.fillOval(xOffset + xpos.toInt, yOffset + top, 3, 3)
  }

  /** draw line composed of connected line segments */
  private[renderers] def drawConnectedLine(g2: Graphics2D, scale: Double, xpos: Float, ypos: Double,
                                           lastX: Double, lastY: Double): Unit = {
    val h = scale * ypos
    val top = (height - h - MARGIN).toInt
    val lastHt = scale * lastY
    val lastTop = (height - lastHt - MARGIN).toInt
    g2.drawLine(xOffset + xpos.toInt, yOffset + top, xOffset + lastX.toInt, yOffset + lastTop)
  }

  private[renderers] def clearBackground(g2: Graphics2D): Unit = {
    g2.setColor(BACKGROUND_COLOR)
    g2.fillRect(xOffset, yOffset, width, height)
  }
}