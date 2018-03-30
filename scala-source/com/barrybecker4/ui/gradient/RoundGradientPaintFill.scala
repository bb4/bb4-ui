// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.gradient

import com.barrybecker4.ui.application.ApplicationFrame
import java.awt._
import java.awt.geom.Point2D
import java.awt.geom.RoundRectangle2D


/** Source derived from Java 2D graphics book by J. Knudsen. */
object RoundGradientPaintFill {
  def main(args: Array[String]): Unit = {
    val f = new RoundGradientPaintFill
    f.setTitle("RoundGradientPaintFill v1.0") //NON-NLS
    f.setSize(200, 200)
    f.center()
    f.setVisible(true)
  }
}

class RoundGradientPaintFill extends ApplicationFrame {
  override def paint(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]
    val r = new RoundRectangle2D.Float(25, 35, 150, 150, 25, 25)
    val rgp = new RoundGradientPaint(75, 75, Color.magenta, new Point2D.Double(0, 85), Color.blue)
    g2.setPaint(rgp)
    g2.fill(r)
  }
}