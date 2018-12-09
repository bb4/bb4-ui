// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.renderers.model

import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.function.LinearFunction
import org.scalatest.FunSuite


class HistogramModelSuite extends FunSuite {

  test("model construction 2 bins (1, 2) low median" ) {
    val data = Array(2, 1)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 2), data.length))
    verifyResult(model, 1.1666666666666667, 3, 0, 1.0)
  }

  test("model construction 2 bins (2, 3) low median" ) {
    val data = Array(2, 1)
    val model = new HistogramModel(data, new LinearFunction(Range(2, 3), data.length))
    verifyResult(model, 2.1666666666666665, 3, 0, 2.0)
  }

  test("model construction 2 bins (1, 3) low median" ) {
    val data = Array(2, 1)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 3), data.length))
    verifyResult(model, 1.3333333333333333, 3, 0, 1.0)
  }

  test("model construction 2 bins (1, 2) high median" ) {
    val data = Array(1, 2)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 2), data.length))
    verifyResult(model, 1.3333333333333333, 3, 1, 1.5)
  }

  test("model construction 2 bins (1, 3) high median" ) {
    val data = Array(1, 2)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 3), data.length))
    verifyResult(model, 1.6666666666666667, 3, 1, 2.0)
  }

  test("model construction 2 bins (1, 3) very high median" ) {
    val data = Array(1, 3)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 3), data.length))
    verifyResult(model, 1.75, 4, 1, 2.0)
  }

   test("model construction with 3 bins") {
    val data = Array(2, 3, 5)
    val model = new HistogramModel(data, new LinearFunction(Range(0, 12), data.length))
    verifyResult(model, 5.2, 10, 1, 4)
  }

  test("model construction 3 bins (1, 3)" ) {
    val data = Array(1, 4, 2)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 4), data.length))
    verifyResult(model, 2.142857142857143 /*15.0/7*/, 7, 1, 2.0)
  }

  test("model construction 4 bins (1, 4)" ) {
    val data = Array(2, 4, 2, 3)
    val model = new HistogramModel(data, new LinearFunction(Range(1, 5), data.length))
    verifyResult(model, 2.5454545454545454 /*28.0/11.0*/, 11, 1, 2.0)
    //  28/11 = n2.5454545454
  }

  test("model construction 4 bins (0, 100)" ) {
    val data = Array(1, 2, 2, 3)  // 0, 25, 50, 75
    val model = new HistogramModel(data, new LinearFunction(Range(0, 100), data.length))
    verifyResult(model, 46.875, 8, 2, 50.0)
  }

  private def verifyResult(model: HistogramModel,
                   expMean: Double, expSum: Double, expMedianPos: Int, expMedian: Double) = {
    assertResult(expMean) {model.mean}
    assertResult(expSum) {model.sum}
    val medianPos = model.calcMedianPos
    assertResult(expMedianPos) {medianPos}
    assertResult(expMedian) {model.getValueForPosition(medianPos)}
  }

}
