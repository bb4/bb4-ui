/* Copyright by Barry G. Becker, 2026. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.animation

import org.scalatest.funsuite.AnyFunSuite

class AnimationComponentSuite extends AnyFunSuite {

  private class StubAnimation extends AnimationComponent {
    override protected def getFileNameBase: String = "test-anim"
    override def timeStep: Double = 0.0
  }

  test("recording flag and steps-per-frame are configurable") {
    val anim = new StubAnimation()
    assert(!anim.getRecordAnimation)
    anim.setRecordAnimation(true)
    assert(anim.getRecordAnimation)
    assert(anim.getNumStepsPerFrame == 1)
    anim.setNumStepsPerFrame(4)
    assert(anim.getNumStepsPerFrame == 4)
  }

  test("pause state is readable and writable") {
    val anim = new StubAnimation()
    assert(anim.isPaused)
    anim.setPaused(false)
    assert(!anim.isPaused)
    anim.setPaused(true)
    assert(anim.isPaused)
  }
}
