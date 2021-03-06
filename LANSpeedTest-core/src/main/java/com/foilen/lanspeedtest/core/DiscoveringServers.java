/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foilen.lanspeedtest.core.events.ServerFoundEvent;
import com.foilen.lanspeedtest.core.events.ServerLostEvent;
import com.foilen.smalltools.listscomparator.ListComparatorHandler;
import com.foilen.smalltools.listscomparator.ListsComparator;
import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryClient;
import com.foilen.smalltools.tools.ThreadTools;

public class DiscoveringServers extends Thread {

    static private final Logger logger = LoggerFactory.getLogger(SpeedServer.class);

    private SpeedTestCore speedTestCore;

    private List<ServerFoundEvent> lastFounds = new ArrayList<>();

    public DiscoveringServers(SpeedTestCore speedTestCore) {
        this.speedTestCore = speedTestCore;
    };

    @Override
    public void run() {

        logger.info("Waiting for new servers");

        LocalBroadcastDiscoveryClient discoveryClient = new LocalBroadcastDiscoveryClient( //
                SpeedTestContants.DISCOVERY_PORT, //
                SpeedTestContants.APP_NAME, //
                SpeedTestContants.APP_VERSION);

        for (;;) {
            ThreadTools.sleep(5000);

            List<DiscoverableService> services = discoveryClient.retrieveServicesList(SpeedTestContants.SERVICE_NAME);
            logger.debug("Found {} services", services.size());

            List<ServerFoundEvent> currentFounds = services.stream() //
                    .map(it -> {
                        return new ServerFoundEvent(it.getServiceDescription(), it.getServerHost(), it.getServerPort());
                    }) //
                    .sorted() //
                    .collect(Collectors.toList());

            ListsComparator.compareLists(currentFounds, lastFounds, new ListComparatorHandler<ServerFoundEvent, ServerFoundEvent>() {

                @Override
                public void both(ServerFoundEvent left, ServerFoundEvent right) {
                }

                @Override
                public void leftOnly(ServerFoundEvent left) {
                    // New server to add
                    speedTestCore.sendEvent(left);
                }

                @Override
                public void rightOnly(ServerFoundEvent right) {
                    // Old server to remove
                    speedTestCore.sendEvent(new ServerLostEvent(right));
                }
            });

            lastFounds = currentFounds;

        }
    }

}
