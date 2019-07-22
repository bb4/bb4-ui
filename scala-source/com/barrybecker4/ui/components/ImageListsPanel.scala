// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.event._
import java.awt.image.BufferedImage
import java.awt.{Color, Dimension, Graphics, Graphics2D}
import javax.swing._
import ImageListPanel.BACKGROUND_COLOR


/**
  * Displays lists of images as rows in a scrolling window.
  * Images do not have to all have the same size.
  * Specify the desired row height.
  * That height will be used for all images and aspect rations will be maintained.
  *
  * @author Barry Becker
  */
final class ImageListsPanel(imgHeight: Int) extends JPanel {

  private var imageLists: Seq[Seq[BufferedImage]] = _
  private var renderer: ImageListsRenderer = _

  this.setMinimumSize(new Dimension(100, 100))

  this.addComponentListener(new ComponentAdapter() {
    override def componentResized(ce: ComponentEvent): Unit = {
      repaint()
    }
  })

  def setImageLists(imageLists: Seq[Seq[BufferedImage]]): Unit = {
    assert(imageLists != null)
    this.imageLists = imageLists
    renderer = new ImageListsRenderer(imageLists, imgHeight)
    this.setPreferredSize(renderer.getDimensions)
    this.repaint()
  }

  def getImageLists: Seq[Seq[BufferedImage]] = imageLists

  /** This renders the array of images to the screen. */
  override protected def paintComponent(g: Graphics): Unit = {
    super.paintComponents(g)
    if (imageLists == null || imageLists.isEmpty) return
    // erase what's there and redraw.
    g.clearRect(0, 0, getWidth, getHeight)
    g.setColor(BACKGROUND_COLOR)
    g.fillRect(0, 0, getWidth, getHeight)
    renderer.drawImages(g.asInstanceOf[Graphics2D])
  }
}
