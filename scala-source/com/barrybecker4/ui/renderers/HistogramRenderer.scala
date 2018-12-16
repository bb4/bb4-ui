/* Copyright by Barry G. Becker, 2015 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.renderers

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.format.DefaultNumberFormatter
import com.barrybecker4.common.format.FormatUtil._
import com.barrybecker4.common.format.INumberFormatter
import com.barrybecker4.common.math.function.InvertibleFunction
import com.barrybecker4.common.math.function.LinearFunction
import HistogramRenderer._
import java.awt._
import com.barrybecker4.ui.renderers.model.HistogramModel


object HistogramRenderer {
  private val BACKGROUND_COLOR = new Color(255, 255, 255)
  private val BAR_COLOR = new Color(160, 120, 255)
  private val BAR_BORDER_COLOR = new Color(0, 0, 0)
  private val FONT = new Font("Sanserif", Font.PLAIN, 12)
  private val BOLD_FONT = new Font("Sanserif", Font.BOLD, 12)
  private val DEFAULT_LABEL_WIDTH = 30
  private val MARGIN: Int = 24
  private val TICK_LENGTH = 2
  private val NARROW_X_THRESH = 390
}

/**
  * This class renders a histogram.
  * The histogram is defined as an array of integers.
  * @param data the array to hold counts for each x axis position. y values for every point on the x axis.
  * @param xFunction a way to scale the values on the x axis.
  *             This function takes an x value in the domain space and maps it to a bin index location.
  * @author Barry Becker
  */
class HistogramRenderer(val data: Array[Int], val xFunction: InvertibleFunction) {

  private val model = new HistogramModel(data, xFunction)
  private var width = 0
  private var height = 0
  private var maxNumLabels = 0
  private var barWidth: Float = 0.0f
  private var zeroXPos = 0
  private var formatter: INumberFormatter = new DefaultNumberFormatter
  private var maxLabelWidth = DEFAULT_LABEL_WIDTH

  /** Constructor that starts at x=0 and assumes no scaling ont he x axis.
    * @param data the array to hold counts for each x axis position.
    */
  def this(data: Array[Int]) {
    this(data, new LinearFunction(1.0))
  }

  def setSize(width: Int, height: Int): Unit = {
    this.width = width
    this.height = height
    maxNumLabels = this.width / maxLabelWidth
    barWidth = (this.width - 2.0F * MARGIN) / model.numBars
    zeroXPos = calcZeroPos()
  }

  def increment(xValue: Double): Unit = model.increment(xValue)

  /** Provides customer formatting for the x axis values.
    * @param formatter a way to format the x axis values
    */
  def setXFormatter(formatter: INumberFormatter): Unit = { this.formatter = formatter }

  /** The larger this is, the fewer equally spaced x labels.
    * @param maxLabelWidth max width of x labels.
    */
  def setMaxLabelWidth(maxLabelWidth: Int): Unit = { this.maxLabelWidth = maxLabelWidth }

  /** draw the histogram graph */
  def paint(g: Graphics): Unit = {
    if (g == null) return
    val g2 = g.asInstanceOf[Graphics2D]
    g2.setFont(FONT)
    val maxHeight = model.getMaxHeight
    val scale = (height - 2.0 * MARGIN) / maxHeight
    clearBackground(g2)
    var xpos: Float = MARGIN
    var xIdx = 0
    for (value <- data) {
      drawBar(g2, scale, xpos, xIdx, value)
      xIdx += 1
      xpos += barWidth
    }
    drawDecoration(g2, maxHeight)
  }

  private def drawDecoration(g2: Graphics2D, maxHeight: Int): Unit = {
    val width = (barWidth * model.numBars).toInt
    drawAxes(g2, maxHeight, width)
    drawVerticalMarkers(g2, width)
  }

  private def drawAxes(g2: Graphics2D, maxHeight: Int, width: Int): Unit = { // left y axis
    g2.drawLine(MARGIN - 1, height - MARGIN, MARGIN - 1, MARGIN)
    // x axis
    g2.drawLine(MARGIN - 1, height - MARGIN - 1, MARGIN - 1 + width, height - MARGIN - 1)
    val numTrials = formatNumber(model.sum)
    val yOffset = 8

    val trialsX = width - (if (width > NARROW_X_THRESH) 230 + 0.2 * width else 210).toInt
    val meanX = width - (if (width > NARROW_X_THRESH) 170 + 0.1 * width else 80).toInt
    g2.drawString(AppContext.getLabel("HEIGHT") + " = " + formatNumber(maxHeight), MARGIN / 3, MARGIN - yOffset)
    if (width > 0.7 * NARROW_X_THRESH) {
      g2.drawString(AppContext.getLabel("NUM_TRIALS") + " = " + numTrials, trialsX, MARGIN - yOffset)
    }
    g2.setFont(BOLD_FONT)
    if (width > 0.4 * NARROW_X_THRESH) {
      g2.drawString(AppContext.getLabel("MEAN") + " = " + formatNumber(model.mean), meanX, MARGIN - yOffset)
    }
    if (width > NARROW_X_THRESH) {
      val median = model.calcMedian(model.calcMedianPos)
      g2.drawString(AppContext.getLabel("MEDIAN") + " = " + formatNumber(median), width - 90, MARGIN - yOffset)
    }
    g2.setFont(FONT)
  }

  private def drawVerticalMarkers(g2: Graphics2D, width: Double): Unit = {
    drawMeanLine(g2, width)
    drawMedianLine(g2, width)
    drawZeroLine(g2, width)
  }

  private def drawMeanLine(g2: Graphics2D, width: Double): Unit = {
    val meanXpos = (MARGIN + width * xFunction.getValue(model.mean) / model.numBars + barWidth / 2).toInt
    g2.drawLine(meanXpos, height - MARGIN, meanXpos, MARGIN)
    g2.drawString(AppContext.getLabel("MEAN"), meanXpos + 4, MARGIN + 12)
  }

  private def drawMedianLine(g2: Graphics2D, width: Double): Unit = {
    val median = model.calcMedianPos
    val medianXpos = (MARGIN + width * median / model.numBars + barWidth / 2).toInt
    g2.drawLine(medianXpos, height - MARGIN, medianXpos, MARGIN)
    g2.drawString(AppContext.getLabel("MEDIAN"), medianXpos + 4, MARGIN + 28)
  }

  private def drawZeroLine(g2: Graphics2D, width: Double): Unit = {
    if (xFunction.getInverseValue(0) < 0) {
      g2.drawLine(zeroXPos, height - MARGIN, zeroXPos, MARGIN)
      g2.drawString("0", zeroXPos + 4, MARGIN + 38)
    }
  }

  private def calcZeroPos(): Int = {
    val zero = xFunction.getValue(0)
    (MARGIN + width * zero / model.numBars + barWidth / 2).toInt
  }

  /** Draw a single bar in the histogram */
  private def drawBar(g2: Graphics2D, scale: Double, xpos: Float, xIdx: Int, value: Int): Unit = {
    val h = scale * value
    val top = (height - h - MARGIN).toInt
    g2.setColor(BAR_COLOR)
    g2.fillRect(xpos.toInt, top, Math.max(1, barWidth).toInt, h.toInt)
    g2.setColor(BAR_BORDER_COLOR)
    if (model.numBars < maxNumLabels) { // if not too many bars add a nice border.
      g2.drawRect(xpos.toInt, top, barWidth.toInt, h.toInt)
    }
    drawLabelIfNeeded(g2, xpos, xIdx)
  }

  /** Draw the label or label and tick if needed for this bar.
    * Avoid drawing the x label if its close to 0 so it does not interfere with the 0 label.
    */
  private def drawLabelIfNeeded(g2: Graphics2D, xpos: Float, xIdx: Int): Unit = {
    val xValue = xFunction.getInverseValue(xIdx)
    val x = (xpos + barWidth / 2).toInt
    val metrics = g2.getFontMetrics
    var drawingLabel = false
    val labelSkip = (maxLabelWidth + 10) * model.numBars / width
    val closeToZero = Math.abs(xpos - zeroXPos) < maxLabelWidth / 2
    if (closeToZero) {
      g2.setFont(BOLD_FONT)
      g2.drawString(formatter.format(0), zeroXPos - 8, height - 5)
      g2.drawLine(zeroXPos, height - MARGIN + TICK_LENGTH + 4, zeroXPos, height - MARGIN - 2) // zero tick
      g2.setFont(FONT)
    }
    else if (model.numBars < maxNumLabels || xIdx % labelSkip == 0) {
      drawingLabel = true
      val label = formatter.format(xValue)
      val labelXPos = x - metrics.stringWidth(label) / 2
      g2.drawString(label, labelXPos, height - 5)
    }

    val skipD2 = Math.max(1, labelSkip / 2)
    val skipD5 = Math.max(1, labelSkip / 5)
    if (labelSkip % 2 == 0 && xIdx % skipD2 == 0)
      g2.drawLine(x, height - MARGIN + TICK_LENGTH + 1, x, height - MARGIN)
    else if (labelSkip % 5 == 0 && xIdx % skipD5 == 0)
      g2.drawLine(x, height - MARGIN + TICK_LENGTH - 2, x, height - MARGIN)
    if (drawingLabel)
      g2.drawLine(x, height - MARGIN + TICK_LENGTH + 4, x, height - MARGIN - 2)
  }

  private def clearBackground(g2: Graphics2D): Unit = {
    g2.setColor(BACKGROUND_COLOR)
    g2.fillRect(0, 0, width, height)
  }
}
