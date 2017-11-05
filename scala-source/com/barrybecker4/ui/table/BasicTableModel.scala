/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */

package com.barrybecker4.ui.table

import javax.swing.table.DefaultTableModel


/**
  * Basically the DefaultTableModel with a few customizations.
  * @author Barry Becker
  */
@SerialVersionUID(0)
class BasicTableModel(data: Array[Array[AnyRef]], columnNames: Array[AnyRef], isEditable: Boolean)
  extends DefaultTableModel(data, columnNames) {

  private val editable = isEditable

  def this(columnNames: Array[AnyRef], rowCount: Int) {
    this(null, columnNames, false)
    this.setRowCount(rowCount)
  }

  override def getColumnClass(col: Int): Class[_] = {
    val v = dataVector.elementAt(0).asInstanceOf[List[_]]
    v(col).getClass
  }

  override def isCellEditable(row: Int, column: Int): Boolean = editable
}
