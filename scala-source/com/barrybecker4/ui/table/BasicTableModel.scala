/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import javax.swing.table.DefaultTableModel


/**
  * Basically the DefaultTableModel with a few customizations.
  * @author Barry Becker
  */
@SerialVersionUID(0)
class BasicTableModel(columnNames: Array[AnyRef], rowCount: Int, isEditable: Boolean = false)
  extends DefaultTableModel(columnNames, rowCount) {

  private val editable = isEditable

  def this(data: Array[Array[AnyRef]], columnNames: Array[AnyRef], isEditable: Boolean) {
    this(columnNames, 0, false)
    throw new UnsupportedOperationException("This constructor is not supported")
  }

  override def getColumnClass(col: Int): Class[_] = {
    val v = dataVector.elementAt(0).asInstanceOf[List[_]]
    v(col).getClass
  }

  override def isCellEditable(row: Int, column: Int): Boolean = editable
}
