// Copyright by Barry G. Becker, 2000-2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.animation

/**
  * Something that listens for changes of status with regard to the animation.
  * @author Barry Becker
  */
trait AnimationChangeListener {
  def statusChanged(message: String): Unit
}
