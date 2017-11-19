/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.uber

import com.barrybecker4.common.math.Range
import com.barrybecker4.common.math.function.LinearFunction
import com.barrybecker4.ui.renderers.HistogramRenderer
import javax.swing._
import java.awt.Dimension
import java.awt.Graphics


/**
  * Tests the use of the histogram component.
  * @author Barry Becker
  */
object HistogramTestPanel {
  private val NUM_X_POINTS = 2000
}

class HistogramTestPanel() extends JPanel {
  val data: Array[Int] = createData
  protected var histogram = new HistogramRenderer(data, new LinearFunction(new Range(0, 200), data.length))
  histogram.setMaxLabelWidth(70)
  histogram.increment(-25)
  histogram.increment(-20)
  histogram.increment(-15)
  histogram.increment(15)
  histogram.increment(20)
  histogram.increment(25)
  this.setPreferredSize(new Dimension(800, 600))


  private def createData = {
    val data = new Array[Int](HistogramTestPanel.NUM_X_POINTS)
    for (i <- 0 until HistogramTestPanel.NUM_X_POINTS) {
      data(i) = (Math.random * 100).toInt
    }
    data
  }

  override def paint(g: Graphics): Unit = {
    histogram.setSize(getWidth, getHeight)
    histogram.paint(g)
  }
}
