package com.minesweeper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by ReneAlexander on 06/11/13.
 */
public class BlockUI extends Button {
    private boolean isCovered;
    private Block block;

    public BlockUI(Context context) {
        super(context);
        Drawable d = getResources().getDrawable(R.drawable.block_states);
        setBackground(d);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabled(false);
                setPressed(true);
            }
        });
    }

    public BlockUI(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*public void setDefaults()
    {
        isCovered = true;

        this.setBackgroundResource(R.drawable.button_states);

    }*/

    public void discover() {
        isCovered = false;
        setEnabled(isCovered);
    }






}
