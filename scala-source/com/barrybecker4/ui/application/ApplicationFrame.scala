// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.application

import javax.swing._
import java.awt._
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


/**
  * Boiler plate code for a scala application.
  * @author Barry Becker
  */
object ApplicationFrame {
  val DEFAULT_TITLE = "Application Frame"

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    new ApplicationFrame("Test Frame")
    println("bb4-ui is meant to be used as a UI library for Scala or Java applications.")
  }
}

class ApplicationFrame(title: String)
  extends JFrame(if (title == null) ApplicationFrame.DEFAULT_TITLE else title) {
  createUI()

  def this() {
    this(ApplicationFrame.DEFAULT_TITLE)
  }

  protected def createUI(): Unit = {
    setSize(500, 400)
    center()
    addWindowListener(new WindowAdapter() {
      override def windowClosing(e: WindowEvent): Unit = {
        dispose()
        System.exit(0)
      }
    })
    this.setVisible(true)
  }

  def center(): Unit = {
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val frameSize = getSize
    val x = (screenSize.width - frameSize.width) >> 1
    val y = (screenSize.height - frameSize.height) >> 1
    setLocation(x, y)
  }
}