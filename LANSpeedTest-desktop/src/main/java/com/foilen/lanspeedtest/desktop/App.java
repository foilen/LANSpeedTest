/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.desktop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.foilen.lanspeedtest.core.SpeedTestCore;
import com.foilen.lanspeedtest.desktop.cli.ClientCli;
import com.foilen.lanspeedtest.desktop.cli.ServerCli;
import com.foilen.lanspeedtest.desktop.swing.PrincipalGui;
import com.foilen.smalltools.tools.LogbackTools;

public class App {

    private static void displayHelpAndExit() {
        System.out.println("Running with no arguments launches a Swing GUI");
        System.out.println("Using --server starts a server that waits for tests in a console");
        System.out.println("Using --client tests all the available servers, displays them in the console");
        System.out.println("Using --debug to enable more logging");
        System.out.println("Using --help or -h shows this help");
        System.exit(1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        SpeedTestCore speedTestCore = new SpeedTestCore();

        // Check arguments
        List<String> arguments = new ArrayList<>(Arrays.asList(args));

        boolean isDebug = arguments.remove("--debug");
        if (isDebug) {
            LogbackTools.changeConfig("/logback-debug.xml");
        } else {
            LogbackTools.changeConfig("/logback-normal.xml");
        }

        // Launch GUI
        if (arguments.isEmpty()) {
            new PrincipalGui(speedTestCore).setVisible(true);
            return;
        }

        if (arguments.size() != 1) {
            displayHelpAndExit();
            return;
        }

        switch (arguments.get(0)) {
        case "--client":
            new ClientCli(speedTestCore).run();
            break;
        case "--server":
            new ServerCli(speedTestCore).run();
            break;

        default:
            displayHelpAndExit();
            break;
        }

    }

}
