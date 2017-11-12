// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.dialogs

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.ui.components.ImageListPanel
import javax.swing._
import java.awt._
import java.awt.event.ActionListener
import java.awt.image.BufferedImage


/**
  * Show an image in a preview dialog.
  * @author Barry Becker
  */
class ImagePreviewDialog(var image: BufferedImage) extends AbstractDialog with ActionListener {
  this.setResizable(true)
  setTitle(AppContext.getLabel("IMAGE_PREVIEW"))
  this.setModal(true)
  showContent()

  override protected def createDialogContent: JComponent = {
    val mainPanel = new JPanel(new BorderLayout)
    mainPanel.add(createImagePanel, BorderLayout.CENTER)
    mainPanel.add(createButtonsPanel, BorderLayout.SOUTH)
    mainPanel
  }

  private def createImagePanel = {
    val imagePanel = new ImageListPanel
    imagePanel.setMaxNumSelections(1)
    imagePanel.setPreferredSize(new Dimension(700, 400))
    imagePanel.setSingleImage(image)
    imagePanel
  }

  /** Create the buttons that go at the bottom ( eg OK, Cancel, ...) */
  protected def createButtonsPanel: JPanel = {
    val buttonsPanel = new JPanel(new FlowLayout)
    initBottomButton(cancelButton, AppContext.getLabel("CANCEL"), "Cancel image prview")
    buttonsPanel.add(cancelButton)
    buttonsPanel
  }
}
