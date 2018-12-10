// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.renderers.model

import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.function.{LinearFunction, LogFunction}
import org.scalatest.FunSuite


class HistogramModelSuite extends FunSuite {

  test("model construction 2 bins (1, 2) low median" ) {
    val data = Array(2, 1)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 2), data.length))
    verifyResult(model, 1.4166666666666667,3.0,0,1.25)
  }

  test("model construction 2 bins (2, 3) low median" ) {
    val data = Array(2, 1)
    val model = new HistogramModel(data, new LinearFunction(Range(2, 3), data.length))
    verifyResult(model, 2.4166666666666665,3.0,0,2.25)
  }

  test("model construction 2 bins (1, 3) low median" ) {
    val data = Array(2, 1)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 3), data.length))
    verifyResult(model, 1.8333333333333333,3.0,0,1.5)
  }

  test("model construction 2 bins (1, 2) high median" ) {
    val data = Array(1, 2)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 2), data.length))
    verifyResult(model, 1.5833333333333333,3.0,1,1.75)
  }

  test("model construction 2 bins (1, 3) high median" ) {
    val data = Array(1, 2)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 3), data.length))
    verifyResult(model, 2.1666666666666665, 3, 1, 2.5)
  }

  test("model construction 2 bins (1, 3) very high median" ) {
    val data = Array(1, 3)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 3), data.length))
    verifyResult(model, 2.25, 4, 1, 2.5)
  }

   test("model construction with 3 bins") {
    val data = Array(2, 3, 5)
    val model = new HistogramModel(data, new LinearFunction(Range(0, 12), data.length))
    verifyResult(model, 7.2,10.0,1,6.0)
  }

  test("model construction 3 bins (1, 3)" ) {
    val data = Array(1, 4, 2)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 4), data.length))
    verifyResult(model, 2.642857142857143,7.0,1,2.5)
  }

  test("model construction 4 bins (1, 4)" ) {
    val data = Array(2, 4, 2, 3)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 5), data.length))
    verifyResult(model, 3.0454545454545454,11.0,1,2.5)
  }

  test("model construction 4 bins (0, 100)" ) {
    val data = Array(1, 2, 2, 3)  // 0, 25, 50, 75
    val model = new HistogramModel(data, new LinearFunction(Range(0, 100), data.length))
    verifyResult(model,59.375,8.0,2,62.5)
  }

  test("incrementing model when 2 bins (0, 100)" ) {
    val data = Array(0, 0)
    val model = new HistogramModel(data, new LinearFunction(Range(0, 100), data.length))
    model.increment(48.0)
    model.increment(95.0)
    verifyResult(model, 71.5,2.0,0,25.0) // expMedian wrong
  }

  test("incrementing model when 3 bins (0, 4)" ) {
    val data = Array(0, 0, 0)
    val model = new HistogramModel(data, new LinearFunction(Range(0, 4), data.length))
    model.increment(0.8)
    model.increment(0.7)
    model.increment(2.9)
    verifyResult(model, 1.4666666666666668,3.0,0,0.6666666666666666)
  }

  test("incrementing model when 2 bins (0, 100) and some values already in data" ) {
    val data = Array(2, 5)
    val model = new HistogramModel(data, new LinearFunction(Range(0, 100), data.length))
    verifyResult(model, 60.714285714285715,7.0,1,75.0)
    model.increment(48.0)
    model.increment(95.0)
    verifyResult(model, 63.111111111111114,9.0,1,75.0)
  }

  test("incrementing model with point outside domain when 3 bins (0, 4)" ) {
    val data = Array(0, 0, 0)
    val model = new HistogramModel(data, new LinearFunction(Range(0, 4), data.length))
    model.increment(0.8)
    model.increment(0.7)
    model.increment(100.7)
    model.increment(101.7)
    model.increment(2.9)
    verifyResult(model, 41.36000000000001,5.0,2,3.333333333333333)
  }

  test("incrementing model when 2 bins (0, 100) and log function on x" ) {
    val data = Array(0, 0)
    val xLogScale = 200
    val model = new HistogramModel(data, new LogFunction(xLogScale, 10.0, false))
    model.increment(48.0)
    model.increment(95.0)
    verifyResult(model, 71.5,2.0,1,1.0174362232703262)
  }

  test("incrementing model when 100 bins (0, 101) and log function on x" ) {
    val data = Array.fill(101)(0)
    val xLogScale = 200
    val model = new HistogramModel(data, new LogFunction(xLogScale, 10.0, false))
    model.increment(48.0)
    model.increment(95.0)
    verifyResult(model, 71.5,2.0,100,3.1805863849298888)
  }

  private case class Result(expMean: Double, expSum: Double, expMedianPos: Int, expMedian: Double)

  private def verifyResult(model: HistogramModel,
                   expMean: Double, expSum: Double, expMedianPos: Int, expMedian: Double) = {
    val expResult = Result(expMean, expSum, expMedianPos, expMedian)
    val medianPos = model.calcMedianPos
    val actResult = Result(model.mean, model.sum, medianPos, model.calcMedian)
    assertResult(expResult) {actResult}
  }
}
