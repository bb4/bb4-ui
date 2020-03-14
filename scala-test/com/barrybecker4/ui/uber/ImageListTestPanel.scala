// Copyright by Barry G. Becker, 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import java.awt.BorderLayout

import com.barrybecker4.ui.components.{ImageListPanel, TexturedPanel}
import com.barrybecker4.ui.util.GUIUtil
import UberAppConstants.IMAGE_ROOT
import MainTexturePanel.BACKGROUND_IMG


class ImageListTestPanel() extends TexturedPanel(BACKGROUND_IMG) {

  setLayout(new BorderLayout)

  private val images = Seq(
    GUIUtil.getBufferedImage(IMAGE_ROOT + "funnel_cloud.jpg"),
    GUIUtil.getBufferedImage(IMAGE_ROOT + "funnel_cloud.jpg"),
    GUIUtil.getBufferedImage(IMAGE_ROOT + "funnel_cloud.jpg"),
    GUIUtil.getBufferedImage(IMAGE_ROOT + "funnel_cloud.jpg"),
    GUIUtil.getBufferedImage(IMAGE_ROOT + "funnel_cloud.jpg")
  )

  val imageListPanel: ImageListPanel = new ImageListPanel()
  imageListPanel.setImageList(images)

  add(imageListPanel, BorderLayout.CENTER)

}
