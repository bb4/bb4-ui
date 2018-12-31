/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.animation

import com.barrybecker4.common.concurrency.ThreadUtil
import org.junit.Assert.assertEquals
import org.scalatest.{BeforeAndAfter, FunSuite}

class FrameRateCalculatorSuite extends FunSuite with BeforeAndAfter {

  /** instance under test. */
  private var calculator: FrameRateCalculator = _


  before {
    calculator = new FrameRateCalculator
  }


  /** The frame rate given on the first frame is 0 because no frame rendered yet. */
  test("DefaultFrameRate") {
    assertResult(0) { calculator.getFrameCount }
    assertEquals("Unexpected initial frame rate", 0.0, calculator.getFrameRate, 0.001)
  }

  test("FrameCount") {
    calculator.incrementFrameCount()
    calculator.incrementFrameCount()
    assertEquals("Unexpected count", 2, calculator.getFrameCount)
  }

  test("FrameRateAfter1") {
    ThreadUtil.sleep(100)
    calculator.incrementFrameCount()
    assertEquals("Unexpected initial frame rate.", 9.0, calculator.getFrameRate, 1.5)
  }

  test("InitialFrameRateWithDelay") {
    ThreadUtil.sleep(100)
    assertEquals("Unexpected initial frame rate.", 0.0, calculator.getFrameRate, 1.0)
  }

  test("FrameRateAfter1WithDelay") {
    calculator.incrementFrameCount()
    ThreadUtil.sleep(100)
    assertEquals("Unexpected initial frame rate.", 9.7, calculator.getFrameRate, 1.2)
  }

  test("FrameRateAfter3") {
    verifyFrameRateAfterN(3, 0, 0.0, 3000)
  }

  test("FrameRateAfter3WithDelayForAllButLast") {
    calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.incrementFrameCount()
    assertEquals("Unexpected frame rate.", 27.0, calculator.getFrameRate, 3.0)
  }

  test("FrameRateAWithPause") {
    calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.setPaused(true)
    ThreadUtil.sleep(200)
    calculator.setPaused(false)
    calculator.incrementFrameCount()
    assertEquals("Unexpected frame rate.", 28.0, calculator.getFrameRate, 4.0)
  }

  test("FrameRateAWithTwoPauses") {
    calculator.incrementFrameCount()
    calculator.setPaused(false)
    ThreadUtil.sleep(50)
    calculator.setPaused(true)
    ThreadUtil.sleep(200)
    calculator.setPaused(false)
    calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.setPaused(false)
    calculator.setPaused(true)
    ThreadUtil.sleep(200)
    calculator.setPaused(false)
    calculator.incrementFrameCount()
    assertEquals("Unexpected frame rate.", 28.0, calculator.getFrameRate, 8.0)
  }

  test("FrameRateAWithTwoLongPausesAndManyIncrements") {
    calculator.setPaused(false)
    for (i <- 0 to 10)
      calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.setPaused(true)
    for (i <- 0 to 10)
      calculator.incrementFrameCount()
    ThreadUtil.sleep(200)
    calculator.setPaused(false)
    for (i <- 0 to 10)
      calculator.incrementFrameCount()
    ThreadUtil.sleep(50)
    calculator.setPaused(false)
    for (i <- 0 to 10)
      calculator.incrementFrameCount()
    calculator.setPaused(true)
    ThreadUtil.sleep(200)
    calculator.setPaused(false)
    for (i <- 0 to 10)
      calculator.incrementFrameCount()
    ThreadUtil.sleep(40)
    assertEquals("Unexpected frame rate.", 346.0, calculator.getFrameRate, 12.0)
  }

  test("FrameRateAfter3WithDelayOf50") {
    verifyFrameRateAfterN(3, 50, 17.0)
  }

  test("FrameRateAfter3WithDelayOf100") {
    verifyFrameRateAfterN(3, 100, 9.8)
  }

  test("FrameRateAfter6WithDelayOf100") {
    verifyFrameRateAfterN(6, 100, 9.5)
  }

  test("FrameRateAfter12WithDelayOf100") {
    verifyFrameRateAfterN(12, 100, 9.0)
  }

  test("FrameRateAfter24WithDelayOf100") {
    verifyFrameRateAfterN(24, 100, 9.9)
  }

  test("FrameRateAfterFilled") {
    verifyFrameRateAfterN(100, 0, 0)
  }

  test("FrameRateWithDelayAfterFilled100") {
    verifyFrameRateAfterN(100, 10, 92, 9.0)
  }

  test("FrameRateWithDelayAfterFilled200") {
    verifyFrameRateAfterN(200, 20, 47, 8.0)
  }


  private def verifyFrameRateAfterN(numFrames: Int, frameDelay: Int, expRate: Double): Unit = {
    verifyFrameRateAfterN(numFrames, frameDelay, expRate, 3.0)
  }

  /**
    * @param numFrames  number of frames to simulate
    * @param frameDelay artificial delay in milliseconds to create each frame
    * @param expRate    expected frame rate.
    * @param tolerance  tolerance threshold for expected result.
    */
  private def verifyFrameRateAfterN(numFrames: Int, frameDelay: Int, expRate: Double, tolerance: Double): Unit = {
    for (i <- 0 until numFrames) {
      calculator.incrementFrameCount()
      ThreadUtil.sleep(frameDelay)
    }
    assertEquals("Unexpected frameRate", expRate, calculator.getFrameRate, tolerance)
  }
}
