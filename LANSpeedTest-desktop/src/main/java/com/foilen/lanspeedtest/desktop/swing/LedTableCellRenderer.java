/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

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
