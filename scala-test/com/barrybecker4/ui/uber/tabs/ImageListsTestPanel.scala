/*
 * Copyright by Barry G. Becker, 2022. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.uber.tabs

import com.barrybecker4.ui.components.{ImageListsScrollPanel, TexturedPanel}
import com.barrybecker4.ui.uber.UberAppConstants.IMAGE_ROOT
import com.barrybecker4.ui.uber.tabs.ImageListsTestPanel.imgAndTip
import com.barrybecker4.ui.uber.tabs.MainTexturePanel.BACKGROUND_IMG
import com.barrybecker4.ui.util.GUIUtil

import java.awt.BorderLayout


object ImageListsTestPanel {
  private def imgAndTip(fname: String, tip: String) = {
    (GUIUtil.getBufferedImage(IMAGE_ROOT + fname), tip)
  }
}

class ImageListsTestPanel() extends TexturedPanel(BACKGROUND_IMG) {

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
