// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.legend

import com.barrybecker4.ui1.util.ColorMap
import javax.swing._
import java.awt._


/**
  * The bar that is the continuous color legend.
  * @author Barry Becker
  */
object ColoredLegendLine {
  private val MARGIN = LegendEditBar.MARGIN
  private val HEIGHT = 20
}

class ColoredLegendLine private[legend](var cmap: ColorMap) extends JPanel {

  override def paintComponent(g: Graphics): Unit = {

    super.paintComponents(g)
    val g2 = g.asInstanceOf[Graphics2D]
    g2.setColor(Color.white)
    g2.fillRect(ColoredLegendLine.MARGIN, 0, getWidth - 2 * ColoredLegendLine.MARGIN, ColoredLegendLine.HEIGHT)
    val firstVal = cmap.getValue(0)
    val ratio = (getWidth - 2 * ColoredLegendLine.MARGIN).toDouble / cmap.getValueRange

    for (i <- 1 until cmap.getNumValues) {
      val xstart = ratio * (cmap.getValue(i - 1) - firstVal)
      val xstop = ratio * (cmap.getValue(i) - firstVal)
      val paint = new GradientPaint(xstart.toFloat, 0.0f, cmap.getColor(i - 1),
                                     xstop.toFloat, 0.0f, cmap.getColor(i), true)
      g2.setPaint(paint)
      val w = xstop.toInt - xstart.toInt
      g2.fillRect(xstart.toInt + ColoredLegendLine.MARGIN, 0, w, ColoredLegendLine.HEIGHT)
    }
  }
}

