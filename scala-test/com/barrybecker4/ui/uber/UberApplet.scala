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
    val tabbedPanel = new JTabbedPane
    //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    tabbedPanel.addTab("Input Elements", new MainTexturePanel)
    tabbedPanel.addTab("Histogram", new HistogramTestPanel)
    tabbedPanel.addTab("Multi-Function", new MultiFunctionTestPanel)
    //this.add(mainPanel);
    mainPanel.setLayout(new BorderLayout)
    mainPanel.add(tabbedPanel, BorderLayout.CENTER)
    mainPanel
    //super.createUI();
  }

  override protected def getResourceList: List[String] =
    super.getResourceList :+ "com.barrybecker4.ui.uber.message"

  protected def createUI(): Unit = {}
}
