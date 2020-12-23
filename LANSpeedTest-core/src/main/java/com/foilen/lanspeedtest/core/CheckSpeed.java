/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.core;

import java.net.Socket;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.foilen.smalltools.exception.SmallToolsException;
import com.foilen.smalltools.tools.DateTools;
import com.foilen.smalltools.tools.StreamsTools;

public class CheckSpeed {

    public static final int BUFFER_SIZE = 1000; // 1kB
    private static final byte[] BUFFER;

    static {
        BUFFER = new byte[BUFFER_SIZE];
    }

    protected static double calculateSpeedInMb(long startTime, long endTime, long amountOfBytes) {
        double seconds = ((endTime - startTime) / 1000.0);
        double mBytesTransfered = amountOfBytes / 1000000;
        return mBytesTransfered / seconds * 8;
    }

    public static double download(Socket socket) {

        try {

            // Send the mode
            StreamsTools.write(socket.getOutputStream(), SpeedTestContants.SERVER_SEND_DATA);

            // Download for 5 seconds
            Date now = new Date();
            long startTime = now.getTime();
            long maxTime = DateTools.addDate(now, Calendar.SECOND, 5).getTime();

            long amountOfBytes = 0;
            long endTime;
            while ((endTime = new Date().getTime()) < maxTime) {
                // Get some data
                amountOfBytes += socket.getInputStream().read(BUFFER);
            }

            return calculateSpeedInMb(startTime, endTime, amountOfBytes);
        } catch (Exception e) {
            throw new SmallToolsException("Problem reading the bytes", e);
        }

    }

    public static double upload(Socket socket) {

        try {

            // Send the mode
            StreamsTools.write(socket.getOutputStream(), SpeedTestContants.SERVER_RECEIVE_DATA);
            Arrays.fill(BUFFER, (byte) 0);

            // Upload for 5 seconds
            Date now = new Date();
            long startTime = now.getTime();
            long maxTime = DateTools.addDate(now, Calendar.SECOND, 5).getTime();

            long amountOfBytes = 0;
            long endTime;
            while ((endTime = new Date().getTime()) < maxTime) {
                // Send some data
                socket.getOutputStream().write(BUFFER);
                amountOfBytes += BUFFER_SIZE;
            }

            return calculateSpeedInMb(startTime, endTime, amountOfBytes);
        } catch (Exception e) {
            throw new SmallToolsException("Problem writing the bytes", e);
        }

    }

}
