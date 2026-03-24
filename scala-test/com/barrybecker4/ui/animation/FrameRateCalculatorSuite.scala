/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.animation

import org.junit.jupiter.api.Assertions.assertEquals
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter

import scala.compiletime.uninitialized

class FrameRateCalculatorSuite extends AnyFunSuite with BeforeAndAfter {

  /** Mutable wall clock for deterministic tests (milliseconds). */
  private var fakeClockMs = 0L

  private var calculator: FrameRateCalculator = uninitialized

  before {
    fakeClockMs = 0L
    calculator = new FrameRateCalculator(() => fakeClockMs)
  }

  /** The frame rate given on the first frame is 0 because no frame rendered yet. */
  test("DefaultFrameRate") {
    assertResult(0) { calculator.getFrameCount }
    assertEquals(0.0, calculator.getFrameRate, 0.001, "Unexpected initial frame rate")
  }

  test("FrameCount") {
    calculator.incrementFrameCount()
    calculator.incrementFrameCount()
    assertEquals(2, calculator.getFrameCount, "Unexpected count")
  }

  test("FrameRateAfter1") {
    fakeClockMs += 100
    calculator.incrementFrameCount()
    assertEquals(10.0, calculator.getFrameRate, 0.01, "Unexpected initial frame rate.")
  }

  test("InitialFrameRateWithDelay") {
    fakeClockMs += 100
    assertEquals(0.0, calculator.getFrameRate, 1.0, "Unexpected initial frame rate.")
  }

  test("FrameRateAfter1WithDelay") {
    calculator.incrementFrameCount()
    fakeClockMs += 100
    assertEquals(10.0, calculator.getFrameRate, 0.01, "Unexpected initial frame rate.")
  }

  test("FrameRateAfter3") {
    verifyFrameRateAfterN(3, 0, 0.0, 3000)
  }

  test("FrameRateAfter3WithDelayForAllButLast") {
    calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.incrementFrameCount()
    assertEquals(30.0, calculator.getFrameRate, 0.01, "Unexpected frame rate.")
  }

  test("FrameRateAWithPause") {
    calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.setPaused(true)
    fakeClockMs += 200
    calculator.setPaused(false)
    calculator.incrementFrameCount()
    assertEquals(30.0, calculator.getFrameRate, 0.01, "Unexpected frame rate.")
  }

  test("FrameRateAWithTwoPauses") {
    calculator.incrementFrameCount()
    calculator.setPaused(false)
    fakeClockMs += 50
    calculator.setPaused(true)
    fakeClockMs += 200
    calculator.setPaused(false)
    calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.setPaused(false)
    calculator.setPaused(true)
    fakeClockMs += 200
    calculator.setPaused(false)
    calculator.incrementFrameCount()
    assertEquals(30.0, calculator.getFrameRate, 0.01, "Unexpected frame rate.")
  }

  test("FrameRateAWithTwoLongPausesAndManyIncrements") {
    calculator.setPaused(false)
    for (_ <- 0 to 10)
      calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.setPaused(true)
    for (_ <- 0 to 10)
      calculator.incrementFrameCount()
    fakeClockMs += 200
    calculator.setPaused(false)
    for (_ <- 0 to 10)
      calculator.incrementFrameCount()
    fakeClockMs += 50
    calculator.setPaused(false)
    for (_ <- 0 to 10)
      calculator.incrementFrameCount()
    calculator.setPaused(true)
    fakeClockMs += 200
    calculator.setPaused(false)
    for (_ <- 0 to 10)
      calculator.incrementFrameCount()
    fakeClockMs += 40
    assertEquals(355.55555555555554, calculator.getFrameRate, 0.01, "Unexpected frame rate.")
  }

  test("FrameRateAfter3WithDelayOf50") {
    verifyFrameRateAfterN(3, 50, 20.0, 0.01)
  }

  test("FrameRateAfter3WithDelayOf100") {
    verifyFrameRateAfterN(3, 100, 10.0)
  }

  test("FrameRateAfter6WithDelayOf100") {
    verifyFrameRateAfterN(6, 100, 10.0)
  }

  test("FrameRateAfter12WithDelayOf100") {
    verifyFrameRateAfterN(12, 100, 10.0)
  }

  test("FrameRateAfter24WithDelayOf100") {
    verifyFrameRateAfterN(24, 100, 10.0)
  }

  test("FrameRateAfterFilled") {
    verifyFrameRateAfterN(100, 0, 0)
  }

  test("FrameRateWithDelayAfterFilled100") {
    verifyFrameRateAfterN(100, 10, 100.0, 0.01)
  }

  test("FrameRateWithDelayAfterFilled200") {
    verifyFrameRateAfterN(200, 20, 50.0, 0.01)
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
    for (_ <- 0 until numFrames) {
      calculator.incrementFrameCount()
      fakeClockMs += frameDelay
    }
    assertEquals(expRate, calculator.getFrameRate, tolerance, "Unexpected frameRate")
  }
}
