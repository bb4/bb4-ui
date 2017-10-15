// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import com.barrybecker4.ui1.util.GUIUtil
import javax.swing._
import java.awt._


/**
  * Panel for showing an animation
  * @param component animation component to animate.
  * @author Barry Becker
  */
final class AnimationPanel(val component: AnimationComponent)

  extends JPanel with AnimationChangeListener {
  setLayout(new BorderLayout)
  setFont(new Font(GUIUtil.DEFAULT_FONT_FAMILY, Font.PLAIN, 12))
  this.add(component, BorderLayout.CENTER)
  private var statusLabel = new Label
  this.add(statusLabel, BorderLayout.SOUTH)
  component.setChangeListener(this)
  startAnimation(component)

  private def startAnimation(ac: AnimationComponent) = {
    val thread = new Thread(ac)
    thread.start()
  }

  override def statusChanged(message: String): Unit = {
    if (message != null) statusLabel.setText(message)
  }
}