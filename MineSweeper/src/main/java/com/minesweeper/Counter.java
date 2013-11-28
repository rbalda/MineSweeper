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
        updateCount();
    }

    public void decrement(){
        count--;
        updateCount();
    }

    private void updateCount(){
        if (count < 10) {
            counterGUI.setText("00" + Integer.toString(count));
        } else if (count < 100) {
            counterGUI.setText("0" + Integer.toString(count));
        } else {
            counterGUI.setText(Integer.toString(count));
        }
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
