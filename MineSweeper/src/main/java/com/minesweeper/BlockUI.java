package com.minesweeper;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ReneAlexander on 06/11/13.
 */
public class BlockUI extends FrameLayout {
    private boolean isCovered;
    private Block block;
    private Smile smile;
    private MineSweeper mineSweeper;
    private int dimension;
    private boolean exploreState;
    private ArrayList<BlockUI> adjacentUI;


    public BlockUI(Context context, int r, int c, MineSweeper m) {
        super(context);
        Drawable d = getResources().getDrawable(R.drawable.block_states);
        setBackground(d);
        adjacentUI = new ArrayList<BlockUI>();

        //setLayoutParams(new LayoutParams(dimension,dimension));
        setPadding(-10, -10, -10, -10);
        block = new Block(r, c);
        mineSweeper = m;


        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int e = event.getAction();
                switch (e) {
                    case MotionEvent.ACTION_DOWN:
                        smile.surprising();
                        if (!mineSweeper.isStarted()) {
                            mineSweeper.setStarted(true);
                            mineSweeper.setMinesOnGamePanel(10, getBlock().getRow(), getBlock().getColumn());
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        uncover();
                        smile.normalizing();

                        break;
                }
                return true;
            }
        });

        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        View viewTemp = (View) event.getLocalState();
                        viewTemp.setScaleX((float) .5);
                        viewTemp.setScaleY((float) .5);
                        break;

                    case DragEvent.ACTION_DROP:

                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        view.setScaleX((float) .5);
                        view.setScaleY((float) .5);

                        owner.removeView(view);
                        //if(owner instanceof BlockUI)
                        addView(view);
                        break;
                }
                return true;
            }
        });


    }

    public BlockUI(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void discover() {
        isCovered = false;
        setEnabled(isCovered);
    }

    public boolean isExploreState() {
        return exploreState;
    }

    public void setExploreState(boolean exploreState) {
        this.exploreState = exploreState;
    }

    public void setValue(int v) {
        this.block.setValue(v);
    }

    public int getValue() {
        return block.getValue();
    }

    public Block getBlock() {
        return block;
    }

    public void addAdjacent(BlockUI block) {
        this.block.addAdjacent(block.getBlock());
    }

    public String toString() {
        return Integer.toString(block.getValue());
    }


    public void setSmile(Smile s) {
        smile = s;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        int imgWidth = getMeasuredWidth();
        int imgHeight = getMeasuredHeight();
        float txtWidth = paint.measureText("1");

        int x = (int) (imgWidth / 2 - txtWidth / 2);
        int y = imgHeight / 2 - 6;
        String temp = this.toString();
        if (isPressed())
            canvas.drawText(temp, x, y, paint);


    }

    public void addAdjacentUI(BlockUI b) {
        adjacentUI.add(b);
    }

    public void uncover() {
        if (this.getValue() >= 0) {
            this.setPressed(true);
            return;
        }
        else{
            for(BlockUI b:adjacentUI){
                b.uncover();
            }
            return;
        }
    }
}
