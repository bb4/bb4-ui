// Copyright by Barry G. Becker, 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import java.awt.BorderLayout

import com.barrybecker4.ui.components.{ImageListPanel, ImageListsPanel, TexturedPanel}
import com.barrybecker4.ui.util.GUIUtil


class ImageListsTestPanel() extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) {

  setLayout(new BorderLayout)

  private val imageLists = Seq(
    Seq(
      GUIUtil.getBufferedImage(UberApp.IMAGE_ROOT + "funnel_cloud.jpg"),
      GUIUtil.getBufferedImage(UberApp.IMAGE_ROOT + "Barry-2019.jpg")
    ),
    Seq(
      GUIUtil.getBufferedImage(UberApp.IMAGE_ROOT + "ocean_trans_20.png"),
      GUIUtil.getBufferedImage(UberApp.IMAGE_ROOT + "pool_pennies_small.jpg"),
      GUIUtil.getBufferedImage(UberApp.IMAGE_ROOT + "sample_thumbnail.jpg")
    )
  )

  val imageListsPanel: ImageListsPanel = new ImageListsPanel(100)
  imageListsPanel.setImageList(imageLists)

  add(imageListsPanel, BorderLayout.CENTER)

}
