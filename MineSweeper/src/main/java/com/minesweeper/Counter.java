package com.minesweeper;

import android.widget.TextView;

/**
 * Created by ReneAlexander on 13/11/13.
 */
public class Counter {
    private int count;
    private TextView counterGUI;

    public Counter(TextView t) {
        count = 0;
        counterGUI = t;
    }

    public void setCount(int count) {
        this.count = count;
    }

  /*  @Override
    public void update() {
        if (count > 0) {
            count--;
            counterGUI.setText(Integer.toString(count));
        }
    }*/

    public int getCount() {
        return count;
    }
}
