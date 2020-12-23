/*
    LANSpeedTest
    https://github.com/foilen/LANSpeedTest
    Copyright (c) 2016-2020 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.lanspeedtest.core;

import org.junit.Assert;
import org.junit.Test;

public class CheckSpeedTest {

    @Test
    public void testCalculateSpeed() {

        // 321 mBits
        long amountOfBytes = 320 * 1000 * 1000;

        Assert.assertEquals(2560, CheckSpeed.calculateSpeedInMb(0, 1000, amountOfBytes), 0.1);
        Assert.assertEquals(1280, CheckSpeed.calculateSpeedInMb(0, 2000, amountOfBytes), 0.1);
    }

}
