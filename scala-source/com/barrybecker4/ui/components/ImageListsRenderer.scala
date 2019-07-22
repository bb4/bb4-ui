// Copyright by Barry G. Becker, 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.{BasicStroke, Dimension, Graphics2D}
import java.awt.image.BufferedImage
import ImageListsRenderer._


object ImageListsRenderer {
  private val IMG_MARGIN = 2
  private val BORDER_STROKE = new BasicStroke(2.0f)

}

/**
  * Determines the layout and renders the lists of images in rows.
  * Each list goes in its own row. The heights of the images are all the same, but widths may vary.
  */
final class ImageListsRenderer(val imageLists: Seq[Seq[BufferedImage]], imgHeight: Int) {
  private var totalHeight = imageLists.size * (IMG_MARGIN + imgHeight)
  private var totalWidth = calculateMaxRowWidth()

  def getDimensions = new Dimension(totalWidth, totalHeight)

  def drawImages(g2: Graphics2D): Unit = {
    g2.setStroke(BORDER_STROKE)
    var yOffset = IMG_MARGIN
    imageLists.foreach(imgList => {
      renderRow(g2, imgList, yOffset)
      yOffset += imgHeight + IMG_MARGIN
    })
  }

  private def renderRow(g2: Graphics2D, imgList: Seq[BufferedImage], yOffset: Int): Unit = {
    var xOffset = IMG_MARGIN
    for (img <- imgList) {
      val imgWidth = calculateScaledWidth(img)
      g2.drawImage(img, xOffset, yOffset, imgWidth, imgHeight, null)
      xOffset += imgWidth + IMG_MARGIN
    }
  }

  private def calculateMaxRowWidth(): Int =
    imageLists.map(calculateRowWidth).max

  private def calculateRowWidth(images: Seq[BufferedImage]): Int =
    images.map(calculateScaledWidth).sum

  private def calculateScaledWidth(img: BufferedImage): Int = {
    img.getWidth * imgHeight / img.getHeight()
  }
}
