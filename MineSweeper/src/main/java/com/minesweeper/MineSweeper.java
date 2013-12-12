package com.minesweeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
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

import javax.sql.DataSource;

/**
 * Contains MineSweeper's Graphical User Interface and Game Logic
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class MineSweeper extends Activity {
    /**
     * Clock timer
     */
    private Clock clock;
    /**
     * Table layout to add the blocks
     */
    public TableLayout gamePanel;
    /**
     * Instance of Smile button
      */
    private Smile btnSmile;
    /**
     * Hash Map who contains the information of levels
     */
    private HashMap<Level, LevelData> levels;
    /**
     * The game is Started
     */
    private boolean isStarted = false;
    /**
     * The game is Over
     */
    private boolean isOver = true;
    /**
     * The game is Mined already
     */
    private boolean isMined= false;
    /**
     * Array that contains the shields
     */
    private Shield shields[];
    /**
     * Array that contains the shieldsUI
     */
    private ImageView shieldsUI[];
    /**
     * Counter of FLAGs
     */
    public Counter counter;
    /**
     * Layout Params with the shields location
     */
    private ViewGroup.LayoutParams locationOfShields;
    /**
     *  Click Sound player
     */
    private MediaPlayer clickSound;
    /**
     * Mine Pressed Sound player
     */
    private MediaPlayer mineSound;
    /**
     * Position to init the sounds
     */
    private int posActual=0;
    /**
     * Array of BlocksUI for game panel
     */
    private BlockUI blocks[][];
    /**
     * Block Dimension
     */
    private int blockDimension = 60;
    /**
     * Block Padding
     */
    private int blockPadding = 2;
    /**
     * Number of Rows in the Game Panel
     */
    private int nOrInGamePanel;
    /**
     * Number of Columns in the Game Panel
     */
    private int nOcInGamePanel;
    /**
     * Total Numbers of Mines to put in Game Panel
     */
    private int totalNumberOfMines;
    /**
     * Current Level Selected
     */
    private String currentLevel;
    /**
     * Dialog when the game is Lost
     */
    public Dialog gameLostDialog;
    /**
     * Dialog when the game is Won
     */
    public Dialog gameFinishDialog;
    /**
     * Use Data Source Instance
     */
    private UserDataSource dataSource;


    /**
     * Method that initialize variables for the Game
     */
    public void init() {
        levels = new HashMap<Level, LevelData>();
        levels.put(Level.BEGINNER, new LevelData(16, 16, 10));
        levels.put(Level.INTERMEDIATE, new LevelData(16, 16, 10));
        levels.put(Level.EXPERT, new LevelData(16, 30, 10));

        dataSource = new UserDataSource(this);
        dataSource.open();

    }

    /**
     * Creates the android elements and instance it for Minesweeper
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minesweeper_layout_relative);

        init();

        Bundle levelR = getIntent().getExtras();

        extractLevel(levelR.getInt("level"));

        TextView t = (TextView) findViewById(R.id.Clock);

        initShields();
        createWonDialog();
        createLoseDialog();

        clock = new Clock(t);

        gamePanel = (TableLayout) findViewById(R.id.game_panel);

        clickSound=new MediaPlayer();
        clickSound=MediaPlayer.create(getApplicationContext(), R.raw.dig);

        mineSound=new MediaPlayer();
        mineSound=MediaPlayer.create(getApplicationContext(), R.raw.mine);



        btnSmile = new Smile((ImageButton) findViewById(R.id.Smile));

        btnSmile.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStarted())
                    startGame();

            }
        });
    }

    /**
     * creates the won dialog and set layouts, contents and listeners
     */
    public void createWonDialog(){
        gameFinishDialog = new Dialog(MineSweeper.this,R.style.Menu);
        gameFinishDialog.setTitle(getResources().getString(R.string.title_level_menu));
        gameFinishDialog.setContentView(R.layout.finishdialog);
        loadFont((TextView)gameFinishDialog.findViewById(R.id.youWin));
        gameFinishDialog.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = ((EditText)gameFinishDialog.findViewById(R.id.nameText)).getText().toString();
                User u = dataSource.createUser(s,clock.getCount(),currentLevel);
                restartGame();
                gameFinishDialog.dismiss();


            }
        });
        gameFinishDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = ((EditText)gameFinishDialog.findViewById(R.id.nameText)).getText().toString();
                User u = dataSource.createUser(s,clock.getCount(),currentLevel);
                gameFinishDialog.dismiss();
                dataSource.close();

                finish();
            }
        });
        //Adding params to increment the behind screen brightness
        WindowManager.LayoutParams lp = gameFinishDialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.screenBrightness = 1.0F;
        gameFinishDialog.getWindow().setAttributes(lp);
        gameFinishDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    /**
     * Start the the block clicked sound
     */
    public void playClick(){
        clickSound.seekTo(posActual);
        clickSound.start();
    }

    /**
     * Start the Mine exploding sound
     */
    public void playMine(){
        mineSound.seekTo(posActual);
        mineSound.start();
    }

    /**
     * creates the lose dialog and set layouts, contents and listeners
     */
    public void createLoseDialog(){
        gameLostDialog = new Dialog(MineSweeper.this,R.style.Menu);
        gameLostDialog.setContentView(R.layout.losedialog);
        loadFont((TextView)gameLostDialog.findViewById(R.id.youLose));
        gameLostDialog.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                gameLostDialog.dismiss();

            }
        });
        gameLostDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameLostDialog.dismiss();

                finish();
            }
        });
        //Adding params to increment the behind screen brightness
        WindowManager.LayoutParams lp = gameFinishDialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.screenBrightness = 1.0F;
        gameLostDialog.getWindow().setAttributes(lp);
        gameLostDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }


    /**
     * Set the values of the game constants and save the current level string
     * @param value from the value sent with intent information from the last activity(MainMenu)
     */
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

    /**
     * Initiates the Shield logic and UI information to put it in the game
     */
    public void initShields(){
        RelativeLayout main = (RelativeLayout)findViewById(R.id.relative_layout);

        counter = new Counter((TextView)findViewById(R.id.Count));
        counter.setCount(totalNumberOfMines);
        ImageView temp= (ImageView)findViewById(R.id.flag);
        main.removeView(temp);
        locationOfShields = temp.getLayoutParams();
        shields = new Shield[totalNumberOfMines];
        shieldsUI = new ImageView[totalNumberOfMines];
        //(ImageView)findViewById(R.id.flag).get;
        for(int i=0;i<totalNumberOfMines;i++){
            shieldsUI[i]=new ImageView(this);
            shieldsUI[i].setBackground(temp.getBackground());
            shieldsUI[i].setLayoutParams(locationOfShields);
            //shieldsUI[i].setScaleX((float) .75);
           // shieldsUI[i].setScaleY((float) .75);
            main.addView(shieldsUI[i]);
            shields[i] = new Shield(shieldsUI[i]);
        }
    }

    /**
     * Restarts the number of Shields of the panel if the game is restarted
     */
    public void restartShields(){
        counter.setCount(totalNumberOfMines);
        RelativeLayout main = (RelativeLayout)findViewById(R.id.relative_layout);
        for(int i=0;i<totalNumberOfMines;i++){
            ViewGroup parent = (ViewGroup)shieldsUI[i].getParent();
            parent.removeView(shieldsUI[i]);
            shieldsUI[i].setScaleX((float) .9);
            shieldsUI[i].setScaleY((float) .9);
            shieldsUI[i].setLayoutParams(locationOfShields);
            main.addView(shieldsUI[i]);

        }
    }

    /**
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        for(int i=0;i<totalNumberOfMines;i++){
            AnimationDrawable d = (AnimationDrawable)shieldsUI[i].getBackground();
            d.start();
        }


        //animationDrawable.start();
    }

    /**
     * Sets the variables of control of the game when starts
     */
    public void startGame() {
        if(!isOver)
            return;
        isOver=false;
        isStarted=true;
        createGamePanel();
        showGamePanel();
        clock.start();
    }

    /**
     * Sets the variables of control of the game when is finish and discover the mines if there is no winner on the game
     */
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


        return;
    }

    /**
     * Restarts all the variables of control and shields to the original state
     */
    public void restartGame(){
        btnSmile.getButton().setBackgroundResource(R.drawable.smile_states);
        clock.restart();
        gamePanel.removeAllViews();
        isMined=false;
        restartShields();
        return;
    }

    /**
     * Creates the Physical game panel using the class BlockUI
     */
    private void createGamePanel() {
        blocks = new BlockUI[nOrInGamePanel][nOcInGamePanel];

        for (int row = 0; row < nOrInGamePanel; row++) {
            for (int column = 0; column < nOcInGamePanel; column++) {
                blocks[row][column] = new BlockUI(this, row, column, this);

                blocks[row][column].setSmile(btnSmile);

            }
        }
    }

    /**{
     * Creates the GUI of the game panel to show the blocks and add all the adjacent blocks of every block
     */
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


                tableRow.addView(blocks[row][column]);
            }


            gamePanel.addView(tableRow, new TableLayout.LayoutParams(
                    (blockDimension + 2 * blockPadding) * nOcInGamePanel, blockDimension + 2 * blockPadding));
        }
        //
    }

    /**
     * Set the Mines on the game panel avoiding the column and row received from param and init the block value of each block
     * @param cColumn
     * @param cRow
     */
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
        //For every mine put is added to the adjacent list of all the blocks around
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


    /**
     * Explores all the blocks setting the exploreState to false and using the recursive function exploreRec.. to discover the blocks without mines or values around that block
     * @param block pressed
     */
    public void explore(BlockUI block) {


        for (int row = 0; row < nOrInGamePanel; row++) {
            for (int col = 0; col < nOcInGamePanel; col++) {
                blocks[row][col].setExploreState(false);
            }

        }
        exploreRec(block);

    }

    /**
     * Recursive function that explores the blocks and pressed it the recursive method is sending to the adjacent blocks avoiding blocks that are not in the panel
     * @param block
     */
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

    /**
     * The Game is Over?
     * @return boolean
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Set the Over state
     * @param isOver
     */
    public void setOver(boolean isOver) {
        this.isOver = isOver;
    }

    /**
     * The game hasStarted?
     * @return boolean
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Set the Started state
     * @param isStarted
     */
    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    /**
     * The Game is mined?
     * @return boolean
     */
    public boolean isMined(){return isMined;}

    /**
     * Load the custom text font of our app
     * @param t
     */
    public void loadFonts(TextView t){
        Typeface lcdFont = Typeface.createFromAsset(getAssets(),"fonts/digital-7 (mono).ttf");
        t.setTypeface(lcdFont);
        t.setTypeface(lcdFont);
        t.setScaleX((float)1.2);
        t.setScaleY((float)1.2);

    }

    /**
     * Function that verifies if there is a winner counting the mines marked blocks pressed and compare it with the constants using operations
     * @return boolean
     */
    public boolean hasWon(){

        if(counter.getCount()>0)
            return false;

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

    /**
     * Gets the clock timer
     * @return
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Load the custom text font of our app
     * @param t
     */
    public void loadFont(TextView t){
        Typeface font;
        TextView text=t;
        font = Typeface.createFromAsset(getAssets(),"fonts/ARMY RUST.ttf");
        text.setTypeface(font); }


}
