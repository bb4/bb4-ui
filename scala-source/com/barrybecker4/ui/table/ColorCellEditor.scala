/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import javax.swing._
import java.awt._

import javax.swing.table.TableCellEditor


/**
  * ColorCellRenderer renders a cell in a table that represents a color
  * @see ColorCellRenderer
  * @author Barry Becker
  */
class ColorCellEditor(var title: String) extends AbstractCellEditor with TableCellEditor  {

  private[table] val cellRenderer = new ColorCellRenderer

  // why need this now?
  override def getCellEditorValue: Any = {
    Color.BLACK
  }

  override def getTableCellEditorComponent(table: JTable, value: Any, isSelected: Boolean,
                                           row: Int, col: Int): Component = {
    val color = value.asInstanceOf[Color]
    var selectedColor = JColorChooser.showDialog(table, title, color)
    if (selectedColor == null) { // then it was canceled.
      selectedColor = color
    }

    table.setValueAt(selectedColor, row, col)

    // shouldn't need this
    table.getModel.setValueAt(selectedColor, row, col)
    cellRenderer.getTableCellRendererComponent(table, selectedColor, isSelected = true, isSelected, row, col)
  }
}
