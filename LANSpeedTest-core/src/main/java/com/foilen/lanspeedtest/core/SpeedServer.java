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

import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryServer;
import com.foilen.smalltools.net.services.ExecutorWrappedSocketCallback;
import com.foilen.smalltools.net.services.SocketCallback;
import com.foilen.smalltools.tools.CloseableTools;

public class SpeedServer extends Thread implements SocketCallback {

    @Override
    public void run() {
        System.out.println("Starting server");

        // Create a service
        LocalBroadcastDiscoveryServer discoveryServer = new LocalBroadcastDiscoveryServer(SpeedTestDiscoveryContants.DISCOVERY_PORT);
        DiscoverableService discoverableService = new DiscoverableService(SpeedTestDiscoveryContants.APP_NAME, //
                SpeedTestDiscoveryContants.APP_VERSION, //
                SpeedTestDiscoveryContants.SERVICE_NAME, //
                SpeedTestDiscoveryContants.SERVICE_DESCRIPTION);
        discoveryServer.addTcpBroadcastService(discoverableService, new ExecutorWrappedSocketCallback(this));

    }

    @Override
    public void newClient(Socket socket) {
        SocketAddress remoteName = socket.getRemoteSocketAddress();
        System.out.println("Client connected " + remoteName);

        double uploadSpeed = CheckSpeed.upload(socket);
        double downloadSpeed = CheckSpeed.download(socket);

        System.out.println(remoteName + ": " + downloadSpeed + " / " + uploadSpeed + " mbits/sec | " + downloadSpeed / 8 + " / " + uploadSpeed / 8 + " mBytes/sec");

        CloseableTools.close(socket);
    }

}
