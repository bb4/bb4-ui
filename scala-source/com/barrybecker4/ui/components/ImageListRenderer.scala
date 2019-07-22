// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}
import com.barrybecker4.ui.components.ImageListRenderer._


object ImageListRenderer {

  private val IMAGE_MARGIN = 2
  private val TOTAL_MARGIN = 2 * IMAGE_MARGIN
  private val HIGHLIGHT_COLOR = Color.ORANGE
  private val SELECTION_COLOR = Color.BLUE
  private val BORDER_STROKE = new BasicStroke(2.0f)

  private def calculateImageRatio(images: Seq[BufferedImage]): Double = {
    if (images == null || images.isEmpty) return 1.0
    // first assert that all the images are the same size
    val firstImage = images.head
    var w = firstImage.getWidth
    var h = firstImage.getHeight
    for (img <- images) {
      if (img != null) {
        assert(img.getWidth == w,
          s"Image dimensions ${img.getWidth}, ${img.getHeight} do not match first: $w, $h")
      }
    }
    w += TOTAL_MARGIN
    h += TOTAL_MARGIN
    w.toDouble / h.toDouble
  }
}

/**
  * Determines the layout and renders the list of images.
  */
final class ImageListRenderer(val images: Seq[BufferedImage]) {
  private var imageRatio = calculateImageRatio(images)
  private var baseImageWidth = images.head.getWidth + TOTAL_MARGIN
  private var numColumns = 0
  private var imageDisplayHeight = 0
  private var imageDisplayWidth = 0
  private var maxNumSelections = Int.MaxValue
  private var enlargeHighlightedImage: Boolean = false
  assert(images != null)


  def drawImages(g2: Graphics2D, width: Int, height: Int,
                 highlightedImage: BufferedImage,
                 selectedImages: Seq[BufferedImage]): Unit = {
    val panelRatio = width.toDouble / height.toDouble
    // find the number of rows that will give the closest match on the aspect ratios
    val numImages = images.size
    var numRows = 1
    numColumns = numImages
    var ratio = imageRatio * numImages
    var lastRatio: Double = 100000000
    while (ratio > panelRatio) {
      lastRatio = ratio
      numRows += 1
      numColumns = Math.ceil(numImages.toDouble / numRows.toDouble).toInt
      ratio = imageRatio * numColumns.toDouble / numRows.toDouble
      //println("ratio="+ ratio +" panelRatio=" + panelRatio + " numRows="+ numRows);
    }
    if (panelRatio - ratio < lastRatio - panelRatio) {
      // then we may have space on right side, but vertical space completely used
      imageDisplayHeight = Math.min(height / numRows, (baseImageWidth / imageRatio).toInt) - TOTAL_MARGIN
      imageDisplayWidth = (imageDisplayHeight * imageRatio).toInt
    }
    else { // horizontal space completely used
      numRows -= 1
      numColumns = Math.ceil(numImages.toDouble / numRows.toDouble).toInt
      imageDisplayWidth = Math.min(width / numColumns, baseImageWidth) - TOTAL_MARGIN
      imageDisplayHeight = (imageDisplayWidth / imageRatio).toInt
    }

    var enlargedImageIndex = -1
    g2.setStroke(BORDER_STROKE)
    for (i <- images.indices) {
      val colPos = getColumnPosition(i) + IMAGE_MARGIN
      val rowPos = getRowPosition(i) + IMAGE_MARGIN
      val img = images(i)
      if (img == highlightedImage && enlargeHighlightedImage) enlargedImageIndex = i
      g2.drawImage(img, colPos, rowPos, imageDisplayWidth, imageDisplayHeight, null)
      // put a border around images that are selected or highlighted
      if ((highlightedImage eq img) || selectedImages.contains(img)) {
        g2.setColor(if (highlightedImage eq img) HIGHLIGHT_COLOR else SELECTION_COLOR)
        g2.drawRect(
          colPos - IMAGE_MARGIN, rowPos - IMAGE_MARGIN,
          imageDisplayWidth + TOTAL_MARGIN, imageDisplayHeight + TOTAL_MARGIN)
      }
    }
    if (enlargedImageIndex >= 0) {
      drawEnlargedImage(g2, highlightedImage, width, height);
    }
  }

  /** @return the image that the mouse is currently over (at x, y coordinates) */
  def findImageOver(x: Int, y: Int): BufferedImage = {
    if (images == null) return null
    for (i <- images.indices) {
      val row = i / numColumns
      val col = i % numColumns
      val colPos = col * (imageDisplayWidth + TOTAL_MARGIN)
      val rowPos = row * (imageDisplayHeight + TOTAL_MARGIN)
      if (x > colPos && x <= colPos + imageDisplayWidth && y > rowPos && y <= rowPos + imageDisplayHeight) {
        return images(i)
      }
    }
    null
  }

  private def drawEnlargedImage(g2: Graphics2D, image: BufferedImage, width: Int, height: Int): Unit = {
    var w = image.getWidth
    var h = image.getHeight
    if (w > width) {
      w = width
      h = (w / imageRatio).toInt
    }
    if (h > height) {
      h = height
      w = (h * imageRatio).toInt
    }
    g2.drawImage(image, 0, 0, w, h, null)
  }

  private def getRowPosition(i: Int) = {
    val row = i / numColumns
    row * (imageDisplayHeight + TOTAL_MARGIN)
  }

  private def getColumnPosition(i: Int) = {
    val col = i % numColumns
    col * (imageDisplayWidth + TOTAL_MARGIN)
  }
}
