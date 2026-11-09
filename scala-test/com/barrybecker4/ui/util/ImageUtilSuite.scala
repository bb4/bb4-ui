/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.util

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import ImageUtil.ImageType
import javax.imageio.ImageIO
import org.scalatest.funsuite.AnyFunSuite

class ImageUtilSuite extends AnyFunSuite {

  private def redSquare: BufferedImage = {
    val img = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB)
    val g = img.createGraphics()
    try {
      g.setColor(Color.RED)
      g.fillRect(0, 0, 4, 4)
    } finally g.dispose()
    img
  }

  test("writeImage JPEG produces readable image") {
    val bos = new ByteArrayOutputStream()
    ImageUtil.writeImage(redSquare, bos, ImageType.JPG)
    val decoded = ImageIO.read(new ByteArrayInputStream(bos.toByteArray))
    assert(decoded != null)
    assert(decoded.getWidth == 4)
    assert(decoded.getHeight == 4)
  }

  test("writeImage closes output stream even after successful encode") {
    var closed = false
    val out = new ByteArrayOutputStream() {
      override def close(): Unit = {
        closed = true
        super.close()
      }
    }
    ImageUtil.writeImage(redSquare, out, ImageType.PNG)
    assert(closed)
    assert(out.toByteArray.nonEmpty)
  }
}
