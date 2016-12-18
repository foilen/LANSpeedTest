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
package com.foilen.lanspeedtest;

import java.net.Socket;
import java.util.Date;

import com.foilen.smalltools.exception.SmallToolsException;
import com.foilen.smalltools.tools.AssertTools;
import com.foilen.smalltools.tools.StreamsTools;

public class CheckSpeed {

    private static final int BUFFER_SIZE = 1000; // 1kB
    private static final int LOOP_COUNT = 200 * 1000 * 1000 / BUFFER_SIZE; // 200 mB in total
    private static final byte[] BUFFER;

    static {
        BUFFER = new byte[BUFFER_SIZE];
    }

    protected static double calculateSpeed(long startTime, long endTime) {
        double seconds = ((endTime - startTime) / 1000.0);
        double mbBySec = LOOP_COUNT * (BUFFER_SIZE + 4); // Total in B with headers
        mbBySec /= 1000 * 1000;// Total got in mB
        mbBySec *= 8;// Total got in mb
        return mbBySec / seconds;
    }

    public static double download(Socket socket) {

        try {

            long startTime = new Date().getTime();

            for (int i = 0; i < LOOP_COUNT; ++i) {
                byte[] bytes = StreamsTools.readBytes(socket.getInputStream());
                AssertTools.assertTrue(bytes.length == BUFFER_SIZE, "Buffer size is not of the right size. Expected " + BUFFER_SIZE + " actual " + bytes.length);
            }

            long endTime = new Date().getTime();
            return calculateSpeed(startTime, endTime);
        } catch (Exception e) {
            throw new SmallToolsException("Problem reading the bytes", e);
        }

    }

    public static double upload(Socket socket) {

        try {

            long startTime = new Date().getTime();

            for (int i = 0; i < LOOP_COUNT; ++i) {
                StreamsTools.write(socket.getOutputStream(), BUFFER);
            }

            long endTime = new Date().getTime();
            return calculateSpeed(startTime, endTime);
        } catch (Exception e) {
            throw new SmallToolsException("Problem writing the bytes", e);
        }

    }

}
