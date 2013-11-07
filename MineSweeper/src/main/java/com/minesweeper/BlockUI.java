package com.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by ReneAlexander on 06/11/13.
 */
public class BlockUI extends Button {
    private boolean isCovered;

    public BlockUI(Context context) {
        super(context);
        isCovered = true;
    }

    public void discover() {
        isCovered = false;
        setEnabled(isCovered);
    }


}
