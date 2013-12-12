package com.minesweeper;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Contains Shield GUI
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class Shield {
    /**
     * Image View container
     */
    ImageView s;

    int oldX = 0;
    int oldY = 0;

    /**
     * Constructor of shield
     * @param shield
     */
    public Shield(ImageView shield) {
        this.s = shield;
        s.setScaleX((float) .9);
        s.setScaleY((float) .9);
        s.setBackgroundResource(R.drawable.shield);
        s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int op = event.getAction();
                switch (op) {
                    case MotionEvent.ACTION_DOWN:
                        //oldX = (int) getS().getX();
                        //oldY = (int) getS().getY();
                        //getS().setScaleX((float) 0.45);
                        //getS().setScaleY((float) 0.45);
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(null, shadowBuilder, v, 0);
                        break;

                    }

                return true;
            }
        });

    }

    /**
     * Getter of s
     * @return
     */
    public ImageView getS() {
        return s;
    }
}
