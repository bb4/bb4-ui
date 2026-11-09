/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.components

import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import org.scalatest.funsuite.AnyFunSuite

class ImageListPanelSuite extends AnyFunSuite {

  private def solidImage(rgb: Int): BufferedImage = {
    val img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB)
    var y = 0
    while (y < 20) {
      var x = 0
      while (x < 20) {
        img.setRGB(x, y, rgb)
        x += 1
      }
      y += 1
    }
    img
  }

  test("clicking a selected image deselects it") {
    val panel = new ImageListPanel()
    val images = Seq(solidImage(0xff0000), solidImage(0x00ff00))
    val buffer = new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB)
    val g = buffer.createGraphics()
    try {
      javax.swing.SwingUtilities.invokeAndWait(() => {
        panel.setSize(400, 200)
        panel.setImageList(images)
        panel.paint(g)
        panel.setSelectedImageIndices(Seq(0))
      })
    } finally g.dispose()
    assert(panel.getSelectedImageIndices == Seq(0))
    var deselected = false
    var x = 5
    while (x < 400 && !deselected) {
      var y = 5
      while (y < 200 && !deselected) {
        panel.setSelectedImageIndices(Seq(0))
        val event = new MouseEvent(panel, MouseEvent.MOUSE_RELEASED, 0, 0, x, y, 1, false)
        javax.swing.SwingUtilities.invokeAndWait(() => panel.mouseReleased(event))
        if (panel.getSelectedImageIndices.isEmpty) deselected = true
        y += 5
      }
      x += 5
    }
    assert(deselected, "expected a click to deselect the first image")
  }

  test("removeImageSelectionListener stops notifications") {
    val panel = new ImageListPanel()
    var count = 0
    val listener = new ImageSelectionListener {
      override def imageSelected(image: BufferedImage): Unit = count += 1
    }
    panel.addImageSelectionListener(listener)
    panel.removeImageSelectionListener(listener)
    val single = Seq(solidImage(0xff0000))
    val buffer = new BufferedImage(400, 200, BufferedImage.TYPE_INT_RGB)
    val g = buffer.createGraphics()
    try {
      javax.swing.SwingUtilities.invokeAndWait(() => {
        panel.setSize(400, 200)
        panel.setImageList(single)
        panel.paint(g)
      })
    } finally g.dispose()
    val event = new MouseEvent(panel, MouseEvent.MOUSE_RELEASED, 0, 0, 30, 30, 1, false)
    javax.swing.SwingUtilities.invokeAndWait(() => panel.mouseReleased(event))
    assert(count == 0)
  }

  test("getSelectedImageIndices is null before images are set") {
    val panel = new ImageListPanel()
    assert(panel.getSelectedImageIndices == null)
  }
}
