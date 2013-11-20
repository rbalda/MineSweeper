package com.minesweeper;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by ReneAlexander on 04/11/13.
 */
public class MineSweeper extends Activity {
    private Clock clock;
    private TableLayout gamePanel; // table layout to add mines to
    private Smile btnSmile;
    private boolean isPressed = false;
    private HashMap<Level, LevelData> levels;
    private boolean isStarted = false;

    private Shield shield;

    private AnimationDrawable animationDrawable;


    private BlockUI blocks[][]; // blocks for mine field
    private int blockDimension = 60; // width of each block
    private int blockPadding = 2; // padding between blocks

    private int nOrInGamePanel = 16;
    private int nOcInGamePanel = 16;
    private int totalNumberOfMines = 10;

    //Method that initialize variables for the Game
    public void init() {
        levels = new HashMap<Level, LevelData>();
        levels.put(Level.BEGINNER, new LevelData(9, 9, 10));
        levels.put(Level.INTERMEDIATE, new LevelData(16, 16, 40));
        levels.put(Level.EXPERT, new LevelData(16, 30, 99));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minesweeper_layout_relative);
        TextView t = (TextView) findViewById(R.id.Clock);
        shield = new Shield((ImageView) findViewById(R.id.flag));

        animationDrawable = (AnimationDrawable) shield.getS().getBackground();
        clock = new Clock(t);

        gamePanel = (TableLayout) findViewById(R.id.game_panel);


        btnSmile = new Smile((ImageButton) findViewById(R.id.Smile));

        btnSmile.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGamePanel();
                showGamePanel();
                setMinesOnGamePanel(10, 4, 5);
                clock.start();
                animationDrawable.start();

            }
        });


    }


    public void startGame() {

    }

    private void createGamePanel() {
        blocks = new BlockUI[nOrInGamePanel + 2][nOcInGamePanel + 2];

        for (int row = 0; row < nOrInGamePanel + 2; row++) {
            for (int column = 0; column < nOcInGamePanel + 2; column++) {
                blocks[row][column] = new BlockUI(this, row, column, this);

                blocks[row][column].setSmile(btnSmile);
                //blocks[row][column].setDefaults();

                final int cRow = row;
                final int cColumn = column;

               /* blocks[row][column].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isPressed){
                            blocks[cRow][cColumn].setBackgroundResource(R.drawable.button_selected);
                            isPressed=true;
                        }else if(isPressed){
                            blocks[cRow][cColumn].setBackgroundResource(R.drawable.button_selected);
                            isPressed=false;
                        }
                    }
                });*/
            }
        }
    }

    private void showGamePanel() {

        int dimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        for (int row = 1; row < nOrInGamePanel + 1; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams((blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
            tableRow.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            for (int column = 1; column < nOcInGamePanel + 1; column++) {
                blocks[row][column].setLayoutParams(new LayoutParams(dimension, dimension));
                //blocks[row][column].setPadding(blockPadding, blockPadding, blockPadding, blockPadding);
                tableRow.addView(blocks[row][column]);
            }


            gamePanel.addView(tableRow, new TableLayout.LayoutParams(
                    (blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
        }
    }

    public void setMinesOnGamePanel(int nMines, int cColumn, int cRow) {
        Random rand = new Random();
        int d_mines = 0;
        while (d_mines < nMines) {
            int mine_c = rand.nextInt(nOcInGamePanel);
            int mine_r = rand.nextInt(nOrInGamePanel);
            if (mine_r + 1 != cRow || mine_c + 1 != cColumn) {
                if (blocks[mine_r][mine_c].getValue() >= 0) {
                    d_mines += 1;
                    blocks[mine_r][mine_c].setValue(-1);
                }
            }
        }

        for (int row = 1; row < nOrInGamePanel + 1; row++) {

            for (int column = 1; column < nOcInGamePanel + 1; column++) {
                if (blocks[row][column].getValue() == -1) {
                    for (int x = row - 1; x <= row + 1; x++) {
                        for (int y = column - 1; y <= column + 1; y++) {
                            if (y != column && x != row)
                                blocks[x][y].addAdjacent(blocks[row][column]);
                        }

                    }
                }

            }


        }

    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
}
