// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.gradient

import java.awt._
import java.awt.geom.Point2D
import java.awt.image.ColorModel
import java.awt.image.Raster


/** Source derived from Java 2D graphics book by J. Knudsen. */
class RoundGradientContext(var point: Point2D, var color1: Color,
                           var radius: Point2D, var color2: Color) extends PaintContext {
  override def dispose(): Unit = {}
  override def getColorModel: ColorModel = ColorModel.getRGBdefault

  override def getRaster(x: Int, y: Int, w: Int, h: Int): Raster = {
    val raster = getColorModel.createCompatibleWritableRaster(w, h)
    val data = new Array[Int](w * h << 2)
    val rad = radius.distance(0, 0)
    for (j <- 0 until h) {
      for (i <-0 until w) {
        val distance = point.distance(x + i, y + j)
        var ratio = distance / rad
        if (ratio > 1.0) ratio = 1.0
        val base = (j * w + i) << 2
        data(base) = (color1.getRed + ratio * (color2.getRed - color1.getRed)).toInt
        data(base + 1) = (color1.getGreen + ratio * (color2.getGreen - color1.getGreen)).toInt
        data(base + 2) = (color1.getBlue + ratio * (color2.getBlue - color1.getBlue)).toInt
        data(base + 3) = (color1.getAlpha + ratio * (color2.getAlpha - color1.getAlpha)).toInt
      }
    }
    raster.setPixels(0, 0, w, h, data)
    raster
  }
}