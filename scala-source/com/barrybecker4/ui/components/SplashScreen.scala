// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import javax.swing._
import java.awt._
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent


/**
  * Given an image show a splash screen to fill the time until the Application starts.
  * The user can click on it to make it go away.
  * Source is mostly copied from http://www.javaworld.com/javaworld/javatips/jw-javatip104.html
  * @param image    image to show in a borderless window
  * @param frame    owning frame (may be null)
  * @param waitTime time to wait in milliseconds before closing the splash screen
  * @author Barry Becker
  */
final class SplashScreen(val image: Icon, frame: Frame, val waitTime: Int) extends JWindow(frame) {
  addLabel(image)
  val splashThread: Thread = createSplashThread(waitTime)
  setVisible(true)
  splashThread.start()

  /**
    * Shows the splash screen until the user clicks on it.
    * @param waitTime time to wait before auto dismissing if the user has not clicked.
    * @return the thread taht will show the splash
    */
  private def createSplashThread(waitTime: Int) = {
    addMouseListener(new MouseAdapter() {
      override def mousePressed(e: MouseEvent): Unit = {
        setVisible(false)
        dispose()
      }
    })
    val pause = waitTime
    val closerRunner = new Runnable() {
      override def run(): Unit = {
        setVisible(false)
        dispose()
      }
    }
    val waitRunner = new Runnable() {
      override def run(): Unit = {
        try {
          Thread.sleep(pause)
          SwingUtilities.invokeAndWait(closerRunner)
        } catch {
          case e: Exception =>
            e.printStackTrace()
          // can catch InvocationTargetException
          // can catch InterruptedException
        }
      }
    }
    new Thread(waitRunner, "SplashThread")
  }

  private def addLabel(image: Icon) = {
    val label = new JLabel(image)
    label.setBorder(BorderFactory.createRaisedBevelBorder)
    getContentPane.add(label, BorderLayout.CENTER)
    pack()
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val labelSize = label.getPreferredSize
    setLocation(
      (screenSize.width >> 1) - (labelSize.width >> 1),
      (screenSize.height >> 1) - (labelSize.height >> 1))
  }
}