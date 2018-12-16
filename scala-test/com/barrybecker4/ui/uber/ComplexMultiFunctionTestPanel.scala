// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import java.awt.{Color, Dimension, Graphics}

import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.function.{Function, HeightFunction}
import com.barrybecker4.ui.renderers.MultipleFunctionRenderer
import javax.swing._

import scala.util.Random


/**
  * Tests the use of the multi-function renderer.
  * @author Barry Becker
  */
class ComplexMultiFunctionTestPanel() extends JPanel {

  val rnd = new Random(1)
  val numXValues = 100
  val numFunctions = 70
  var functions: Seq[Function] = for (i <- 0 until numFunctions) yield createRandomFunction(numXValues)
  val colors: Seq[Color] = for (i <- 0 until numFunctions) yield new Color(i * 2, 10, 240 - i * 2)
  private val histogram: MultipleFunctionRenderer = new MultipleFunctionRenderer(functions, Some(colors))
  histogram.setRightNormalizePercent(90)
  this.setPreferredSize(new Dimension(800, 600))


  private def createRandomFunction(num: Int) = {
    val data = new Array[Double](num)
    var total: Double = 0
    val variance = rnd.nextDouble()
    for (i <- 0 until num) {
      total += rnd.nextDouble() + 0.1 * i * rnd.nextDouble()
      data(i) = total + variance * i * (rnd.nextDouble() - 0.4)
    }

    val negative = 10
    new HeightFunction(data, Range(-negative, num - negative))
  }

  override def paint(g: Graphics): Unit = {
    histogram.setSize(getWidth, getHeight)
    histogram.paint(g)
  }
}
