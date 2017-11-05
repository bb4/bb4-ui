/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import javax.swing.table.DefaultTableCellRenderer


/**
  * Renders a cell in a table that has a tooltip
  * @see ColorCellEditor
  * @author Barry Becker
  */
class BasicCellRenderer() extends DefaultTableCellRenderer {

  override protected def setValue(value: Any): Unit = {
    super.setValue(value)
    this.setToolTipText(if (value == null) ""
    else value.toString)
  }
}
