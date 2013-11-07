package com.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ReneAlexander on 04/11/13.
 */
public class MineSweeper extends Activity {
    private Clock clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clock = new Clock();
        setContentView(R.layout.minesweeper_layout);
        TextView t = (TextView) findViewById(R.id.Clock);
        t.setText(clock.toString());
    }

    public void startGame() {

    }
}
