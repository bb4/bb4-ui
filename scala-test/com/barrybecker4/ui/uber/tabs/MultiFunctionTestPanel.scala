/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.math.Range
import com.barrybecker4.math.function.{Function, HeightFunction}
import com.barrybecker4.ui.renderers.MultipleFunctionRenderer

import java.awt.{Color, Dimension, Graphics}
import javax.swing.*



/**
  * Tests the use of the multi-function renderer.
  * @author Barry Becker
  */
class MultiFunctionTestPanel() extends JPanel {

  val numXValues = 50
  var functions: Seq[Function] = for (i <- 0 until 7) yield createRandomFunction(numXValues)
  val colors: Seq[Color] = for (i <- 0 until 7) yield new Color(i * 40, 20, 240 - i * 40)
  private val histogram: MultipleFunctionRenderer = new MultipleFunctionRenderer(functions, Some(colors))
  this.setPreferredSize(new Dimension(800, 600))

  private def createRandomFunction(num: Int) = {
    val data = new Array[Double](num)
    var total: Double = 0
    val variance = Math.random * Math.random
    for (i <- 0 until num) {
      total += Math.random
      data(i) = total + variance * Math.random * Math.random * i - i / 2.0
    }

    val negative = 10
    new HeightFunction(data, Range(-negative, num - negative))
  }

  override def paint(g: Graphics): Unit = {
    histogram.setSize(getWidth, getHeight)
    histogram.paint(g)
  }
}
