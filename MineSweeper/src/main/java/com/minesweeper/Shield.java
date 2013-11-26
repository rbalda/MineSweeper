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
 * Created by ReneAlexander on 17/11/13.
 */
public class Shield {
    ImageView s;
    String msg;
    RelativeLayout.LayoutParams layoutParams;
    int oldX = 0;
    int oldY = 0;

    public Shield(ImageView s) {
        this.s = s;
        s.setScaleX((float) .5);
        s.setScaleY((float) .5);
        s.setBackgroundResource(R.drawable.shield);
        s.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int op = event.getAction();
                switch (op) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = (int) getS().getX();
                        oldY = (int) getS().getY();
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

    public ImageView getS() {
        return s;
    }
}
