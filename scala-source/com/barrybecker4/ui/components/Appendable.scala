// Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui.components

/**
  * Something that can append strings.
  * @author Barry Becker
  */
trait Appendable {

  /** @param message string to append to the appendable */
  def append(message: String): Unit
}
