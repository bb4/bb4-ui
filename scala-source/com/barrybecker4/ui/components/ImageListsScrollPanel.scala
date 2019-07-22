// Copyright by Barry G. Becker, 2017 - 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.event._
import java.awt.image.BufferedImage
import java.awt.{BorderLayout, Dimension, Graphics, Graphics2D}

import com.barrybecker4.ui.components.ImageListPanel.BACKGROUND_COLOR
import javax.swing._


/**
  * Displays lists of images as rows in a scrolling window.
  * Images do not have to all have the same size.
  * Specify the desired row height.
  * That height will be used for all images and aspect rations will be maintained.
  *
  * @author Barry Becker
  */
final class ImageListsScrollPanel(imgHeight: Int) extends JPanel {

  private val imageListsPanel: ImageListsPanel = new ImageListsPanel(imgHeight)

  this.setMinimumSize(new Dimension(100, 100))
  //this.setViewport(imageListsPanel)

  val scrollPane = new JScrollPane(imageListsPanel)
  this.setLayout(new BorderLayout())
  this.add(scrollPane, BorderLayout.CENTER)
  //scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)


  def setImageLists(imageLists: Seq[Seq[BufferedImage]]): Unit = {
    imageListsPanel.setImageLists(imageLists)
    this.repaint()
  }

}
