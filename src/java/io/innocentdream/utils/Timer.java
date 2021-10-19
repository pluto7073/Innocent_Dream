package io.innocentdream.utils;

public class Timer {

    private long lastValue;
    private long difference;

    public Timer() {
        lastValue = System.currentTimeMillis();
    }

    public void updateTime() {
        long timeNow = System.currentTimeMillis();
        difference = timeNow - lastValue;
        lastValue = timeNow;
    }

    public float getTimeDifference() {
        return difference / 5f;
    }

}
