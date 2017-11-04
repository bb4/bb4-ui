/*
 * Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
 */
package com.barrybecker4.ui.sliders

import java.awt._


/**
  * @author Barry Becker
  */
trait ColorChangeListener {

  /** @param color the newly changed color. */
  def colorChanged(color: Color): Unit
}
