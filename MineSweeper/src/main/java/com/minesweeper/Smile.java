package com.minesweeper;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

/**
 * Contains Smile states info
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class Smile {
    /**
     * Image Button container of smile
     */
    ImageButton button;

    /**
     * Constructor
     * @param b
     */
    public Smile(ImageButton b) {
        this.button = b;
    }

    /**
     * function who set into pressed state
     */
    public void surprising() {
        button.setPressed(true);
    }

    /**
     * function who set into unpressed state
     */
    public void normalizing() {
        button.setPressed(false);
        button.setEnabled(true);
    }

    /**
     * function that set into killed state
     */
    public void killed(){
        button.setBackgroundResource(R.drawable.dead_smile);
    }

    /**
     * function that set into cooled state
     */
    public void cooled(){
        button.setBackgroundResource(R.drawable.cool_smile);
    }

    /**
     * getter of button
     * @return
     */
    public ImageButton getButton() {
        return this.button;
    }
}
