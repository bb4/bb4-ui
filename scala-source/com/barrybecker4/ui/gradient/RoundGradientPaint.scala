// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.gradient

import java.awt._
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.awt.image.ColorModel
import java.awt.Transparency._


/**
  * Source derived from Java 2D graphics book by J. Knudsen.
  */
class RoundGradientPaint(val x: Double, val y: Double, var pointColor: Color, var radius: Point2D, var bgColor: Color)
  extends Paint {

  if (radius.distance(0, 0) <= 0) throw new IllegalArgumentException("Radius must be greater than 0.")
  protected var point = new Point2D.Double(x, y)

  override def createContext(cm: ColorModel, deviceBounds: Rectangle, userBounds: Rectangle2D,
                             xform: AffineTransform, hints: RenderingHints): PaintContext = {
    val transformedPoint = xform.transform(point, null)
    val transformedRadius = xform.deltaTransform(radius, null)
    new RoundGradientContext(transformedPoint, pointColor, transformedRadius, bgColor)
  }

  override def getTransparency: Int = {
    val a1 = pointColor.getAlpha
    val a2 = bgColor.getAlpha
    if ((a1 & a2) == 0xff) OPAQUE else TRANSLUCENT
  }
}