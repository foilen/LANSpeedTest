/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.desktop.cli;

import com.foilen.lanspeedtest.core.SpeedTestCore;

public class ServerCli implements Runnable {

    private SpeedTestCore speedTestCore;

    public ServerCli(SpeedTestCore speedTestCore) {
        this.speedTestCore = speedTestCore;
    }

    @Override
    public void run() {

        speedTestCore.startServer();

    }

}
