/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.core;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foilen.smalltools.JavaEnvironmentValues;
import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryServer;
import com.foilen.smalltools.net.services.ExecutorWrappedSocketCallback;
import com.foilen.smalltools.net.services.SocketCallback;
import com.foilen.smalltools.tools.CloseableTools;
import com.foilen.smalltools.tools.StreamsTools;

public class SpeedServer extends Thread implements SocketCallback {

    static private final Logger logger = LoggerFactory.getLogger(SpeedServer.class);

    @Override
    public void newClient(Socket socket) {
        SocketAddress remoteName = socket.getRemoteSocketAddress();
        logger.info("Client {} connected", remoteName);

        try {
            byte[] buffer = new byte[CheckSpeed.BUFFER_SIZE];

            // Get mode and execute it
            int mode = StreamsTools.readInt(socket.getInputStream());
            switch (mode) {
            case SpeedTestContants.SERVER_SEND_DATA:
                logger.info("Client {} sending it data", remoteName);
                Arrays.fill(buffer, (byte) 0);
                for (;;) {
                    socket.getOutputStream().write(buffer);
                }
            case SpeedTestContants.SERVER_RECEIVE_DATA:
                logger.info("Client {} receiving data from it", remoteName);
                for (;;) {
                    socket.getInputStream().read(buffer);
                }
            }
        } catch (Exception e) {
            // Simply disconnecting
        }

        logger.info("Client {} disconnected", remoteName);

        CloseableTools.close(socket);
    }

    @Override
    public void run() {
        logger.info("Starting server");

        // Create a service
        LocalBroadcastDiscoveryServer discoveryServer = new LocalBroadcastDiscoveryServer(SpeedTestContants.DISCOVERY_PORT);
        DiscoverableService discoverableService = new DiscoverableService(SpeedTestContants.APP_NAME, //
                SpeedTestContants.APP_VERSION, //
                SpeedTestContants.SERVICE_NAME, //
                JavaEnvironmentValues.getHostName());
        discoveryServer.addTcpBroadcastService(discoverableService, new ExecutorWrappedSocketCallback(this));

    }

}
