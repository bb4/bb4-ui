/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.ui.table

import javax.swing._
import javax.swing.event.ListSelectionListener
import javax.swing.table.TableModel
import scala.collection.JavaConverters
import TableBase.conv


object TableBase {
  private def conv(items: java.util.ArrayList[AnyRef]): Seq[_] = {
    JavaConverters.asScalaIteratorConverter(items.iterator).asScala.toSeq
  }
}

/**
  * This represents a generic table, with a set of columns and tooltips for those column headers.
  * @author Barry Becker
  */
abstract class TableBase() {

  protected var table: JTable = _

  /** information about each column and its header. */
  protected var columnMeta: Array[TableColumnMeta] = _

  def this(rows: Seq[_], columnNames: Array[String]) {
    this()
    initColumnMeta(columnNames)
    initializeTable(rows)
  }

  /** @param rows to initialize the rows in the table with.
    */
  def this(rows: Seq[_], columnMeta: Array[TableColumnMeta]) {
    this()
    this.columnMeta = columnMeta
    initializeTable(rows)
  }

  def this(rows: java.util.ArrayList[AnyRef], columnMeta: Array[TableColumnMeta]) {
    this(conv(rows), columnMeta)
  }

  def this(rows: java.util.ArrayList[AnyRef], columnNames: Array[String]) {
    this(conv(rows), columnNames)
  }

  protected def initColumnMeta(columnNames: Array[String]): Unit = {
    val columnMeta = new Array[TableColumnMeta](columnNames.length)
    for (i <- columnNames.indices) {
      columnMeta(i) = new TableColumnMeta(columnNames(i), columnNames(i))
    }
    this.columnMeta = columnMeta
  }

  /**
    * @param rows initial data to show in the table.
    */
  protected def initializeTable(rows: Seq[_]): Unit = {
    val columnNames = new Array[String](columnMeta.length)
    for (i <- columnMeta.indices) {
      columnNames(i) = columnMeta(i).getName
    }
    val m = createTableModel(columnNames)
    table = new JTable(m)
    updateColumnMeta(columnMeta)
    for (meta <- columnMeta) {
      meta.initializeColumn(table)
    }
    table.doLayout()
    if (rows != null) {
      for (p <- rows) {
        addRow(p)
      }
    }
  }

  protected def addRow(row: Any): Unit

  /**
    * Override to assign specific tooltips, widths, renderers and editors on a per column basis.
    * @param columnMeta meta
    */
  protected def updateColumnMeta(columnMeta: Array[TableColumnMeta]): Unit = {
    // does nothing by default
  }

  protected def createTableModel(columnNames: Array[String]): TableModel
  def getSelectedRow: Int = table.getSelectedRow
  def getTable: JTable = table
  def getModel: TableModel = table.getModel
  protected def getNumColumns: Int = columnMeta.length
  def getNumRows: Int = table.getRowCount

  def addListSelectionListener(l: ListSelectionListener): Unit = {
    table.getSelectionModel.addListSelectionListener(l)
  }

  protected def setRowHeight(height: Int): Unit = {
    table.setRowHeight(height)
  }
}
