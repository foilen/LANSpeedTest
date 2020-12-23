/*
    LANSpeedTest https://github.com/foilen/LANSpeedTest
    Copyright (C) 2016-2017

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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foilen.lanspeedtest.core.SpeedTestCore;
import com.foilen.lanspeedtest.core.events.ServerFoundEvent;
import com.foilen.lanspeedtest.core.events.ServerLostEvent;
import com.foilen.lanspeedtest.core.events.TestBeginEvent;
import com.foilen.lanspeedtest.core.events.TestCompleteEvent;
import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.smalltools.tools.ThreadTools;
import com.google.common.eventbus.Subscribe;

import net.miginfocom.swing.MigLayout;

public class PrincipalGui extends JFrame {

    private static final long serialVersionUID = 1L;
    static private final Logger logger = LoggerFactory.getLogger(PrincipalGui.class);

    private static final String ACTION_START_SERVER = "start server";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        SpeedTestCore speedTestCore = new SpeedTestCore();

        EventQueue.invokeLater(() -> {
            try {
                PrincipalGui frame = new PrincipalGui(speedTestCore);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Stub
            new Thread(() -> {
                logger.info("Will send fake events");

                for (;;) {
                    logger.info("Will add 3 hosts");
                    ThreadTools.sleep(1000);
                    speedTestCore.sendEvent(new ServerFoundEvent("host1", "127.0.0.1", 2001));
                    ThreadTools.sleep(1000);
                    speedTestCore.sendEvent(new ServerFoundEvent("host2", "127.0.0.2", 2002));
                    ThreadTools.sleep(1000);
                    speedTestCore.sendEvent(new ServerFoundEvent("host3", "127.0.0.3", 2003));

                    ThreadTools.sleep(5000);
                    logger.info("Desactivate 1 host");
                    speedTestCore.sendEvent(new ServerLostEvent("host2", "127.0.0.2", 2002));

                }

            }, "Stub").start();
        });

    }

    private JTable table;

    private ResultsTableModel tableModel;

    private JLabel statusText = new JLabel();

    public PrincipalGui(SpeedTestCore speedTestCore) throws IOException {
        speedTestCore.startDiscoveringServers();
        speedTestCore.registerEventsHandler(this);
        setTitle("LAN Speed Test");
        setIconImage(ImageIO.read(ResourceTools.getResourceAsStream("icon.png", getClass())));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 677, 518);
        setMinimumSize(new Dimension(1080, 500));
        setResizable(true);
        JPanel appPanel = new JPanel();
        appPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        appPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(appPanel);

        // The content
        JPanel contentPanel = new JPanel();
        appPanel.add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout());

        // The top logos
        JLabel logo = new JLabel("");
        logo.setIcon(new ImageIcon(PrincipalGui.class.getResource("/com/foilen/lanspeedtest/desktop/swing/name.png")));
        contentPanel.add(logo);
        JLabel logoFoilen = new JLabel("");
        logoFoilen.setIcon(new ImageIcon(PrincipalGui.class.getResource("/com/foilen/lanspeedtest/desktop/swing/logo.png")));
        contentPanel.add(logoFoilen, "pushx, align right, wrap");

        // The server section
        contentPanel.add(new JLabel("Execute tests on these servers:"), "span, wrap");
        JButton startServerButton = new JButton("Start Server");
        startServerButton.setActionCommand(ACTION_START_SERVER);
        startServerButton.addActionListener(event -> {
            if (ACTION_START_SERVER.equals(event.getActionCommand())) {
                speedTestCore.startServer();
                startServerButton.setEnabled(false);
            }
        });
        contentPanel.add(startServerButton, "wrap");

        // The client section
        contentPanel.add(new JLabel("Execute tests on these servers:"), "span, wrap");

        // The client grid
        MesureButtonTableCellRendererAndEditor mesureButtonTableCellRenderer = new MesureButtonTableCellRendererAndEditor(speedTestCore);
        tableModel = new ResultsTableModel(mesureButtonTableCellRenderer);
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        LedTableCellRenderer ledTableCellRenderer = new LedTableCellRenderer();
        for (int i = 0; i < table.getColumnModel().getColumnCount(); ++i) {
            TableColumn column = table.getColumnModel().getColumn(i);
            switch (i) {
            case 0:
                column.setMinWidth(120);
                column.setPreferredWidth(120);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 1:
                column.setMinWidth(14);
                column.setPreferredWidth(14);
                column.setCellRenderer(ledTableCellRenderer);
                break;
            case 2:
                column.setMinWidth(85);
                column.setPreferredWidth(85);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 3:
                column.setMinWidth(135);
                column.setPreferredWidth(135);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 4:
                column.setMinWidth(110);
                column.setPreferredWidth(110);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 5:
                column.setMinWidth(130);
                column.setPreferredWidth(130);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 6:
                column.setMinWidth(110);
                column.setPreferredWidth(110);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 7:
                column.setMinWidth(200);
                column.setPreferredWidth(200);
                column.setCellRenderer(defaultTableCellRenderer);
                break;
            case 8:
                column.setMinWidth(110);
                column.setPreferredWidth(110);
                column.setCellRenderer(mesureButtonTableCellRenderer);
                column.setCellEditor(mesureButtonTableCellRenderer);
                break;
            }
        }

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contentPanel.add(tableScrollPane, "span, h 100%, grow, wrap");

        // The status bar
        contentPanel.add(statusText);

    }

    @Subscribe
    public void addOrUpdateServer(ServerFoundEvent event) {
        tableModel.addOrUpdate(event);
    }

    @Subscribe
    public void updateServer(ServerLostEvent event) {
        tableModel.update(event);
    }

    @Subscribe
    public void updateServer(TestCompleteEvent event) {
        statusText.setText("Completed testing " + event.getName());
        tableModel.update(event);
    }

    @Subscribe
    public void updateStatus(TestBeginEvent event) {
        statusText.setText("Starting to test testing " + event.getName() + " ...");
        tableModel.update(event);
    }

}
