package com.minesweeper;

import android.graphics.drawable.Drawable;
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
        button.setEnabled(true);
    }

    public void killed(){
        button.setBackgroundResource(R.drawable.dead_smile);
    }

    public void cooled(){
        button.setBackgroundResource(R.drawable.cool_smile);
    }

    public ImageButton getButton() {
        return this.button;
    }
}
