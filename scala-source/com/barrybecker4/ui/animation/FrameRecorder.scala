// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import com.barrybecker4.ui.util.ImageUtil
import java.awt.{Component, Dimension, Graphics, Graphics2D, Image}

import javax.swing.SwingUtilities


/**
  * Records animation frame images to the disk with a name that includes the frame number.
  * Offscreen frame buffer updates are marshalled to the EDT; display happens in [[paintFrame]] on repaint.
  * @author Barry Becker
  */
class FrameRecorder(var fileNameBase: String) {
  /** An image showing the current animation frame (owned by the animation/recorder worker between renders). */
  protected var image: Image = null

  /**
    * Same the current frame image if there is one.
    * @return true if the image was successfully saved.
    */
  def saveFrame(frameIndex: Long): Boolean = {
    val frame = image
    if (frame != null) {
      val fname = fileNameBase + (1000000L + frameIndex)
      ImageUtil.saveAsImage(fname, frame, ImageUtil.ImageType.PNG)
      true
    } else false
  }

  def getFrameImage: Image = image

  /** Draw the latest completed offscreen frame (EDT). */
  def paintFrame(g: Graphics): Unit = {
    val frame = image
    if (frame != null) g.drawImage(frame, 0, 0, null)
  }

  /** Render the animation component into the offscreen buffer without touching on-screen graphics. */
  def renderImage(comp: Component): Unit = {
    val dimensions = comp.getSize
    if (!checkImage(dimensions, comp)) return

    def paintOffscreen(): Unit = {
      val imageGraphics = image.getGraphics
      if (imageGraphics != null) {
        try {
          imageGraphics.setColor(comp.getBackground)
          imageGraphics.fillRect(0, 0, dimensions.width, dimensions.height)
          imageGraphics.setColor(comp.getForeground)
          comp.paint(imageGraphics)
        } finally
          imageGraphics.dispose()
      }
    }

    if (SwingUtilities.isEventDispatchThread) paintOffscreen()
    else SwingUtilities.invokeAndWait(() => paintOffscreen())
  }

  /** Check for offscreen image. Creates it if needed. */
  protected def checkImage(dimensions: Dimension, comp: Component): Boolean = {
    if (dimensions.width <= 0 || dimensions.height <= 0) return false
    if (image == null || image.getWidth(null) != dimensions.width || image.getHeight(null) != dimensions.height)
      image = comp.createImage(dimensions.width, dimensions.height)
    true
  }
}
