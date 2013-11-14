package com.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by ReneAlexander on 04/11/13.
 */
public class MineSweeper extends Activity {
    private Clock clock;
    private TableLayout gamePanel; // table layout to add mines to
    private ImageButton btnSmile;
    private boolean isPressed = false;

    private BlockUI blocks[][]; // blocks for mine field
    private int blockDimension = 24; // width of each block
    private int blockPadding = 2; // padding between blocks



    private int nOrInGamePanel = 19;
    private int nOcInGamePanel = 19;
    private int totalNumberOfMines = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.minesweeper_layout);
        TextView t = (TextView) findViewById(R.id.Clock);
        clock = new Clock(t);

        gamePanel = (TableLayout)findViewById(R.id.game_panel);


        btnSmile = (ImageButton) findViewById(R.id.Smile);

        btnSmile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createGamePanel();
                showGamePanel();
                clock.start();
                setMinesOnGamePanel(10,4,5);
            }
        });


    }

    public void startGame() {

    }

    private void createGamePanel()
    {
        blocks = new BlockUI[nOrInGamePanel + 2][nOcInGamePanel + 2];

        for (int row = 0; row < nOrInGamePanel + 2; row++)
        {
            for (int column = 0; column < nOcInGamePanel + 2; column++)
            {
                blocks[row][column] = new BlockUI(this);
                blocks[row][column].setDefaults(row,column);

                final int cRow = row;
                final int cColumn = column;

                blocks[row][column].setOnClickListener(new View.OnClickListener() {
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
                });
            }
        }
    }

    private void showGamePanel()
    {
        for (int row = 1; row < nOrInGamePanel + 1; row++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams((blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
            tableRow.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            for (int column = 1; column < nOcInGamePanel + 1; column++)
            {
                blocks[row][column].setLayoutParams(new LayoutParams(26,26));
                blocks[row][column].setPadding(blockPadding, blockPadding, blockPadding, blockPadding);
                tableRow.addView(blocks[row][column]);
            }


            gamePanel.addView(tableRow, new TableLayout.LayoutParams(
                    (blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
         
        }

    }

    private void setMinesOnGamePanel(int nMines, int cColumn, int cRow){
        Random rand = new Random();
        int d_mines = 0;
        while (d_mines < nMines){
            int mine_c = rand.nextInt(nOcInGamePanel);
            int mine_r = rand.nextInt(nOrInGamePanel);
            if(mine_r+1 != cRow || mine_c+1!=cColumn){
                if (blocks[mine_r][mine_c].getValue() >= 0){
                    d_mines += 1;
                    blocks[mine_r][mine_c].setValue(-1);
                }
            }
        }
    }

}
