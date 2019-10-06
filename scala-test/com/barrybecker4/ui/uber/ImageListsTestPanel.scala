// Copyright by Barry G. Becker, 2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.uber

import java.awt.BorderLayout
import com.barrybecker4.ui.components.{ImageListsScrollPanel, TexturedPanel}
import com.barrybecker4.ui.util.GUIUtil
import ImageListsTestPanel.imgAndTip


object ImageListsTestPanel {
  private def imgAndTip(fname: String, tip: String) = {
    (GUIUtil.getBufferedImage(UberApp.IMAGE_ROOT + fname), tip)
  }
}

class ImageListsTestPanel() extends TexturedPanel(MainTexturePanel.BACKGROUND_IMG) {

  setLayout(new BorderLayout)

  private val imageLists = Seq(
    Seq(
      imgAndTip("funnel_cloud.jpg", "funnel cloud"),
      imgAndTip("Barry-2019.jpg", "Barry's portrait")
    ),
    Seq(
      imgAndTip("ocean_trans_20.png", "transparent ocean"),
      imgAndTip("pool_pennies_small.jpg", "pennies at the bottom of a pool"),
      imgAndTip("sample_thumbnail.jpg", "sample thumbnail image"),
      imgAndTip("pool_pennies_small.jpg", "pennies in a pool"),
    )
  )

  val imageListsPanel = new ImageListsScrollPanel(200)
  imageListsPanel.setImageListsWithTips(imageLists)
  add(imageListsPanel, BorderLayout.CENTER)
}
