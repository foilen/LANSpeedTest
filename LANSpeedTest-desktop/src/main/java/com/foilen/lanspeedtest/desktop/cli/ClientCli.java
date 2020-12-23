/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.desktop.cli;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.foilen.lanspeedtest.core.SpeedTestCore;
import com.foilen.lanspeedtest.core.events.ServerFoundEvent;
import com.foilen.lanspeedtest.core.events.TestBeginEvent;
import com.foilen.lanspeedtest.core.events.TestCompleteEvent;
import com.foilen.smalltools.tools.ThreadTools;
import com.google.common.eventbus.Subscribe;

public class ClientCli implements Runnable {

    private SpeedTestCore speedTestCore;

    private AtomicBoolean oneRan = new AtomicBoolean();
    private AtomicInteger leftToExecute = new AtomicInteger();

    public ClientCli(SpeedTestCore speedTestCore) {
        this.speedTestCore = speedTestCore;
    }

    @Subscribe
    public void newServerToTest(ServerFoundEvent event) {

        oneRan.set(true);
        leftToExecute.incrementAndGet();
        speedTestCore.queueExecuteTest(event.getName(), event.getHost(), event.getPort());

    }

    @Override
    public void run() {
        speedTestCore.registerEventsHandler(this);
        speedTestCore.startDiscoveringServers();

        while (leftToExecute.get() != 0 || !oneRan.get()) {
            ThreadTools.sleep(1000);
        }

        System.out.println("All services were tested");
        speedTestCore.shutdown();
    }

    @Subscribe
    public void testBegin(TestBeginEvent event) {
        System.out.println("Testing " + event.getName() + " (" + event.getHost() + ")");
    }

    @Subscribe
    public void testComplete(TestCompleteEvent event) {
        leftToExecute.decrementAndGet();
        System.out.println("\tResults for " //
                + event.getName() + " (" + event.getHost() + ") " //
                + event.getDownloadSpeedMbps() + " / " + event.getUploadSpeedMbps() + " mbps | " //
                + event.getDownloadSpeedMbps() / 8 + " / " + event.getUploadSpeedMbps() / 8 + " mBps" //

        );
    }

}
