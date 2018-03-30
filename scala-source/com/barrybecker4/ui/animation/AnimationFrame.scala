// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import com.barrybecker4.ui.application.ApplicationFrame
import java.awt._


final class AnimationFrame(val component: AnimationComponent, title: String)
  extends ApplicationFrame(title) with AnimationChangeListener {

  /** Shows the current animation status. Like the frame rate for example */
  private var statusLabel = new Label()

  val contentPane: Container = this.getContentPane
  contentPane.setLayout(new BorderLayout)
  contentPane.add(component, BorderLayout.CENTER)
  contentPane.add(statusLabel, BorderLayout.SOUTH)
  component.setChangeListener(this)
  startAnimation(component)


  /**
    * Constructor
    * @param component the animation component to show and animate.
    */
  def this(component: AnimationComponent) {
    this(component, null)
  }

  private def startAnimation(component: AnimationComponent) = {
    val thread = new Thread(component)
    thread.start()
  }

  override def statusChanged(message: String): Unit = {
    statusLabel.setText(message)
  }
}