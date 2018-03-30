// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import com.barrybecker4.common.app.AppContext
import com.barrybecker4.common.concurrency.ThreadUtil
import com.barrybecker4.common.format.FormatUtil
import javax.swing.JComponent
import javax.swing.JToggleButton
import java.awt.event.ItemEvent
import java.awt.event.ItemListener


/**
  * A ui component for showing animations.
  * The calculation and animation rendering are done in a separate thread so the
  * rest of the ui does not lock up.
  */
abstract class AnimationComponent()

/** Constructor */
  extends JComponent with Runnable {
  /** parameters controlling the animation */
  private var params = new AnimationParameters()
  private var frameRateCalc = new FrameRateCalculator
  /** records images showing animation frames */
  protected var recorder = new FrameRecorder(getFileNameBase)


  /**
    * if recordAnimation is true then each frame is written to a numbered file for
    * compilation into a movie later
    *
    * @param doIt if true, then the animation will be recorded.
    */
  def setRecordAnimation(doIt: Boolean): Unit = params.recordAnimation = doIt
  def getRecordAnimation: Boolean = params.recordAnimation

  /**
    * set the number of time steps to computer for every frame of animation
    * for unstable calculations using simple numerical methods (like Eulers integration for eg)
    * this can speed things a lot.
    *
    * @param num number of time steps to calculate each animation frame.
    */
  def setNumStepsPerFrame(num: Int): Unit = params.numStepsPerFrame = num
  def getNumStepsPerFrame: Int = params.numStepsPerFrame
  def timeStep: Double

  /** @return the base filename when recording  */
  protected def getFileNameBase: String

  /**
    * Do the timeStepping and rendering in a separate thread
    * so the rest of the GUI does not freeze and can still handle events.
    */
  override def run(): Unit = {
    render()
    while ( {
      params.animating
    }) if (isPaused) ThreadUtil.sleep(500)
    else {
      frameRateCalc.incrementFrameCount()
      render()
      if (params.recordAnimation) recorder.saveFrame(frameRateCalc.getFrameCount)
      var i = 0
      while (i < params.getNumStepsPerFrame) {
        timeStep
        i += 1
      }
      animationChangeListener.statusChanged(getStatusMessage)
    }
  }

  protected def isAnimating: Boolean = params.animating

  protected def setAnimating(animating: Boolean): Unit = {
    if (animating != params.animating) if (animating) {
      params.animating = true
      new Thread(this).start()
    }
    else params.animating = false
  }

  /** @return a start button that says Pause or Resume once started */
  protected def createStartButton: JToggleButton = {
    val toggleButton = new JToggleButton(AppContext.getLabel("START"), true)
    toggleButton.addItemListener(new ItemListener() {
      override def itemStateChanged(ie: ItemEvent): Unit = {
        val paused = ie.getStateChange == ItemEvent.SELECTED
        toggleButton.setText(if (paused) AppContext.getLabel("RESUME")
        else AppContext.getLabel("PAUSE"))
        setPaused(paused)
      }
    })
    toggleButton
  }

  /** render the animation component as an image */
  protected def render(): Unit = {
    recorder.renderImage(this)
  }

  /** Message to show in the status bar at the bottom */
  protected def getStatusMessage: String =
    FormatUtil.formatNumber(getFrameRate) + AppContext.getLabel("FPS") + " "

  def getFrameRate: Double = frameRateCalc.getFrameRate

  /**
    * If paused is true the animation is stopped
    * @param paused true if you want the animation to stop temporarily.
    */
  def setPaused(paused: Boolean): Unit = {
    params.paused = paused
    frameRateCalc.setPaused(paused)
  }

  /** @return true if currently paused */
  def isPaused: Boolean = params.paused

  /** Property change support. */
  private var animationChangeListener: AnimationChangeListener = _

  def setChangeListener(af: AnimationChangeListener): Unit =
    animationChangeListener = af
}