package com.minesweeper;

import android.widget.ImageButton;

/**
 * Created by ReneAlexander on 16/11/13.
 */
public class Smile {
    ImageButton button;

    public Smile(ImageButton b) {
        this.button = b;
    }

    public void surprising() {
        button.setPressed(true);
    }

    public void normalizing() {
        button.setPressed(false);
    }

    public ImageButton getButton() {
        return this.button;
    }
}
