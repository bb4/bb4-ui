/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import java.util.EventListener


/** Called when you click a TableButton in a table cell. */
trait TableButtonListener extends EventListener {
  def tableButtonClicked(row: Int, col: Int, e: String): Unit
}
