package com.minesweeper;

import android.os.Handler;
import android.widget.TextView;


/**
 * Clock Class
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class Clock implements Runnable {
    /**
     * Count of time
     */
    private int count;
    /**
     * Max Count got
     */
    private final int MAX_COUNT = 999;
    /**
     * is initialized state
     */
    private boolean isInitialized = false;
    /**
     * text view container
     */
    private TextView text;
    /**
     * Handler of the time
     */
    private Handler time;

    /**
     * Constructor
     * @param t
     */
    public Clock(TextView t) {
        time = new Handler();
        count = 0;
        text = t;
        t.setText("00" + Integer.toString(count));

    }

    /**
     * Run the clock
     */
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

    /**
     * Start the count of clock
     */
    public void start() {
        if (!isInitialized) {
            count = 0;
            time.removeCallbacks(this);
            time.postDelayed(this, 1000);
            isInitialized = true;
        }
    }

    /**
     * Stop the count of clock
     */
    public void stop() {
        time.removeCallbacks(this);
        isInitialized = false;
    }

    /**
     * getter of count
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * restar the count of clock
     */
    public void restart(){
        count=0;
        text.setText("00" + Integer.toString(count));
    }

    /**
     * Function to string
     * @return
     */
    public String toString() {
        return Integer.toString(count);
    }

}
