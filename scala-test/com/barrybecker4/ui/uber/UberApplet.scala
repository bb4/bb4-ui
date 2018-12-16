/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.uber

import com.barrybecker4.ui.application.ApplicationApplet
import com.barrybecker4.ui.util.GUIUtil
import javax.swing.JPanel
import javax.swing.JTabbedPane
import java.awt.BorderLayout
import java.util


object UberApplet {
  val IMAGE_ROOT = "com/barrybecker4/ui/uber/images/" // NON-NLS

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val applet = new UberApplet
    GUIUtil.showApplet(applet)
  }
}

/**
  * An app that tries to demonstrate the use of most of the UI components in this package.
  * @author Barry Becker
  */
class UberApplet() extends ApplicationApplet {

  override protected def createMainPanel: JPanel = {
    val mainPanel = new JPanel
    mainPanel.setLayout(new BorderLayout)
    mainPanel.add(new UberTabbedPanel, BorderLayout.CENTER)
    mainPanel
  }

  override protected def getResourceList: util.List[String] = {
    val list = super.getResourceList
    list.add("com.barrybecker4.ui.uber.message")
    list
  }

  protected def createUI(): Unit = {}
}
