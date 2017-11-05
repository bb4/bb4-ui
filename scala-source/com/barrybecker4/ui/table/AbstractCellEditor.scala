package com.barrybecker4.ui.table

import javax.swing._
import javax.swing.event.CellEditorListener
import javax.swing.event.ChangeEvent
import javax.swing.event.EventListenerList
import javax.swing.table.TableCellEditor
import javax.swing.tree.TreeCellEditor
import java.awt.event.MouseEvent
import java.util.EventObject
import scala.collection.JavaConverters._


/**
  * Copied with modification from from Graphic Java (Swing) book (by David Geary).
  */
abstract class AbstractCellEditor extends TableCellEditor with TreeCellEditor {
  protected var listenerList = new EventListenerList
  protected var value: AnyRef = _
  protected var changeEvent: ChangeEvent = _
  protected var clickCountToStart = 1

  override def getCellEditorValue: AnyRef = value

  def setCellEditorValue(value: AnyRef): Unit = {
    this.value = value
  }

  def setClickCountToStart(count: Int): Unit = {
    clickCountToStart = count
  }

  def getClickCountToStart: Int = clickCountToStart

  override def isCellEditable(anEvent: EventObject): Boolean = {
    anEvent match {
      case event: MouseEvent => event.getClickCount >= clickCountToStart
      case _ => true
    }
  }

  override def shouldSelectCell(anEvent: EventObject) = true

  override def stopCellEditing: Boolean = {
    fireEditingStopped()
    true
  }

  override def cancelCellEditing(): Unit = {
    fireEditingCanceled()
  }

  override def addCellEditorListener(l: CellEditorListener): Unit = {
    listenerList.add(classOf[CellEditorListener], l)
  }

  override def removeCellEditorListener(l: CellEditorListener): Unit = {
    listenerList.remove(classOf[CellEditorListener], l)
  }

  override def getTreeCellEditorComponent(tree: JTree, value: Any,
                                          isSelected: Boolean, expanded: Boolean, leaf: Boolean, row: Int) = null

  protected def fireEditingStopped(): Unit = {
    val listeners = listenerList.getListenerList
    for (i <- listeners.length - 2 to 0 by -2) {
      if (listeners(i) eq classOf[CellEditorListener]) {
        if (changeEvent == null) changeEvent = new ChangeEvent(this)
        listeners(i + 1).asInstanceOf[CellEditorListener].editingStopped(changeEvent)
      }
    }
  }

  protected def fireEditingCanceled(): Unit = {
    val listeners = listenerList.getListenerList
    for (i <- listeners.length - 2 to 0 by -2) {
      if (listeners(i) eq classOf[CellEditorListener]) {
        if (changeEvent == null) changeEvent = new ChangeEvent(this)
        listeners(i + 1).asInstanceOf[CellEditorListener].editingCanceled(changeEvent)
      }
    }
  }
}
