package com.minesweeper;

import android.widget.TextView;

/**
 * Created by ReneAlexander on 05/11/13.
 */
public class Clock implements Runnable {
    private Thread thread;
    private int count;
    private final int MAX_COUNT = 999;
    private boolean isInitialized = false;

    public Clock() {
        thread = new Thread(this);
        count = 0;
    }

    @Override
    public void run() {
        while (count < MAX_COUNT && isInitialized) {
            try {
                ++count;
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        count = 0;
        isInitialized = false;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return Integer.toString(count);
    }

}
