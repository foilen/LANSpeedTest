/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.core;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foilen.lanspeedtest.core.events.ServerFoundEvent;
import com.foilen.lanspeedtest.core.events.ServerLostEvent;
import com.foilen.lanspeedtest.core.events.TestBeginEvent;
import com.foilen.lanspeedtest.core.events.TestCompleteEvent;
import com.foilen.smalltools.tools.CloseableTools;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class SpeedTestCore {

    static private final Logger logger = LoggerFactory.getLogger(SpeedTestCore.class);

    private EventBus eventBus = new EventBus();
    private DiscoveringServers discoveringServers;
    private SpeedServer speedServer;

    // Execution queue
    private ExecutorService testExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * Queue the execution of the test, execute the test and send events. This is an async call.
     *
     * @param name
     *            the name
     * @param host
     *            the host
     * @param port
     *            the port
     */
    public void queueExecuteTest(String name, String host, int port) {

        testExecutorService.execute(() -> {

            sendEvent(new TestBeginEvent(name, host, port));

            Socket socket = null;

            try {
                socket = new Socket(host, port);
                logger.debug("Calculating download speed for {} / {}", name, host);
                double downloadSpeedMbps = CheckSpeed.download(socket);

                socket = new Socket(host, port);
                logger.debug("Calculating upload speed for {} / {}", name, host);
                double uploadSpeedMbps = CheckSpeed.upload(socket);

                sendEvent(new TestCompleteEvent(name, host, port, downloadSpeedMbps, uploadSpeedMbps, null));
            } catch (Exception e) {
                logger.error("Failed to test {} / {}", name, host, e);
                sendEvent(new TestCompleteEvent(name, host, port, null, null, e.getMessage()));
            } finally {
                CloseableTools.close(socket);
            }

        });
    }

    /**
     * Register an object with {@link Subscribe} annotations to events in com.foilen.lanspeedtest.core.events .
     *
     * @param handler
     *            the object
     */
    public void registerEventsHandler(Object handler) {
        eventBus.register(handler);
    }

    /**
     * Send an event in com.foilen.lanspeedtest.core.events .
     *
     * @param event
     *            the event
     */
    public void sendEvent(Object event) {
        logger.debug("Dispatching event: {}", event);
        eventBus.post(event);
    }

    /**
     * When you want to end the program, that stops the threads that are not daemons.
     */
    public void shutdown() {
        testExecutorService.shutdown();
    }

    /**
     * Wait for servers, send {@link ServerFoundEvent} when new ones are found and send {@link ServerLostEvent} when they disappear.
     */
    public void startDiscoveringServers() {
        discoveringServers = new DiscoveringServers(this);
        discoveringServers.setDaemon(true);
        discoveringServers.start();
    }

    /**
     * Start the server on its own thread.
     */
    public void startServer() {
        speedServer = new SpeedServer();
        speedServer.start();
    }

}
