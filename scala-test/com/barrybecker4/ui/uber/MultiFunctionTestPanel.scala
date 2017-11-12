/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber

import java.awt.{Dimension, Graphics}

import com.barrybecker4.common.math.function.Function
import com.barrybecker4.common.math.function.HeightFunction
import com.barrybecker4.ui.renderers.MultipleFunctionRenderer
import javax.swing._



/**
  * Tests the use of the multi-function renderer.
  * @author Barry Becker
  */
class MultiFunctionTestPanel() extends JPanel {

  var functions: List[Function] = List[Function]()
  for (i <- 0 until 2) {
    functions +:= createRandomFunction
  }
  private var histogram  = new MultipleFunctionRenderer(functions)
  this.setPreferredSize(new Dimension(800, 600))

  private def createRandomFunction = {
    val num = 50
    val data = new Array[Double](num)
    var total: Double = 0
    val variance = Math.random * Math.random
    for (i <- 0 until num) {
      total += Math.random
      data(i) = total + variance * Math.random * Math.random * i - i / 2.0
    }
    new HeightFunction(data)
  }

  override def paint(g: Graphics): Unit = {
    histogram.setSize(getWidth, getHeight)
    histogram.paint(g)
  }
}
