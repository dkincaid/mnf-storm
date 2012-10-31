package com.idexx.stat;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/30/12
 * Time: 11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamingMovingAverage {
    private int[] holder;
    private int index = 0;

    public StreamingMovingAverage(int windowSize) {
        holder = new int[windowSize];
    }

    public void put(int value) {
        holder[index % holder.length] = value;
        index++;
    }

    public int calculate() {
        int sum = 0;
        for (int aHolder : holder) {
            sum += aHolder;
        }

        return sum / holder.length;
    }
}
