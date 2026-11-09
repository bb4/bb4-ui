/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.components

import java.awt.image.BufferedImage

import javax.swing.JPanel
import org.scalatest.funsuite.AnyFunSuite

class ImageListRendererSuite extends AnyFunSuite {

  private def tile(w: Int, h: Int, rgb: Int): BufferedImage = {
    val img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    var y = 0
    while (y < h) {
      var x = 0
      while (x < w) {
        img.setRGB(x, y, rgb)
        x += 1
      }
      y += 1
    }
    img
  }

  test("rapid highlight changes only enlarge the latest highlight") {
    val images = Seq(tile(10, 10, 0xff0000), tile(10, 10, 0x00ff00))
    val renderer = new ImageListRenderer(images)
  val panel = new JPanel()
    renderer.setHighlightedImage(images(0), panel)
    renderer.setHighlightedImage(images(1), panel)
    Thread.sleep(1000)
    javax.swing.SwingUtilities.invokeAndWait(() => ())
    assert(renderer.getHighlightedImage eq images(1))
    assert(renderer.enlargeHighlightedImage)
  }
}
