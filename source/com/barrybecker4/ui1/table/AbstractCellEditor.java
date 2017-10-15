package com.barrybecker4.ui1.table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Copied with modification from from Graphic Java (Swing) book (by David Geary).
 */
public abstract class AbstractCellEditor
                implements TableCellEditor, TreeCellEditor {

    protected EventListenerList listenerList = new EventListenerList();
    protected Object value;
    protected ChangeEvent changeEvent = null;
    protected int clickCountToStart = 1;

    public Object getCellEditorValue() {
        return value;
    }

    public void setCellEditorValue(Object value) {
        this.value = value;
    }

    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    public int getClickCountToStart() {
        return clickCountToStart;
    }

    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            if (((MouseEvent)anEvent).getClickCount() <
                    clickCountToStart)
                return false;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    public Component getTreeCellEditorComponent(
                        JTree tree, Object value,
                        boolean isSelected, boolean expanded,
                        boolean leaf, int row) {
        return null;
    }


    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener)
                listeners[i+1]).editingStopped(changeEvent);
            }
        }
    }

    protected void fireEditingCanceled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener)
                listeners[i+1]).editingCanceled(changeEvent);
            }
        }
    }
}
