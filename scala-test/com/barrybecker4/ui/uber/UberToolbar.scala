/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber

import com.barrybecker4.ui1.components.GradientButton
import com.barrybecker4.ui1.components.TexturedToolBar
import com.barrybecker4.ui1.util.GUIUtil
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionListener

import com.barrybecker4.ui1.uber.UberApp


/**
  * Sample Toolbar that appears a the top of the application window.
  * @author Barry Becker
  */
class UberToolbar(listener: ActionListener)
  extends TexturedToolBar(GUIUtil.getIcon(UberApp.IMAGE_ROOT + "ocean_trans_20.png"), listener) {

  init()

  private def init(): Unit = {
    val sampleImage = GUIUtil.getIcon(UberApp.IMAGE_ROOT + "sample_thumbnail.jpg")
    val sampleButton1 = createToolBarButton("Sample Button1", "Some tooltip text1", sampleImage)
    val sampleButton2 = new GradientButton("Sample Button2")
    sampleButton2.setStartColor(Color.BLUE)
    sampleButton2.setEndColor(Color.CYAN)
    sampleButton2.setMaximumSize(new Dimension(160, 25))
    val sampleButton3 = createToolBarButton("Button3", "Some tooltip text3", sampleImage)
    add(sampleButton1)
    add(sampleButton2)
    add(sampleButton3)
  }
}
