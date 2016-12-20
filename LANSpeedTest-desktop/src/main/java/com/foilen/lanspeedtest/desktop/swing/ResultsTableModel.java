/*
    LANSpeedTest https://github.com/foilen/LANSpeedTest
    Copyright (C) 2016-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package com.foilen.lanspeedtest.desktop.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.foilen.lanspeedtest.core.events.ServerFoundEvent;
import com.foilen.lanspeedtest.core.events.ServerLostEvent;
import com.foilen.lanspeedtest.core.events.TestBeginEvent;
import com.foilen.lanspeedtest.core.events.TestCompleteEvent;

public class ResultsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    static private final String[] COLUMN_NAMES = new String[] { "Name", "", "Host", "Download (mbps)", "Upload (mbps)", "Download (mBps)", "Upload (mBps)", "Comment", "Action" };

    private MesureButtonTableCellRendererAndEditor mesureButtonTableCellRendererAndEditor;

    private List<ResultModel> rows = new ArrayList<>();

    public ResultsTableModel(MesureButtonTableCellRendererAndEditor mesureButtonTableCellRendererAndEditor) {
        this.mesureButtonTableCellRendererAndEditor = mesureButtonTableCellRendererAndEditor;
    }

    public void addOrUpdate(ServerFoundEvent event) {

        int existingRowIndex = findRow(genKey(event));

        if (existingRowIndex == -1) {
            // Add
            rows.add(new ResultModel(event));
            int newIndex = rows.size() - 1;
            fireTableRowsInserted(newIndex, newIndex);
        } else {
            // Update
            ResultModel row = rows.get(existingRowIndex);
            row.setPort(event.getPort());
            row.setActive(true);
            fireTableRowsUpdated(existingRowIndex, existingRowIndex);
        }

    }

    private int findRow(String key) {
        for (int i = 0; i < rows.size(); ++i) {
            if (genKey(rows.get(i)).equals(key)) {
                return i;
            }
        }

        return -1;
    }

    private String genKey(ResultModel resultModel) {
        return resultModel.getName() + "/" + resultModel.getHost();
    }

    private String genKey(ServerFoundEvent event) {
        return event.getName() + "/" + event.getHost();
    }

    private String genKey(ServerLostEvent event) {
        return event.getName() + "/" + event.getHost();
    }

    private String genKey(TestBeginEvent event) {
        return event.getName() + "/" + event.getHost();
    }

    private String genKey(TestCompleteEvent event) {
        return event.getName() + "/" + event.getHost();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    public ResultModel getRow(int rowIndex) {
        if (rowIndex >= rows.size()) {
            throw new IllegalArgumentException("requested row does not exists");
        }

        return rows.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        ResultModel row = getRow(rowIndex);
        switch (columnIndex) {
        case 0:
            return row.getName();
        case 1:
            return row.isActive();
        case 2:
            return row.getHost();
        case 3:
            return row.getDownloadSpeedMbps();
        case 4:
            return row.getUploadSpeedMbps();
        case 5:
            return row.getDownloadSpeedMbps() == null ? null : row.getDownloadSpeedMbps() / 8;
        case 6:
            return row.getUploadSpeedMbps() == null ? null : row.getUploadSpeedMbps() / 8;
        case 7:
            return row.getComment();
        case 8:
            return "Mesure";
        default:
            throw new IllegalArgumentException("requested column does not exists");
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 8;
    }

    public void update(ServerLostEvent event) {

        int existingRowIndex = findRow(genKey(event));

        if (existingRowIndex != -1) {
            // Update
            ResultModel row = rows.get(existingRowIndex);
            row.setPort(event.getPort());
            row.setActive(false);
            fireTableRowsUpdated(existingRowIndex, existingRowIndex);
        }

    }

    public void update(TestBeginEvent event) {
        int existingRowIndex = findRow(genKey(event));

        if (existingRowIndex != -1) {
            // Update
            ResultModel row = rows.get(existingRowIndex);
            row.setDownloadSpeedMbps(null);
            row.setUploadSpeedMbps(null);
            row.setComment("Testing...");
            fireTableRowsUpdated(existingRowIndex, existingRowIndex);
        }
    }

    public void update(TestCompleteEvent event) {
        int existingRowIndex = findRow(genKey(event));

        if (existingRowIndex != -1) {
            // Update
            ResultModel row = rows.get(existingRowIndex);
            row.setDownloadSpeedMbps(event.getDownloadSpeedMbps());
            row.setUploadSpeedMbps(event.getUploadSpeedMbps());
            row.setComment(event.getComment());
            row.setPendingMesure(false);

            // Must update the "editedRow" if it is this one
            if (row == mesureButtonTableCellRendererAndEditor.getEditedRow()) {
                mesureButtonTableCellRendererAndEditor.getButtonEdit().setEnabled(true);
            }
            fireTableRowsUpdated(existingRowIndex, existingRowIndex);
        }
    }

}
