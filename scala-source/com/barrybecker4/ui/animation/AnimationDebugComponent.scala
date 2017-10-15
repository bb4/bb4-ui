// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import com.barrybecker4.common.app.AppContext
import java.awt._
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
  * For debugging animations.
  * @author Barry Becker
  */
abstract class AnimationDebugComponent() extends AnimationComponent with ActionListener {

  private var runNextStep = false
  protected var stepButton = new Button(AppContext.getLabel("ADVANCE_FRAME"))
  stepButton.addActionListener(this)

  override def run(): Unit = {
    while (isAnimating) {
      if (runNextStep) {
        render()
        timeStep
        runNextStep = false
        repaint()
      }
    }
  }

  def getStepButton: Button = stepButton

  override def actionPerformed(event: ActionEvent): Unit = {
    if (event.getSource eq stepButton) runNextStep = true
  }
}