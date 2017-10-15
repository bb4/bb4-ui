// Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.ui1.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Meta data information about a column in a Table.
 * @author Barry Becker
 */
public class TableColumnMeta {

    /** name of the column */
    private String name;
    /** mouse over tip (optional)   */
    private String tooltip;
    private Integer minWidth;
    private Integer preferredWidth;
    private Integer maxWidth;
    private boolean resizable = true;
    private TableCellRenderer cellRenderer = null;
    private TableCellEditor cellEditor = null;

    public TableColumnMeta(String name, String tooltip) {
        this.name = name;
        this.tooltip = tooltip;
    }

    public TableColumnMeta(String name, String tooltip, int minWidth, int preferredWidth, int maxWidth) {
        this(name, tooltip);
        this.minWidth = minWidth;
        this.preferredWidth = preferredWidth;
        this.maxWidth = maxWidth;
    }

    /**
     * Initialize the column in this table for this metaColumn data.
     * @param table
     */
    public void initializeColumn(JTable table) {
        String name = getName();
        TableColumn column = table.getColumn(name);
        if (getTooltip() != null)  {
            TableCellRenderer r = new HeaderRenderer();
            JComponent c = (JComponent)r.getTableCellRendererComponent(table, name, false, false, 0, 0);
            c.setToolTipText(getTooltip());
            column.setHeaderRenderer(r);
        }
        if (getMinWidth() != null) {
            column.setMinWidth(getMinWidth());
        }
        if (getPreferredWidth() != null) {
            column.setPreferredWidth(getPreferredWidth());
        }
        if (getMaxWidth() != null) {
            column.setMaxWidth(getMaxWidth());
        }
        if (getCellRenderer() != null) {
            column.setCellRenderer(getCellRenderer());
        }
        if (getCellEditor() != null) {
            column.setCellEditor(getCellEditor());
        }
    }

    public String getName() {
        return name;
    }


    public void setTooltip(String tip) {
        tooltip = tip;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setMinWidth(int w) {
        minWidth = w;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setPreferredWidth(int w) {
        preferredWidth = w;
    }

    public Integer getPreferredWidth() {
        return preferredWidth;
    }

    public void setMaxWidth(int w) {
        maxWidth = w;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }


    public TableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(TableCellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public TableCellEditor getCellEditor() {
        return cellEditor;
    }

    public void setCellEditor(TableCellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }

}
