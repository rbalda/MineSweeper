package com.minesweeper;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by ReneAlexander on 04/11/13.
 */
public class MineSweeper extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minesweeper_layout);
    }
}
