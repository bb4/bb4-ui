/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.animation

import java.awt.Dimension

import javax.swing.JPanel
import org.scalatest.funsuite.AnyFunSuite

class FrameRecorderSuite extends AnyFunSuite {

  private class TestRecorder extends FrameRecorder("out") {
    def acceptsSize(dim: Dimension, comp: java.awt.Component): Boolean = checkImage(dim, comp)
  }

  test("saveFrame returns false when no frame image exists") {
    val recorder = new FrameRecorder("out")
    assert(!recorder.saveFrame(0))
  }

  test("checkImage rejects non-positive sizes") {
    val recorder = new TestRecorder()
    val panel = new JPanel()
    javax.swing.SwingUtilities.invokeAndWait(() => panel.setSize(0, 0))
    assert(!recorder.acceptsSize(panel.getSize, panel))
  }

  test("renderImage creates frame buffer when component has positive size") {
    val recorder = new FrameRecorder("out")
    val panel = new JPanel() {
      override def paint(g: java.awt.Graphics): Unit = {
        g.setColor(java.awt.Color.BLUE)
        g.fillRect(0, 0, getWidth, getHeight)
      }
    }
    javax.swing.SwingUtilities.invokeAndWait(() => panel.setSize(32, 32))
    recorder.renderImage(panel)
    assert(recorder.getFrameImage != null)
    assert(recorder.getFrameImage.getWidth(null) == 32)
  }
}
