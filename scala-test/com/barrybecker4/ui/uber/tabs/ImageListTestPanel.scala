/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.ui.components.{ImageListPanel, TexturedPanel}
import com.barrybecker4.ui.uber.UberAppConstants.IMAGE_ROOT
import com.barrybecker4.ui.uber.tabs.MainTexturePanel.BACKGROUND_IMG
import com.barrybecker4.ui.util.GUIUtil

import java.awt.BorderLayout


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
