/* Copyright by Barry G. Becker, 2023. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.uber.components

import com.barrybecker4.math.Range
import com.barrybecker4.math.function.{Function, HeightFunction}
import com.barrybecker4.ui.renderers.MultipleFunctionRenderer

import java.awt.{Color, Dimension, Graphics}
import javax.swing.JPanel
import scala.util.Random

class MultiFunctionPanel extends JPanel {

  val rnd = new Random(1)
  val numXValues = 100
  val numFunctions = 70
  var functions: Seq[Function] = for (i <- 0 until numFunctions) yield createRandomFunction(numXValues)
  val colors: Seq[Color] = for (i <- 0 until numFunctions) yield new Color(i * 2, 10, 240 - i * 2)
  private var functionRenderer: MultipleFunctionRenderer = _

  init()

  def init(): Unit = {
    functionRenderer = new MultipleFunctionRenderer(functions, Some(colors))
    functionRenderer.setRightNormalizePercent(90)
    functionRenderer.setPosition(5, 10)
    this.setPreferredSize(new Dimension(800, 700))
  }

  def setNumPixelsPerXPoint(numPixels: Int): Unit = {
    functionRenderer.setNumPixelsPerXPoint(numPixels)
    this.repaint()
  }

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
    functionRenderer.setSize(getWidth, getHeight)
    functionRenderer.paint(g)
  }
}
