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
import java.util.List;

import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryClient;
import com.foilen.smalltools.tools.CloseableTools;
import com.foilen.smalltools.tools.ThreadTools;

public class SpeedClient extends Thread {

    @Override
    public void run() {

        // Start the discovery
        LocalBroadcastDiscoveryClient discoveryClient = new LocalBroadcastDiscoveryClient(SpeedTestDiscoveryContants.DISCOVERY_PORT);

        System.out.println("Waiting 10 secs to discover the servers");
        ThreadTools.sleep(10000);

        // Retrieve the services that were seen
        List<DiscoverableService> services = discoveryClient.retrieveServicesList(SpeedTestDiscoveryContants.SERVICE_NAME);
        System.out.println("Found " + services.size() + " services to test");

        for (DiscoverableService service : services) {

            String remoteName = service.getServerHost();

            Socket socket = service.connecToTcpService();
            System.out.println("Calculating download speed for: " + service.getServerHost());
            double downloadSpeed = CheckSpeed.download(socket);
            System.out.println("Calculating upload speed for: " + service.getServerHost());
            double uploadSpeed = CheckSpeed.upload(socket);

            System.out.println(remoteName + ": " + downloadSpeed + " / " + uploadSpeed + " mbits/sec | " + downloadSpeed / 8 + " / " + uploadSpeed / 8 + " mBytes/sec");

            CloseableTools.close(socket);

        }

    }

}
