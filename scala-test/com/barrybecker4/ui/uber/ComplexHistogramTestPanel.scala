// Copyright by Barry G. Becker, 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import java.awt.{Dimension, Graphics}
import com.barrybecker4.common.format.CurrencyFormatter
import com.barrybecker4.math.Range
import com.barrybecker4.math.function.LinearFunction
import com.barrybecker4.ui.renderers.HistogramRenderer
import javax.swing._
import scala.util.Random
import com.barrybecker4.ui.uber.ComplexHistogramTestPanel.NUM_X_POINTS


object ComplexHistogramTestPanel {
  private val NUM_X_POINTS = 100
}

class ComplexHistogramTestPanel() extends JPanel {
  private val data = createData
  protected var histogram = new HistogramRenderer(data, new LinearFunction(Range(-5000000, 20000000), data.length))
  histogram.increment(-25)
  for (i <- 0 to 200)
    histogram.increment(-2000)
  for (i <- 0 to 300)
    histogram.increment(-20000)


  histogram.setMaxLabelWidth(80)
  histogram.setXFormatter(new CurrencyFormatter)

  this.setPreferredSize(new Dimension(800, 600))


  private def createData = {
    val rnd = new Random()
    val data = new Array[Int](NUM_X_POINTS)
    for (i <- data.indices) {
      data(i) = (Math.abs(rnd.nextGaussian() + rnd.nextDouble()) * 100).toInt
    }
    data
  }

  override def paint(g: Graphics): Unit = {
    histogram.setSize(getWidth, getHeight)
    histogram.paint(g)
  }
}
