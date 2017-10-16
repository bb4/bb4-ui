// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

import java.awt.image.BufferedImage
import java.util.EventListener


/**
  * This interface must be implemented by any class that wants to receive ImageSelectedEvents
  * @author Barry Becker
  */
trait ImageSelectionListener extends EventListener {
  def imageSelected(img: BufferedImage): Unit
}
