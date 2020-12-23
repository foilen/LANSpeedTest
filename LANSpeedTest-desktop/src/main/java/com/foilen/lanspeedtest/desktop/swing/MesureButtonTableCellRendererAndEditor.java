/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.desktop.swing;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.foilen.lanspeedtest.core.SpeedTestCore;

public class MesureButtonTableCellRendererAndEditor implements TableCellEditor, TableCellRenderer {

    private static final String ACTION_MESURE = "mesure";

    private SpeedTestCore speedTestCore;

    private JButton buttonView;
    private JButton buttonEdit;
    private ResultModel editedRow;

    public MesureButtonTableCellRendererAndEditor(SpeedTestCore speedTestCore) {

        this.speedTestCore = speedTestCore;

        buttonView = new JButton();

        buttonEdit = new JButton();
        buttonEdit.setActionCommand(ACTION_MESURE);
        buttonEdit.addActionListener(event -> {
            if (ACTION_MESURE.equals(event.getActionCommand())) {
                editedRow.setPendingMesure(true);
                buttonEdit.setEnabled(false);
                this.speedTestCore.queueExecuteTest(editedRow.getName(), editedRow.getHost(), editedRow.getPort());
            }
        });
    }

    @Override
    public void addCellEditorListener(CellEditorListener listener) {
    }

    @Override
    public void cancelCellEditing() {
        editedRow = null;
    }

    public JButton getButtonEdit() {
        return buttonEdit;
    }

    @Override
    public Object getCellEditorValue() {
        return editedRow;
    }

    public ResultModel getEditedRow() {
        return editedRow;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean highlighting, int rowIndex, int columnIndex) {
        editedRow = ((ResultsTableModel) table.getModel()).getRow(rowIndex);
        buttonEdit.setText((String) value);
        buttonEdit.setEnabled(!editedRow.isPendingMesure());
        return buttonEdit;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        ResultModel row = ((ResultsTableModel) table.getModel()).getRow(rowIndex);
        buttonView.setText((String) value);
        buttonView.setEnabled(!row.isPendingMesure());
        return buttonView;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        return true;
    }

    @Override
    public void removeCellEditorListener(CellEditorListener listener) {
    }

    @Override
    public boolean shouldSelectCell(EventObject event) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

}
