// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

/**
  * Calculates the framerate given previous times in milliseconds
  *
  * @see AnimationComponent
  * @author Barry Becker
  */
object FrameRateCalculator {
  /** keep times for the last 64 frames */
  private val HISTORY_LENGTH = 32
}

class FrameRateCalculator() {
  /** previous times in milliseconds. */
  private var previousTimes = new Array[Long](FrameRateCalculator.HISTORY_LENGTH)
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
      else totalPauseTime += (System.currentTimeMillis - startPauseTime)
      isPaused = paused
    }
  }

  /**
    * Determine the number of frames per second as a moving average.
    * Use the more stable method of calculation if all history is available.
    */
  private def calculateFrameRate() = {
    val now = System.currentTimeMillis
    var deltaTime = .0
    val index = getIndex
    if (frameCount < FrameRateCalculator.HISTORY_LENGTH) {
      deltaTime = now - previousTimes(0) - totalPauseTime
      frameRate = if (deltaTime == 0) 0.0
      else (1000.0 * index) / deltaTime
    }
    else {
      deltaTime = now - previousTimes((index + 1) % FrameRateCalculator.HISTORY_LENGTH) - totalPauseTime
      frameRate = (1000.0 * FrameRateCalculator.HISTORY_LENGTH) / deltaTime
    }
    //System.out.println("index=" + index + " deltaTime="  + deltaTime + " fct=" + frameCount + " fr="+ frameRate);
    dirty = false
  }

  private def getIndex = (frameCount % FrameRateCalculator.HISTORY_LENGTH).toInt
}