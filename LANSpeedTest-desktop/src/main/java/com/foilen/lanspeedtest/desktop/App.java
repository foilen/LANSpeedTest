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
package com.foilen.lanspeedtest.desktop;

import java.io.IOException;

import com.foilen.lanspeedtest.core.SpeedTestCore;
import com.foilen.lanspeedtest.desktop.cli.ClientCli;
import com.foilen.lanspeedtest.desktop.cli.ServerCli;
import com.foilen.lanspeedtest.desktop.swing.PrincipalGui;

public class App {

    private static void displayHelpAndExit() {
        System.out.println("Running with no arguments launches a Swing GUI");
        System.out.println("Using --server starts a server that waits for tests in a console");
        System.out.println("Using --client tests all the available servers, displays them in the console");
        System.out.println("Using --help or -h shows this help");
        System.exit(1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        SpeedTestCore speedTestCore = new SpeedTestCore();

        // Launch GUI
        if (args.length == 0) {
            new PrincipalGui(speedTestCore).setVisible(true);
            return;
        }

        if (args.length != 1) {
            displayHelpAndExit();
            return;
        }

        switch (args[0]) {
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
