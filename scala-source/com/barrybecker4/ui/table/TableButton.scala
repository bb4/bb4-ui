/* Copyright by Barry G. Becker, 2017. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.ui.table

import com.barrybecker4.ui.components.GradientButton
import javax.swing._
import javax.swing.event.CellEditorListener
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer
import javax.swing.table.TableModel
import java.awt._
import java.awt.event.ActionEvent
import java.util.EventObject


/**
  * A button that can be placed in a table cell.
  * Add a TableButtonListener to do something when clicked.
  * @param text label for all the buttons in the column
  * @param theId used to identify the button clicked in the tableButton handler.
  * @author Barry Becker
  */
class TableButton(text: String, theId: String) extends GradientButton(text) with TableCellRenderer with TableCellEditor {
  private var selectedRow: Int = 0
  private var selectedColumn: Int = 0
  private var listeners: Set[TableButtonListener] = _
  final private var id: String = theId
  private var columnIndex: Int = 0
  private var disabledValues: Seq[AnyRef] = _
  commonInit()

  /**
    * Constructor
    * @param columnIndex the column that has the label to show in the button.
    * @param id          used to identify the button clicked in the tableButton handler.
    */
  def this(columnIndex: Int, id: String) {
    this("---", id)
    this.columnIndex = columnIndex
  }

  private def commonInit(): Unit = {
    listeners = Set[TableButtonListener]()
    addActionListener((e: ActionEvent) => {
      for (lsnr <- listeners) {
        lsnr.tableButtonClicked(selectedRow, selectedColumn, id)
      }
    })
    // no disabled values by default
    disabledValues = Seq[AnyRef]()
  }

  def addTableButtonListener(lsnr: TableButtonListener): Unit = {listeners += lsnr}
  def removeTableButtonListener(lsnr: TableButtonListener): Unit = {listeners -= lsnr}

  /**
    * Optional special cell values for which the button should be disabled.
    * @param disabledValues values to disable button for.
    */
  def setDisabledValues(disabledValues: Seq[AnyRef]): Unit = {
    this.disabledValues = disabledValues
  }

  override def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, col: Int): Component = {
    setLabel(table.getModel, row)
    this
  }

  override def getTableCellEditorComponent(table: JTable, value: Any, isSelected: Boolean, row: Int, col: Int): Component = {
    selectedRow = row
    selectedColumn = col
    setLabel(table.getModel, row)
    this
  }

  private def setLabel(tableModel: TableModel, row: Int): Unit = {
    val cellValue = tableModel.getValueAt(row, columnIndex)
    val isNullValued = cellValue == null
    if (columnIndex >= 0 && !isNullValued) this.setText(cellValue.toString)
    this.setEnabled(!(isNullValued || disabledValues.contains(cellValue)))
  }

  override def addCellEditorListener(arg0: CellEditorListener): Unit = {}
  override def cancelCellEditing(): Unit = {}
  override def getCellEditorValue = ""
  override def isCellEditable(arg0: EventObject) = true
  override def removeCellEditorListener(arg0: CellEditorListener): Unit = {}
  override def shouldSelectCell(arg0: EventObject) = false // was true
  override def stopCellEditing = true
}
