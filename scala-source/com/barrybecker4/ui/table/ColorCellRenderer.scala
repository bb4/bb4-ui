/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT*/
package com.barrybecker4.ui.table

import javax.swing._
import javax.swing.table.DefaultTableCellRenderer
import java.awt._


/**
  * ColorCellRenderer renders a cell in a table that represents a color
  * @see ColorCellEditor
  * @author Barry Becker
  */
class ColorCellRenderer() extends DefaultTableCellRenderer {
  setHorizontalAlignment(SwingConstants.CENTER)

  override def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean,
                                             row: Int, col: Int): Component = {
    val color = if (value == null) Color.GRAY else value.asInstanceOf[Color]
    setBackground(color)
    setToolTipText(color.toString)
    this
  }
}
