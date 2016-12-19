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
package com.foilen.lanspeedtest.desktop.cli;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private ExecutorService testExecutorService = Executors.newSingleThreadScheduledExecutor();
    private AtomicBoolean oneRan = new AtomicBoolean();
    private AtomicInteger leftToExecute = new AtomicInteger();

    public ClientCli(SpeedTestCore speedTestCore) {
        this.speedTestCore = speedTestCore;
    }

    @Subscribe
    public void newServerToTest(ServerFoundEvent event) {

        leftToExecute.incrementAndGet();
        testExecutorService.execute(() -> {
            oneRan.set(true);
            speedTestCore.executeTest(event.getName(), event.getHost(), event.getPort());
            leftToExecute.decrementAndGet();
        });

    }

    @Override
    public void run() {
        speedTestCore.registerEventsHandler(this);
        speedTestCore.startDiscoveringServers();

        while (leftToExecute.get() != 0 || !oneRan.get()) {
            ThreadTools.sleep(1000);
        }

        System.out.println("All services were tested");
        testExecutorService.shutdown();
    }

    @Subscribe
    public void testBegin(TestBeginEvent event) {
        System.out.println("Testing " + event.getName() + " (" + event.getHost() + ")");
    }

    @Subscribe
    public void testComplete(TestCompleteEvent event) {
        System.out.println("\tResults for " //
                + event.getName() + " (" + event.getHost() + ") " //
                + event.getDownloadSpeedMbps() + " / " + event.getUploadSpeedMbps() + " mbps | " //
                + event.getDownloadSpeedMbps() / 8 + " / " + event.getUploadSpeedMbps() / 8 + " mBps" //

        );
    }

}
