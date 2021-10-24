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

  @deprecated("Use main constructor that takes (columnNames: Array, rowCount: Int, isEditable: Boolean) instead",
    "bb4-ui 1.6")
  def this(data: Array[Array[AnyRef]], columnNames: Array[AnyRef], isEditable: Boolean) = {
    this(columnNames, 0, false)
    throw new UnsupportedOperationException("This constructor is not supported")
  }

  override def getColumnClass(col: Int): Class[_] = {
    dataVector.elementAt(0) match {
      //case list: List[_] => list(col).getClass
      case vec: java.util.Vector[_] => vec.get(col).getClass
      case _ => throw new IllegalArgumentException("Unexpected type: " + dataVector.elementAt(0))
    }
  }

  override def isCellEditable(row: Int, column: Int): Boolean = editable
}
