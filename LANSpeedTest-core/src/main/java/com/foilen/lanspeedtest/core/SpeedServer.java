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
package com.foilen.lanspeedtest.core;

import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foilen.smalltools.JavaEnvironmentValues;
import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryServer;
import com.foilen.smalltools.net.services.ExecutorWrappedSocketCallback;
import com.foilen.smalltools.net.services.SocketCallback;
import com.foilen.smalltools.tools.CloseableTools;

public class SpeedServer extends Thread implements SocketCallback {

    static private final Logger logger = LoggerFactory.getLogger(SpeedServer.class);

    @Override
    public void newClient(Socket socket) {
        SocketAddress remoteName = socket.getRemoteSocketAddress();
        logger.info("Client {} connected", remoteName);

        try {
            CheckSpeed.upload(socket);
            CheckSpeed.download(socket);
        } catch (Exception e) {
            logger.error("Problem with client {}", remoteName, e);
        }

        CloseableTools.close(socket);
    }

    @Override
    public void run() {
        logger.info("Starting server");

        // Create a service
        LocalBroadcastDiscoveryServer discoveryServer = new LocalBroadcastDiscoveryServer(SpeedTestDiscoveryContants.DISCOVERY_PORT);
        DiscoverableService discoverableService = new DiscoverableService(SpeedTestDiscoveryContants.APP_NAME, //
                SpeedTestDiscoveryContants.APP_VERSION, //
                SpeedTestDiscoveryContants.SERVICE_NAME, //
                JavaEnvironmentValues.getHostName());
        discoveryServer.addTcpBroadcastService(discoverableService, new ExecutorWrappedSocketCallback(this));

    }

}
