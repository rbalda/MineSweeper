package com.minesweeper;

import android.os.Handler;
import android.widget.TextView;


/**
 * Created by ReneAlexander on 05/11/13.
 */
public class Clock implements Runnable {
    private int count;
    private final int MAX_COUNT = 999;
    private boolean isInitialized = false;
    private TextView text;
    private Handler time;

    public Clock(TextView t) {
        time = new Handler();
        count = 0;
        text = t;
        t.setText("00" + Integer.toString(count));

    }

    @Override
    public void run() {
        long currentMilliseconds = System.currentTimeMillis();
        ++count;

        if (count < 10) {
            text.setText("00" + Integer.toString(count));
        } else if (count < 100) {
            text.setText("0" + Integer.toString(count));
        } else {
            text.setText(Integer.toString(count));
        }

        time.postAtTime(this, currentMilliseconds);

        time.postDelayed(this, 1000);

        if (count >= MAX_COUNT)
            stop();
    }

    public void start() {
        if (!isInitialized) {
            count = 0;
            time.removeCallbacks(this);
            time.postDelayed(this, 1000);
            isInitialized = true;
        }
    }

    public void stop() {
        time.removeCallbacks(this);
        isInitialized = false;
    }

    public int getCount() {
        return count;
    }

    public void restart(){
        count=0;
        text.setText("00" + Integer.toString(count));
    }

    public String toString() {
        return Integer.toString(count);
    }

}
