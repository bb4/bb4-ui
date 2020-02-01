// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.renderers.model

import com.barrybecker4.math.function.InvertibleFunction


/**
  * Manages the statistics for a histogram.
  * The histogram this represent can be incrementally modified.
  * @param data the initial histogram data
  * @param xFunction manner in which the x domain is mapped. For example it can be linear or log scale.
  * @param integralXValues if true, then the x values will assumed to be only equal to the low bin cutPoint value.
  *                       This implies that the the median only be one of those low bin cutPoint values.
  */
class HistogramModel(val data: Array[Int],
                     val xFunction: InvertibleFunction,
                     val integralXValues: Boolean = false) {

  assert(!data.exists(_ < 0), "Histograms can only have non-negative values")
  var sum: Double = data.sum
  var mean: Double = calcMean()

  def numBars: Int = this.data.length
  def getValueForPosition(pos: Int): Double = xFunction.getInverseValue(pos)
  def getPositionForValue(value: Double): Int = xFunction.getValue(value).toInt

  def getMaxHeight: Int = Math.max(1, data.max)

  // Used only by unit test
  def calcMedian(medianPos: Int): Double = {
    val m = getValueForPosition(medianPos)
    if (integralXValues) m
    else {
      val rightVal = if (medianPos >= numBars) xFunction.getDomain.max else getValueForPosition(medianPos + 1)
      (m + rightVal) / 2.0
    }
  }

  /**
    * @param xValue add some data to the histogram. This will change bar height representing count for specified value.
    */
  def increment(xValue: Double): Unit = {
    val xPos = xFunction.getValue(xValue).toInt
    // it has to be in range to be shown in the histogram.
    if (xPos >= 0 && xPos < data.length) data(xPos) += 1
    mean = (sum * mean + xValue) / (sum + 1)
    sum += 1
  }

  def calcMean(): Double = {
    var sumXValues: Double = 0
    for (i <- data.indices) {
      // the bin holds all values between xInverse(i) and xInverse(i + 1)
      val lowInverse = xFunction.getInverseValue(i)
      val binAvgX =
        if (integralXValues) lowInverse
        else (lowInverse + xFunction.getInverseValue(i + 1)) / 2.0
      sumXValues += data(i) * binAvgX
    }
    if (sum == 0) 0 else sumXValues / sum
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
