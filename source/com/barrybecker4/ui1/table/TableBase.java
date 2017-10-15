// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.table;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * This represents a generic table, with a set of columns and tooltips for those column headers.
 *
 * @author Barry Becker
 */
public abstract class TableBase {

    protected JTable table;

    /** information about each column and its header. */
    protected TableColumnMeta[] columnMeta;

    public TableBase() {}

    /**
     * Constructor
     */
    public TableBase(List rows, String[] columnNames) {
        initColumnMeta(columnNames);
        initializeTable(rows);
    }

    /**
     * constructor
     * @param rows to initialize the rows in the table with.
     */
    public TableBase(List rows, TableColumnMeta[] columnMeta) {
        this.columnMeta = columnMeta;
        initializeTable(rows);
    }

    protected void initColumnMeta(String[] columnNames) {
        TableColumnMeta[] columnMeta = new TableColumnMeta[columnNames.length];
        for (int i=0; i<columnNames.length; i++) {
            columnMeta[i] = new TableColumnMeta(columnNames[i], columnNames[i]);
        }
        this.columnMeta = columnMeta;
    }

    /**
     * @param rows initial data to show in the table.
     */
    protected void initializeTable(List rows) {

        String[] columnNames = new String[columnMeta.length];
        for (int i = 0; i< columnMeta.length; i++) {
            columnNames[i] = columnMeta[i].getName();
        }
        TableModel m = createTableModel(columnNames);
        table = new JTable(m);

        updateColumnMeta(columnMeta);

        for (TableColumnMeta meta : columnMeta) {
            meta.initializeColumn(table);
        }

        table.doLayout();

        if (rows != null) {
            for (Object p : rows) {
                addRow(p);
            }
        }
    }

    protected abstract void addRow(Object row);

    /**
     * override to assign specific tooltips, widths, renderers and editors on a per column basis.
     * @param columnMeta
     */
    protected void updateColumnMeta(TableColumnMeta[] columnMeta) {
        // does nothing by default
    }

    protected abstract TableModel createTableModel(String[] columnNames);


    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public JTable getTable()  {
        return table;
    }

    public void addListSelectionListener(ListSelectionListener l) {
        table.getSelectionModel().addListSelectionListener(l);
    }

    public TableModel getModel() {
        return table.getModel();
    }

    protected void setRowHeight(int height) {
        table.setRowHeight(height);
    }

    protected int getNumColumns() {
        return columnMeta.length;
    }

    public int getNumRows() {
        return table.getRowCount();
    }
}
