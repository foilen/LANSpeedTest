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

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class LedTableCellRenderer implements TableCellRenderer {

    private ImageIcon iconGreen;
    private ImageIcon iconRed;
    private JLabel led;

    public LedTableCellRenderer() {
        led = new JLabel();
        iconGreen = new ImageIcon(PrincipalGui.class.getResource("/com/foilen/lanspeedtest/desktop/swing/glyphicons-64-power-green.png"));
        iconRed = new ImageIcon(PrincipalGui.class.getResource("/com/foilen/lanspeedtest/desktop/swing/glyphicons-64-power-red.png"));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        led.setIcon((boolean) value ? iconGreen : iconRed);
        return led;
    }

}
