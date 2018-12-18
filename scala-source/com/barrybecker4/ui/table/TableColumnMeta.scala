/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import javax.swing._
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer


/**
  * Meta data information about a column in a Table.
  * @param name name of the column
  * @param tooltip  mouse over tip (optional)
  * @author Barry Becker
  */
class TableColumnMeta(var name: String, var tooltip: String) {

  private var minWidth: Int = _
  private var preferredWidth: Int = _
  private var maxWidth: Int = _
  private var resizable = true
  private var cellRenderer: TableCellRenderer = _
  private var cellEditor: TableCellEditor = _

  def this(name: String, tooltip: String, minWidth: Int, preferredWidth: Int, maxWidth: Int) {
    this(name, tooltip)
    this.minWidth = minWidth
    this.preferredWidth = preferredWidth
    this.maxWidth = maxWidth
  }

  /**
    * Initialize the column in this table for this metaColumn data.
    * @param table the table component
    */
  def initializeColumn(table: JTable): Unit = {
    val name = getName
    val column = table.getColumn(name)
    if (getTooltip != null) {
      val r = new HeaderRenderer
      val c = r.getTableCellRendererComponent(table, name, selected = false, focused = false, 0, 0)
        .asInstanceOf[JComponent]
      c.setToolTipText(getTooltip)
      column.setHeaderRenderer(r)
    }
    if (getMinWidth != 0) column.setMinWidth(getMinWidth)
    if (getPreferredWidth != 0) column.setPreferredWidth(getPreferredWidth)
    if (getMaxWidth != 0) column.setMaxWidth(getMaxWidth)
    if (getCellRenderer != null) column.setCellRenderer(getCellRenderer)
    if (getCellEditor != null) column.setCellEditor(getCellEditor)
  }

  def getName: String = name

  def setTooltip(tip: String): Unit = {tooltip = tip}
  def getTooltip: String = tooltip
  def setMinWidth(w: Int): Unit = {minWidth = w}
  def getMinWidth: Integer = minWidth
  def setPreferredWidth(w: Int): Unit = {preferredWidth = w}
  def getPreferredWidth: Integer = preferredWidth
  def setMaxWidth(w: Int): Unit = {maxWidth = w}
  def getMaxWidth: Integer = maxWidth
  def isResizable: Boolean = resizable
  def setResizable(resizable: Boolean): Unit = {this.resizable = resizable}
  def getCellRenderer: TableCellRenderer = cellRenderer
  def getCellEditor: TableCellEditor = cellEditor

  def setCellRenderer(cellRenderer: TableCellRenderer): Unit = {
    this.cellRenderer = cellRenderer
  }

  def setCellEditor(cellEditor: TableCellEditor): Unit = {
    this.cellEditor = cellEditor
  }
}
