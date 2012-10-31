package com.idexx.stat.test;

import com.idexx.stat.StreamingMovingAverage;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/30/12
 * Time: 11:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamingMovingAverageTest {

    @Test
    public void MovingAverageTest() {
        StreamingMovingAverage sma = new StreamingMovingAverage(3);

        sma.put(2);
        sma.put(2);
        sma.put(2);

        int average = sma.calculate();
        assertEquals(2, average);

        sma.put(5);
        assertEquals(3, sma.calculate());

        sma.put(5);
        assertEquals(4, sma.calculate());

        sma.put(5);
        assertEquals(5, sma.calculate());
    }
}
