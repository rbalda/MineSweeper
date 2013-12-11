package com.minesweeper;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    private final int colours[]= {Color.MAGENTA,Color.CYAN, Color.YELLOW,Color.WHITE,Color.GREEN,Color.RED,Color.LTGRAY,Color.MAGENTA};
    private boolean isShielded = false;
    private Vibrator vb;


    public BlockUI(Context context, int r, int c, MineSweeper m) {
        super(context);
        Drawable d = getResources().getDrawable(R.drawable.block_states);
        setBackground(d);
        adjacentUI = new ArrayList<BlockUI>();
        vb = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);



        //setLayoutParams(new LayoutParams(dimension,dimension));
        //setPadding(-10, -10, -10, -10);
        block = new Block(r, c);
        mineSweeper = m;



        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int e = event.getAction();
                BlockUI f = (BlockUI)v;
                switch (e) {
                    case MotionEvent.ACTION_DOWN:

                        smile.surprising();
                        if (!mineSweeper.isMined()) {
                             mineSweeper.setMinesOnGamePanel(getBlock().getRow(), getBlock().getColumn());
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if(getValue()>-1){
                            mineSweeper.playClick();
                            mineSweeper.explore(f);
                        }
                        else{
                            mineSweeper.playMine();
                            setPressed(true);
                            mineSweeper.endGame();
                            vb.vibrate(500);
                            smile.killed();
                            mineSweeper.gameLostDialog.show();
                        }
                        if(mineSweeper.hasWon()){

                            mineSweeper.getClock().stop();
                            mineSweeper.setStarted(false);
                            mineSweeper.setOver(true);
                            //mineSweeper.endGame();
                            mineSweeper.gameFinishDialog.show();

                        }
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
                        ViewGroup ownerTemp = (ViewGroup) viewTemp.getParent();


                        if (ownerTemp instanceof BlockUI)
                            ((BlockUI) ownerTemp).isShielded = false;

                        break;

                    case DragEvent.ACTION_DROP:
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();

                        view.setScaleX((float) .75);
                        view.setScaleY((float) .75);


                        if (!isPressed()) {
                            owner.removeView(view);
                            addView(view);
                            if (owner instanceof RelativeLayout)
                                mineSweeper.counter.decrement();
                        } else {

                            if (!(owner instanceof RelativeLayout))
                                owner.addView(view);

                        }
                        isShielded = true;
                        if (mineSweeper.hasWon())
                            mineSweeper.gameFinishDialog.show();
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
        if(!(this.getValue()==-1))
            this.block.addAdjacent(block.getBlock());
        else
            return;
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
        //paint.setStrokeWidth(1);

        paint.setTextSize(30);
        int imgWidth = getMeasuredWidth();
        int imgHeight = getMeasuredHeight();
        float txtWidth = paint.measureText("1");
        int x=0,y=0;
        if(getBlock().getValue()>0){
            x = (int) (imgWidth / 2 - txtWidth / 2);
            y = imgHeight/2 + (int)(txtWidth/1.5);

            paint.setColor(colours[getBlock().getValue()]);
            String temp = this.toString();
                if (isPressed())
                canvas.drawText(temp, x, y, paint);
        }
        else if(getBlock().getValue()==-1){
            x = (int)(imgWidth/5);


            Bitmap b = BitmapFactory.decodeResource(this.getResources(),R.drawable.minesweeperlogo);
            b = b.createScaledBitmap(b,25,25,false);

            if(isPressed())
            canvas.drawBitmap(b,x,x,paint);
        }


    }

    public void addAdjacentUI(BlockUI b) {
        adjacentUI.add(b);
    }


    public void uncover() {
        if(this.getValue()==-1)
            return;
        if (this.getValue() > 0) {
            this.setPressed(true);
            return;
        }
        if(this.getValue()==0){
            this.setPressed(true);
            for(BlockUI b:adjacentUI){
                b.uncover();
                return;
            }

        }


    }

    public boolean isShielded(){
        return isShielded;
    }
}
