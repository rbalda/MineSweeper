package com.minesweeper;

import android.widget.TextView;

/**
 * Contains Counter GUI
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class Counter {
    /**
     * Count of flags
     */
    private int count;
    /**
     * Container of counter
     */
    private TextView counterGUI;

    /**
     * Constructor
     * @param t
     */
    public Counter(TextView t) {
        count = 0;
        counterGUI = t;
    }

    /**
     * Setter of count
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
        updateCount();
    }

    /**
     * Function to decrement the count
     */
    public void decrement(){
        count--;
        updateCount();
    }

    /**
     * Function who updates the count
     */
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

    /**
     * Getter of Count
     * @return
     */
    public int getCount() {
        return count;
    }


}
