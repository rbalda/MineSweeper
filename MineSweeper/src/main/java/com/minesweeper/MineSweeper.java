package com.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by ReneAlexander on 04/11/13.
 */
public class MineSweeper extends Activity {
    private Clock clock;
    public TableLayout gamePanel; // table layout to add mines to
    private Smile btnSmile;
    private boolean isPressed = false;
    private HashMap<Level, LevelData> levels;
    private boolean isStarted = false;
    private boolean isOver = true;
    private boolean isMined= false;
    private Shield shields[];
    private ImageView shieldsUI[];
    public Counter counter;
    private Intent intent;


    private AnimationDrawable animationDrawable;


    private BlockUI blocks[][]; // blocks for mine field
    private int blockDimension = 60; // width of each block
    private int blockPadding = 2; // padding between blocks

    private int nOrInGamePanel;
    private int nOcInGamePanel;
    private int totalNumberOfMines;
    private String currentLevel;
    public Dialog gameLostDialog;
    public Dialog gameFinishDialog;

    public static final String TAG = "TratamientoXML";


    //Method that initialize variables for the Game
    public void init() {
        levels = new HashMap<Level, LevelData>();
        levels.put(Level.BEGINNER, new LevelData(16, 16, 10));
        levels.put(Level.INTERMEDIATE, new LevelData(16, 16, 40));
        levels.put(Level.EXPERT, new LevelData(16, 30, 99));

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minesweeper_layout_relative);

        init();

        Bundle levelR = getIntent().getExtras();

        extractLevel(levelR.getInt("level"));

        TextView t = (TextView) findViewById(R.id.Clock);


        //shield = new Shield((ImageView) findViewById(R.id.flag));
        initShields();
        createWonDialog();
        createLoseDialog();

        //animationDrawable = (AnimationDrawable) shield.getS().getBackground();
        clock = new Clock(t);

        gamePanel = (TableLayout) findViewById(R.id.game_panel);


        btnSmile = new Smile((ImageButton) findViewById(R.id.Smile));

        btnSmile.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStarted())
                    startGame();

                //setMinesOnGamePanel(10, 4, 5);
            }
        });



    }

    public void createWonDialog(){
        gameFinishDialog = new Dialog(MineSweeper.this,R.style.Menu);
        gameFinishDialog.setTitle(getResources().getString(R.string.title_level_menu));
        gameFinishDialog.setContentView(R.layout.finishdialog);
        gameFinishDialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveXml("Jimmy",currentLevel,counter.getCount());
                restartGame();
                gameFinishDialog.dismiss();
                //readXml();
            }
        });

        WindowManager.LayoutParams lp = gameFinishDialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.screenBrightness = 1.0F;
        gameFinishDialog.getWindow().setAttributes(lp);
        gameFinishDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //setLevelButtonAction();
    }

    public void createLoseDialog(){
        gameLostDialog = new Dialog(MineSweeper.this,R.style.Menu);
        gameLostDialog.setTitle(getResources().getString(R.string.title_level_menu));
        gameLostDialog.setContentView(R.layout.losedialog);
        gameLostDialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                gameLostDialog.dismiss();
                //readXml();
            }
        });

        WindowManager.LayoutParams lp = gameFinishDialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.screenBrightness = 1.0F;
        gameLostDialog.getWindow().setAttributes(lp);
        gameLostDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //setLevelButtonAction();
    }


    private void saveXml(String name, String level, int time){
        FileOutputStream fileOut =null;

        try{
            fileOut = openFileOutput("scores.xml", MODE_PRIVATE);

        }catch (FileNotFoundException e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();

        }

        XmlSerializer serializer = Xml.newSerializer();

        try {
            serializer.setOutput(fileOut, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "jugadores");
            serializer.startTag(null, "jugador");

            serializer.startTag(null,"nombre");
            serializer.text(name);
            serializer.endTag(null, "nombre");

            serializer.startTag(null, "nivel");
            serializer.text(level);
            serializer.endTag(null, "nivel");

            serializer.startTag(null,"tiempo");
            serializer.text("" + time);
            serializer.endTag(null, "tiempo");

            serializer.endTag(null,"jugador");
            serializer.endTag(null,"jugadores");

            serializer.endDocument();
            serializer.flush();
            fileOut.close();

            Toast.makeText(getApplicationContext(), "Escrito correctamente", Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }


    }

    private void readXml() {
        FileInputStream fileIn = null;

        try{
            fileIn = openFileInput("score.xml");

        } catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(fileIn, "UTF-8");

            int event = parser.next();
            while(event != XmlPullParser.END_DOCUMENT) {
                if(event == XmlPullParser.START_TAG) {
                    Log.d(TAG, "<" + parser.getName() + ">");
                    for(int i = 0; i < parser.getAttributeCount(); i++) {
                        Log.d(TAG, "\t" + parser.getAttributeName(i) + " = " + parser.getAttributeValue(i));
                    }
                }

                if(event == XmlPullParser.TEXT && parser.getText().trim().length() != 0)
                    Log.d(TAG, "\t\t" + parser.getText());

                if(event == XmlPullParser.END_TAG)
                    Log.d(TAG, "</" + parser.getName() + ">");

                event = parser.next();
            }
            fileIn.close();

            Toast.makeText(this, "Leido correctamente", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void extractLevel(int value){
        switch (value){
            case 1:
                currentLevel="Easy";
                nOcInGamePanel=levels.get(Level.BEGINNER).getColumn();
                nOrInGamePanel=levels.get(Level.BEGINNER).getRows();
                totalNumberOfMines=levels.get(Level.BEGINNER).getMinesNumber();
                break;
            case 2:
                currentLevel="Normal";
                nOcInGamePanel=levels.get(Level.INTERMEDIATE).getColumn();
                nOrInGamePanel=levels.get(Level.INTERMEDIATE).getRows();
                totalNumberOfMines=levels.get(Level.INTERMEDIATE).getMinesNumber();
                break;
            case 3:
                currentLevel="Expert";
                nOcInGamePanel=levels.get(Level.EXPERT).getColumn();
                nOrInGamePanel=levels.get(Level.EXPERT).getRows();
                totalNumberOfMines=levels.get(Level.EXPERT).getMinesNumber();
                break;
        }
        return;
    }

    public void initShields(){
        RelativeLayout main = (RelativeLayout)findViewById(R.id.relative_layout);

        counter = new Counter((TextView)findViewById(R.id.Count));
        counter.setCount(totalNumberOfMines);
        ImageView temp= (ImageView)findViewById(R.id.flag);
        main.removeView(temp);
        shields = new Shield[totalNumberOfMines];
        shieldsUI = new ImageView[totalNumberOfMines];
        //(ImageView)findViewById(R.id.flag).get;
        for(int i=0;i<totalNumberOfMines;i++){
            shieldsUI[i]=new ImageView(this);
            shieldsUI[i].setBackground(temp.getBackground());
            shieldsUI[i].setLayoutParams(temp.getLayoutParams());
            shieldsUI[i].setScaleX((float) .45);
            shieldsUI[i].setScaleY((float) .45);
            main.addView(shieldsUI[i]);
            shields[i] = new Shield(shieldsUI[i]);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        for(int i=0;i<totalNumberOfMines;i++){
            AnimationDrawable d = (AnimationDrawable)shieldsUI[i].getBackground();
            d.start();
        }


        //animationDrawable.start();
    }

    public void startGame() {
        if(!isOver)
            return;
        isOver=false;
        isStarted=true;
        createGamePanel();
        showGamePanel();
        clock.start();
    }

    public void endGame(){
        clock.stop();
        isStarted=false;
        isOver=true;

        if(!hasWon()){
            for (int row = 0; row < nOrInGamePanel; row++) {
                for (int column = 0; column < nOcInGamePanel; column++) {
                    if(blocks[row][column].getValue()==-1){
                        blocks[row][column].setPressed(true);
                    }
                }
            }
        }
    }

    public void restartGame(){
        clock.restart();
        gamePanel.removeAllViews();
        isMined=false;
    }

    private void createGamePanel() {
        blocks = new BlockUI[nOrInGamePanel][nOcInGamePanel];

        for (int row = 0; row < nOrInGamePanel; row++) {
            for (int column = 0; column < nOcInGamePanel; column++) {
                blocks[row][column] = new BlockUI(this, row, column, this);

                blocks[row][column].setSmile(btnSmile);
                //blocks[row][column].setDefaults();



              /*  blocks[row][column].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isPressed){
                            if(blocks[cRow][cColumn].getValue()!= -1){
                                blocks[cRow][cColumn].setTextColor(Color.CYAN);
                                blocks[cRow][cColumn].setText(blocks[cRow][cColumn].toString());
                                blocks[cRow][cColumn].setBackgroundResource(R.drawable.hole);
                            }else
                                blocks[cRow][cColumn].setBackgroundResource(R.drawable.button_selected);
                            isPressed=true;
                            blocks[cRow][cColumn].setClickable(false);
                        }else if(isPressed){

                            if(blocks[cRow][cColumn].getValue()!= -1)
                                blocks[cRow][cColumn].setBackgroundResource(R.drawable.hole);
                            else
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
        for (int row = 0; row < nOrInGamePanel; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams((blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
            tableRow.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
            for (int column = 0; column < nOcInGamePanel; column++) {
                blocks[row][column].setLayoutParams(new LayoutParams(dimension, dimension));
                for (int x = row - 1; x <= row+1; x++) {
                    for (int y = column - 1; y <= column+1; y++) {
                        if (!(y == column && x == row)){
                           if(x>=0 && y>=0 && x<nOrInGamePanel &&y<nOcInGamePanel)
                                blocks[row][column].addAdjacentUI(blocks[x][y]);
                        }
                    }
                }
                //
                //blocks[row][column].setPadding(blockPadding, blockPadding, blockPadding, blockPadding);

                tableRow.addView(blocks[row][column]);
            }


            gamePanel.addView(tableRow, new TableLayout.LayoutParams(
                    (blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
        }
        //
    }

    public void setMinesOnGamePanel(int cColumn, int cRow) {
        isMined= true;
        Random rand = new Random();
        int d_mines = 0;
        while (d_mines < totalNumberOfMines) {
            int mine_c = rand.nextInt(nOcInGamePanel);
            int mine_r = rand.nextInt(nOrInGamePanel);
            if (mine_r + 1 != cRow || mine_c + 1 != cColumn) {
                if (blocks[mine_r][mine_c].getValue() >= 0) {
                    blocks[mine_r][mine_c].setValue(-1);
                    d_mines += 1;

                }
            }
        }

        for (int row = 0; row < nOrInGamePanel; row++) {

            for (int column = 0; column < nOcInGamePanel; column++) {
                if (blocks[row][column].getValue() == -1) {
                    for (int x = row - 1; x <= row + 1; x++) {
                        for (int y = column - 1; y <= column + 1; y++) {
                            if (!(y == column && x == row)){
                                if(x>=0 && y>=0 && x<nOrInGamePanel &&y<nOcInGamePanel)
                                    blocks[x][y].addAdjacent(blocks[row][column]);
                            }
                        }

                    }
                }

            }


        }

    }



    public boolean explore(BlockUI block) {

        if (block.getValue() == -1) {
            return true;
        } else {
            for (int row = 0; row < nOrInGamePanel; row++) {
                for (int col = 0; col < nOcInGamePanel; col++) {
                    blocks[row][col].setExploreState(false);
                }

            }
            exploreRec(block);
        }
        return false;
    }

    private void exploreRec(BlockUI block) {
        if (block.isExploreState()||block.isShielded())
            return;
        block.setExploreState(true);

        block.setPressed(true);

        if (block.getValue() > 0)
            return;

        int cRow = block.getBlock().getRow();
        int cCol = block.getBlock().getColumn();

        if ((cRow > 0) && (cCol > 0))
            exploreRec(blocks[cRow - 1][cCol - 1]);

        if (cRow > 0)
            exploreRec(blocks[cRow - 1][cCol]);

        if ((cRow > 0) && (cCol < nOcInGamePanel - 1))
            exploreRec(blocks[cRow - 1][cCol + 1]);

        if (cCol > 0)
            exploreRec(blocks[cRow][cCol - 1]);

        if (cCol < nOcInGamePanel - 1)
            exploreRec(blocks[cRow][cCol + 1]);

        if (cRow < nOrInGamePanel - 1)
            exploreRec(blocks[cRow + 1][cCol]);

        if ((cRow < nOrInGamePanel - 1) && (cCol < nOcInGamePanel - 1))
            exploreRec(blocks[cRow + 1][cCol + 1]);

        if ((cRow < nOrInGamePanel - 1) && (cCol > 0))
            exploreRec(blocks[cRow + 1][cCol - 1]);

    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public boolean isMined(){return isMined;}

    public void loadFonts(TextView t){
        Typeface lcdFont = Typeface.createFromAsset(getAssets(),"fonts/digital-7 (mono).ttf");
        t.setTypeface(lcdFont);
        t.setTypeface(lcdFont);
        t.setScaleX((float)1.2);
        t.setScaleY((float)1.2);

    }

    public boolean hasWon(){
        /*if(counter.getCount()>0)
            return false;
        */
        int totalMinesMarked=0;
        int totalBlocksPressed=0;

        for (int row = 0; row < nOrInGamePanel; row++) {
            for (int column = 0; column < nOcInGamePanel; column++) {
                if(blocks[row][column].getValue()==-1 && blocks[row][column].isShielded() )
                    totalMinesMarked++;

                if(blocks[row][column].getValue()>-1 && blocks[row][column].isPressed())
                    totalBlocksPressed++;
            }
        }

        if(totalMinesMarked==totalNumberOfMines && totalBlocksPressed==((nOcInGamePanel*nOrInGamePanel)-totalNumberOfMines))
            return true;
        else
            return false;
    }



}
