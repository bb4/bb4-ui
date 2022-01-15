/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.common.format.IntegerFormatter
import com.barrybecker4.math.Range
import com.barrybecker4.math.function.LinearFunction
import com.barrybecker4.ui.renderers.HistogramRenderer
import com.barrybecker4.ui.uber.tabs.IntegralHistogramTestPanel.NUM_X_POINTS

import java.awt.{Dimension, Graphics}
import javax.swing.*
import scala.util.Random


object IntegralHistogramTestPanel {
  private val NUM_X_POINTS = 10
}

/**
  * Tests the use of the histogram component.
  * @author Barry Becker
  */
class IntegralHistogramTestPanel() extends JPanel {
  private val data = createData
  protected var histogram =
    new HistogramRenderer(data, new LinearFunction(Range(0, 10), data.length), true)

  for (i <- 0 to 7)
    histogram.increment(15)
  for (i <- 0 to 9)
  histogram.increment(16)
  histogram.increment(17)

  //histogram.setMaxLabelWidth(30)
  histogram.setXFormatter(new IntegerFormatter)

  this.setPreferredSize(new Dimension(800, 600))


  private def createData = {
    val rnd = new Random()
    val data = new Array[Int](NUM_X_POINTS)
    for (i <- data.indices) {
      data(i) = (Math.abs(rnd.nextGaussian()) * 20).toInt
    }
    data
  }

  override def paint(g: Graphics): Unit = {
    histogram.setSize(getWidth, getHeight)
    histogram.paint(g)
  }
}
