/*
 * Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */

package com.barrybecker4.ui.renderers.model

import com.barrybecker4.common.math.function.InvertibleFunction

class HistogramModel(val data: Array[Int], val xFunction: InvertibleFunction) {

  assert(!data.exists(_ < 0), "Histograms can only have non-negative values")
  var sum = data.sum
  var mean: Double = calcMean()

  def numBars: Int = this.data.length
  def getValueForPosition(pos: Int): Double = xFunction.getInverseValue(pos)
  def getPositionForValue(value: Double): Int = xFunction.getValue(value).toInt
  def getMaxHeight = Math.max(1, data.max)

  def increment(xValue: Double): Unit = {
    val xPos = xFunction.getValue(xValue).toInt
    // it has to be in range to be shown in the histogram.
    if (xPos >= 0 && xPos < data.length) data(xPos) += 1
    mean = (mean * sum + xValue) / (sum + 1)
    sum += 1
  }

  def calcMean(): Double = {
    var sumXValues: Double = 0
    for (i <- data.indices) {
      val binAvgX = xFunction.getInverseValue(i) //(xFunction.getInverseValue(i) + xFunction.getInverseValue(i + 1)) / 2.0
      sumXValues += data(i) * binAvgX
    }
    sumXValues / sum
  }

  def calcMedianPos: Int = {
    val halfTotal: Double = sum / 2.0
    var medianPos = 0
    var cumulativeTotal = 0
    while (cumulativeTotal < halfTotal && medianPos < data.length) {
      cumulativeTotal += data(medianPos)
      medianPos += 1
    }
    if (medianPos == data.length && cumulativeTotal < halfTotal)
      println(s"Warning: medianPosition: $medianPos too big and will not be shown. " +
        s"cumTotal = $cumulativeTotal halfTotal = $halfTotal")
    if (medianPos > 0 && data(medianPos - 1) > 0)
      medianPos -= ((cumulativeTotal - halfTotal) / data(medianPos - 1)).toInt
    medianPos - 1
  }
}