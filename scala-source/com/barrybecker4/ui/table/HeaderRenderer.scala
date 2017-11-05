/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.table

import javax.swing._
import javax.swing.table.DefaultTableCellRenderer
import java.awt._


/**
  * Use this for a TableHeader Renderer instead of the DefaultTableCellRenderer
  * @author Barry Becker
  */
class HeaderRenderer() extends DefaultTableCellRenderer {
  setHorizontalAlignment(SwingConstants.CENTER)
  setOpaque(true)
  // This call is needed because DefaultTableCellRenderer calls setBorder()
  // in its constructor, which is executed after updateUI()
  setBorder(UIManager.getBorder("TableHeader.cellBorder"))

  override def updateUI(): Unit = {
    super.updateUI()
    setBorder(UIManager.getBorder("TableHeader.cellBorder"))
  }

  override def getTableCellRendererComponent(table: JTable, value: Any, selected: Boolean, focused: Boolean,
                                             row: Int, column: Int): Component = {
    val h = if (table != null) table.getTableHeader
    else null
    if (h != null) {
      setEnabled(h.isEnabled)
      setComponentOrientation(h.getComponentOrientation)
      setForeground(h.getForeground)
      setBackground(h.getBackground)
      setFont(h.getFont)
    }
    else {
      // Use sensible values instead of random leftover values from the last call
      setEnabled(true)
      setComponentOrientation(ComponentOrientation.UNKNOWN)
      setForeground(UIManager.getColor("TableHeader.foreground"))
      setBackground(UIManager.getColor("TableHeader.background"))
      setFont(UIManager.getFont("TableHeader.font"))
    }
    setValue(value)
    this
  }
}