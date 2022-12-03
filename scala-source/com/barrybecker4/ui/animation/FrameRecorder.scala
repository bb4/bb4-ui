// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import com.barrybecker4.ui.util.ImageUtil
import java.awt.{Component, Dimension, Graphics2D, Image}


/**
  * Records animation frame images to the disk with a name that includes the frame number.
  * @author Barry Becker
  */
class FrameRecorder(var fileNameBase: String) {
  /** An image showing the current animation frame */
  protected var image: Image = _

  /**
    * Same the current frame image if there is one.
    * @return true if the image was successfully saved.
    */
  def saveFrame(frameIndex: Long): Boolean = {
    if (image != null) {
      val fname = fileNameBase + (1000000L + frameIndex)
      ImageUtil.saveAsImage(fname, image, ImageUtil.ImageType.PNG)
      return true
    }
    false
  }

  /** render the animation component as an image */
  def renderImage(comp: Component): Unit = {
    val g = comp.getGraphics.asInstanceOf[Graphics2D]
    if (g != null) {
      val dimensions = comp.getSize
      if (checkImage(dimensions, comp)) {
        val imageGraphics = image.getGraphics
        // Clear the image background.
        imageGraphics.setColor(comp.getBackground)
        imageGraphics.fillRect(0, 0, dimensions.width, dimensions.height)
        imageGraphics.setColor(comp.getForeground)
        // Draw this component offscreen.
        comp.paint(imageGraphics)
        // Now put the offscreen image on the screen.
        g.drawImage(image, 0, 0, null)
        imageGraphics.dispose()
      }
    }
  }

  /** Check for offscreen image. Creates it if needed. */
  protected def checkImage(dimensions: Dimension, comp: Component): Boolean = {
    if (dimensions.width <= 0 || dimensions.height <= 0) return false
    if (image == null || image.getWidth(null) != dimensions.width || image.getHeight(null) != dimensions.height)
      image = comp.createImage(dimensions.width, dimensions.height)
    true
  }
}