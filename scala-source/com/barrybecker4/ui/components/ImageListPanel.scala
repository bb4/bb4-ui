// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.{BasicStroke, Color, Dimension, Graphics, Graphics2D}
import javax.swing._
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.util
import java.util.{Timer, TimerTask}
import collection.JavaConverters._
import com.barrybecker4.ui1.components.ImageSelectionListener
import ImageListPanel._


/**
  * Displays an array of images.
  * The images are displayed in a way that uses the available space effectively without using a scrollbar.
  * The images may get smaller than their actual size, but will maintain aspect ratio, and will not get bigger.
  * @author Barry Becker
  */
object ImageListPanel {
  private val TIME_TO_ENLARGE = 900
  private val BACKGROUND_COLOR = Color.WHITE
  private val HIGHLIGHT_COLOR = Color.ORANGE
  private val SELECTION_COLOR = Color.BLUE
  private val BORDER_STROKE = new BasicStroke(2.0f)
  private val IMAGE_MARGIN = 2
  private val TOTAL_MARGIN = 2 * IMAGE_MARGIN

  private def calculateImageRatio(images: Seq[BufferedImage]): Double = {
    if (images == null || images.isEmpty) return 1.0
    // first assert that all the images are the same size
    val firstImage = images.head
    var w = firstImage.getWidth
    var h = firstImage.getHeight
    for (img <- images) {
      assert(img.getWidth == w,
        "Image dimensions " + img.getWidth + ", " + img.getHeight + " do not match first: " + w + ", " + h)
    }
    w += TOTAL_MARGIN
    h += TOTAL_MARGIN
    w.toDouble / h.toDouble
  }
}

/**
  * Use this Constructor if you just want the empty panel initially.
  * Maybe add constructor for unequal sized images where we specify the desired max size.
  */
final class ImageListPanel() extends JPanel with MouseMotionListener with MouseListener {
  private var imageSelectionListeners = List[ImageSelectionListener]()
  private var images: Seq[BufferedImage] = _
  private var highlightedImage: BufferedImage = _
  private var selectedImages: Seq[BufferedImage] = _
  private var imageRatio = 0.0
  private var baseImageWidth = 0
  private var numColumns = 0
  private var imageDisplayHeight = 0
  private var imageDisplayWidth = 0
  private var maxNumSelections = Integer.MAX_VALUE
  private var enlargeHighlightedImage: Boolean = false

  this.setMinimumSize(new Dimension(100, 100))
  this.addComponentListener(new ComponentAdapter() {
    override def componentResized(ce: ComponentEvent): Unit = {repaint()}
  })
  this.addMouseMotionListener(this)
  this.addMouseListener(this)

  /** @param images array of identically sized images to show in an array.*/
  def this(images: util.List[BufferedImage]) {
    this()
    setImageList(images)
  }

  def setImageList(images: Seq[BufferedImage]): Unit = {
    assert(images != null)
    this.images = images
    imageRatio = calculateImageRatio(images)
    baseImageWidth = images.head.getWidth + TOTAL_MARGIN
    selectedImages = Vector[BufferedImage]()
    highlightedImage = null
    this.repaint()
  }
  def setImageList(images: util.List[BufferedImage]): Unit = {
    assert(images != null)
    this.setImageList(images.asScala)
  }

  def getImageList: Seq[BufferedImage] = images
  def setMaxNumSelections(max: Int): Unit = {maxNumSelections = max}

  /** Sometimes we just want to show a single image and have it fit the available area. */
  def setSingleImage(image: BufferedImage): Unit = {
    if (image == null) {
      println("warning: setting null image") //NON-NLS
      return
    }
    val imageList = new util.ArrayList[BufferedImage](1)
    imageList.add(image)
    setImageList(imageList)
  }

  /**
    * This is how the client can register itself to receive these events.
    * @param isl the listener to add
    */
  def addImageSelectionListener(isl: ImageSelectionListener): Unit = {imageSelectionListeners +:= isl}

  /**
    * This is how the client can unregister itself to receive these events.
    * @param isl the listener  to remove
    */
  def removeImageSelectionListener(isl: ImageSelectionListener): Unit = {
    imageSelectionListeners.diff(List(isl))
  }

  def getSelectedImageIndices: Seq[Int] = {
    if (images == null) return null
    images.zipWithIndex.filter(img => selectedImages.contains(img._1)).map(img => img._2)
  }

  def setSelectedImageIndices(selectedIndices: Seq[Integer]): Unit = {
    assert(selectedIndices != null)
    // replace what we have with the new selections
    selectedImages = images.zipWithIndex.filter(img => selectedIndices.contains(img._2)).map(_._1)
  }

  /** This renders the array of images to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponents(g)
    if (images == null || images.isEmpty) return
    // erase what's there and redraw.
    g.clearRect(0, 0, getWidth, getHeight)
    g.setColor(BACKGROUND_COLOR)
    g.fillRect(0, 0, getWidth, getHeight)
    drawImages(g.asInstanceOf[Graphics2D])
  }

  private def drawImages(g2: Graphics2D) = {
    val panelRatio = getWidth.toDouble / getHeight.toDouble
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
      imageDisplayHeight = Math.min(getHeight / numRows, (baseImageWidth / imageRatio).toInt) - TOTAL_MARGIN
      imageDisplayWidth = (imageDisplayHeight * imageRatio).toInt
    }
    else { // horizontal space completely used
      numRows -= 1
      numColumns = Math.ceil(numImages.toDouble / numRows.toDouble).toInt
      imageDisplayWidth = Math.min(getWidth / numColumns, baseImageWidth) - TOTAL_MARGIN
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
      var w = highlightedImage.getWidth
      var h = highlightedImage.getHeight
      if (w > getWidth) {
        w = getWidth
        h = (w / imageRatio).toInt
      }
      if (h > getHeight) {
        h = getHeight
        w = (h * imageRatio).toInt
      }
      g2.drawImage(highlightedImage, 0, 0, w, h, null)
    }
  }

  private def getRowPosition(i: Int) = {
    val row = i / numColumns
    row * (imageDisplayHeight + TOTAL_MARGIN)
  }

  private def getColumnPosition(i: Int) = {
    val col = i % numColumns
    col * (imageDisplayWidth + TOTAL_MARGIN)
  }

  /** @return the image that the mouse is currently over (at x, y coordinates) */
  private def findImageOver(x: Int, y: Int): BufferedImage = {
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

  override def mouseMoved(e: MouseEvent): Unit = {
    val image = findImageOver(e.getX, e.getY)
    val changed = image != highlightedImage
    if (changed) {
      if (image != null) highlightedImage = image
      else highlightedImage = null
      this.repaint()
      // start a time that is canceled if the mouse moves
      val enlargementTimer = new Timer
      enlargeHighlightedImage = false
      enlargementTimer.schedule(new TimerTask() {
        override def run(): Unit = {
          enlargeHighlightedImage = true
          enlargementTimer.cancel()
          repaint()
        }
      }, TIME_TO_ENLARGE)
    }
  }

  override def mouseDragged(e: MouseEvent): Unit = {}
  override def mouseClicked(e: MouseEvent): Unit = {}
  override def mousePressed(e: MouseEvent): Unit = {}

  /** An image was selected on release of the mouse click */
  override def mouseReleased(e: MouseEvent): Unit = {
    val img = findImageOver(e.getX, e.getY)
    if (img != null) {
      if (selectedImages.contains(img)) selectedImages.diff(Seq(img))
      else { // if we are at our limit we first need to remove the first selected
        if (selectedImages.size == maxNumSelections) selectedImages = selectedImages.tail
        selectedImages :+= img
      }
      this.repaint()
    }
    updateImageSelectionListeners(img)
  }

  /** make sure listeners get notification that an image was selected. */
  private def updateImageSelectionListeners(selectedImage: BufferedImage) = {
    imageSelectionListeners.foreach(_.imageSelected(selectedImage))
  }

  override def mouseEntered(e: MouseEvent): Unit = {}
  override def mouseExited(e: MouseEvent): Unit = {}
}