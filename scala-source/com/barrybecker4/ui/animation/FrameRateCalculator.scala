// Copyright by Barry G. Becker, 2017 - 2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

import FrameRateCalculator.HISTORY_LENGTH

/**
  * Calculates the frame rate given previous times in milliseconds
  * @see AnimationComponent
  * @author Barry Becker
  */
object FrameRateCalculator {
  /** keep times for this many recent frames */
  private val HISTORY_LENGTH = 32
}

class FrameRateCalculator() {

  /** previous times in milliseconds. */
  private val previousTimes = new Array[Long](HISTORY_LENGTH)

  /** incremented for every frame that is shown */
  private var frameCount = 0L

  previousTimes(0) = System.currentTimeMillis

  /** frames per second. */
  private var frameRate = .0
  /** becomes dirty when the frameCount changes */

  private var dirty = true
  private var startPauseTime = 0L
  private var totalPauseTime: Long = 0L
  private var isPaused = false

  /** This must be called right before each frame is rendered. */
  def incrementFrameCount(): Unit = {
    frameCount += 1
    previousTimes(getIndex) = System.currentTimeMillis
    dirty = true
  }

  /** @return the number of animation frames so far */
  def getFrameCount: Long = frameCount

  def getFrameRate: Double = {
    if (dirty) calculateFrameRate()
    frameRate
  }

  def setPaused(paused: Boolean): Unit = {
    if (paused != isPaused) {
      if (paused) startPauseTime = System.currentTimeMillis
      else totalPauseTime += System.currentTimeMillis - startPauseTime
      isPaused = paused
    }
  }

  /** Determine the number of frames per second as a moving average.
    * Use the more stable method of calculation if all history is available.
    */
  private def calculateFrameRate() = {
    var deltaTime = .0
    val index = getIndex
    if (frameCount < HISTORY_LENGTH) {
      deltaTime = System.currentTimeMillis - previousTimes(0) - totalPauseTime
      frameRate = if (deltaTime == 0) 0.0 else (1000.0 * index) / deltaTime
    }
    else {
      deltaTime = System.currentTimeMillis - previousTimes((index + 1) % HISTORY_LENGTH) - totalPauseTime
      frameRate = (1000.0 * HISTORY_LENGTH) / deltaTime
    }
    if (frameRate < 0) {
      throw new IllegalStateException(
        s"The frame-rate unexpectedly fell below 0. " +
          s"index=$index deltaTime=$deltaTime fct=$frameCount fr=$frameRate totalPause=$totalPauseTime " +
          s"prev=${previousTimes((index + 1) % HISTORY_LENGTH)}")
    }
    dirty = false
  }

  private def getIndex = (frameCount % HISTORY_LENGTH).toInt
}
