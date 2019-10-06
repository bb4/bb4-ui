// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.{Color, Dimension, Graphics, Graphics2D}
import javax.swing._
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.util
import java.util.{Timer, TimerTask}
import scala.jdk.CollectionConverters._
import ImageListPanel._


object ImageListPanel {

  val BACKGROUND_COLOR = Color.WHITE
}

/**
  * Displays an array of images that all have the same size.
  * The images are displayed in a way that uses the available space effectively without using a scrollbar.
  * The images may get smaller than their actual size, but will maintain aspect ratio, and will not get bigger.
  *
  * @author Barry Becker
  */
final class ImageListPanel() extends JPanel with MouseMotionListener with MouseListener {

  private var imageSelectionListeners = List[ImageSelectionListener]()
  private var images: Seq[BufferedImage] = _
  private var selectedImages: Seq[BufferedImage] = _
  private var maxNumSelections = Int.MaxValue
  private var renderer: ImageListRenderer = _

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
    renderer = new ImageListRenderer(images)

    selectedImages = Vector[BufferedImage]()
    this.repaint()
  }

  def setImageList(images: util.List[BufferedImage]): Unit = {
    assert(images != null)
    this.setImageList(images.asScala.toSeq)
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

  /** This is how the client can register itself to receive these events.
    * @param isl the listener to add
    */
  def addImageSelectionListener(isl: ImageSelectionListener): Unit = {
    imageSelectionListeners +:= isl
  }

  /** This is how the client can unregister itself to receive these events.
    * @param isl the listener  to remove
    */
  def removeImageSelectionListener(isl: ImageSelectionListener): Unit = {
    imageSelectionListeners.diff(List(isl))
  }

  def getSelectedImageIndices: Seq[Int] = {
    if (images == null) return null
    images.zipWithIndex.filter(img => selectedImages.contains(img._1)).map(img => img._2)
  }

  def setSelectedImageIndices(selectedIndices: Seq[Int]): Unit = {
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
    renderer.drawImages(g.asInstanceOf[Graphics2D], getWidth, getHeight, selectedImages)
  }

  override def mouseMoved(e: MouseEvent): Unit = {
    val image = renderer.findImageOver(e.getX, e.getY)
    val changed = image != renderer.getHighlightedImage
    if (changed) {
      renderer.setHighlightedImage(image, this)
      this.repaint()
    }
  }

  override def mouseDragged(e: MouseEvent): Unit = {}
  override def mouseClicked(e: MouseEvent): Unit = {}
  override def mousePressed(e: MouseEvent): Unit = {}

  /** An image was selected on release of the mouse click */
  override def mouseReleased(e: MouseEvent): Unit = {
    val img = renderer.findImageOver(e.getX, e.getY)
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
  private def updateImageSelectionListeners(selectedImage: BufferedImage): Unit = {
    imageSelectionListeners.foreach(_.imageSelected(selectedImage))
  }

  override def mouseEntered(e: MouseEvent): Unit = {}
  override def mouseExited(e: MouseEvent): Unit = {}
}
