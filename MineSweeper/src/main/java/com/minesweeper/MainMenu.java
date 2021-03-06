package com.minesweeper;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;


import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Contains Main Menu's Graphical User Interface
 *  ---Main Activity---
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class MainMenu extends Activity {
    /**
     *
     */
    private Typeface font;
    /**
     * First Dialog (start)
     */
    private Dialog startDialog;
    /**
     * Select Levels Dialog
     */
    private Dialog levelDialog;
    /**
     * Scores Dialog
     */
    private Dialog scoreDialog;
    /**
     * About Dialog
     */
    private Dialog aboutDialog;
    /**
     * User Data Source from db
     */
    private UserDataSource source;
    /**
     * List View to Show the Easy Mode Scores
     */
    private ListView scoreListEasy;
    /**
     * List View to Show the Normal Mode Scores
     */
    private ListView scoreListNormal;
    /**
     * List View to Show the Expert Mode Scores
     */
    private ListView scoreListExpert;
    /**
     * Array to contains all the expert scores
     */
    private ArrayList<User> experts;
    /**
     * Array to contains all the normal scores
     */
    private ArrayList<User> normal;
    /**
     * Array to contains all the easy scores
     */
    private ArrayList<User> easy;
    /**
     * Instance of permanentAudio class to play the background sound
     */
    private PermanentAudio backgroundSound;

    /**
     * Creates the android elements and instance it for Main Menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);
        source = new UserDataSource(this);
        source.open();
        init();


        backgroundSound= getPermanentAudio(R.raw.indiana);
        backgroundSound.play();

        createScoreDialog();
        createInitialDialog();
        createLevelDialog();
        createAboutDialog();
        startDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        levelDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        scoreDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

    }

    /**
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        startDialog.show();
        findViewById(R.id.title).setScaleX((float)1.2);
        findViewById(R.id.title).setScaleY((float)1.2);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.title_animation);
        findViewById(R.id.title).startAnimation(anim);
        loadLevels(source.getAllUsers());

    }

    /**
     * Method that initialize the arrays of users for scores
     */
    public void init(){
        experts = new ArrayList<User>();
        normal = new ArrayList<User>();
        easy = new ArrayList<User>();
    }

    /**
     * gets the permanent Audio
     * @param id
     * @return permanentAudio
     */
    private PermanentAudio getPermanentAudio(int id) {

        return new PermanentAudio(getBaseContext(),id);
    }

    /**
     * creates the select levels dialog and set layouts, contents and listeners
     */
    public void createLevelDialog(){
        levelDialog = new Dialog(MainMenu.this,R.style.Menu);
        levelDialog.setTitle(getResources().getString(R.string.title_level_menu));
        levelDialog.setContentView(R.layout.menulevellayout);
        loadFont((TextView) levelDialog.findViewById(R.id.easy));
        loadFont((TextView)levelDialog.findViewById(R.id.normal));
        loadFont((TextView)levelDialog.findViewById(R.id.expert));
        loadFont((TextView) levelDialog.findViewById(R.id.back));
        setLevelButtonAction();
    }

    /**
     * creates the Initial dialog(Menu) and set layouts, contents and listeners
     */
    public void createInitialDialog(){
        startDialog = new Dialog(MainMenu.this,R.style.Menu);
        startDialog.setContentView(R.layout.menudialoglayout);
        loadFont((TextView) startDialog.findViewById(R.id.high_scores));
        loadFont((TextView)startDialog.findViewById(R.id.exit));
        loadFont((TextView)startDialog.findViewById(R.id.start_game));
        loadFont((TextView) startDialog.findViewById(R.id.about));
        setInitialButtonAction();
    }

    /**
     * creates the score dialog and set layouts, contents and listeners
     */
    public void createScoreDialog(){
        scoreDialog = new Dialog(MainMenu.this,R.style.Menu);
        scoreDialog.setTitle(getResources().getString(R.string.title_level_menu));
        scoreDialog.setContentView(R.layout.scoredialog);
        scoreListEasy=(ListView) scoreDialog.findViewById(R.id.scoreViewEasy);
        scoreListNormal=(ListView) scoreDialog.findViewById(R.id.scoreViewNormal);
        scoreListExpert=(ListView) scoreDialog.findViewById(R.id.scoreViewExpert);
        setListToListView(scoreListEasy,easy);
        setListToListView(scoreListNormal,normal);
        setListToListView(scoreListExpert,experts);
        loadFont((TextView)scoreDialog.findViewById(R.id.cat_expert));
        loadFont((TextView)scoreDialog.findViewById(R.id.cat_normal));
        loadFont((TextView)scoreDialog.findViewById(R.id.cat_easy));

        scoreDialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreDialog.dismiss();
                startDialog.show();
            }
        });

    }

    /**
     * Sets the on click listeners for every button of the initial dialog
     */
    public void setInitialButtonAction(){
        startDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenu.this.finish();
            }
        });
        startDialog.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
                aboutDialog.show();
            }
        });
        startDialog.findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
                levelDialog.show();
            }
        });
        startDialog.findViewById(R.id.high_scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog.dismiss();
                scoreDialog.show();
            }
        });
    }

    /**
     * Sets the on click listeners for every button of the select level dialog
     */
    public void setLevelButtonAction(){
        levelDialog.findViewById(R.id.easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainMenu.this,MineSweeper.class);
                int1.putExtra("level",1);

                startActivity(int1);
                levelDialog.dismiss();

            }
        });
        levelDialog.findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainMenu.this,MineSweeper.class);
                int1.putExtra("level",2);

                startActivity(int1);
                levelDialog.dismiss();
            }
        });
        levelDialog.findViewById(R.id.expert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainMenu.this,MineSweeper.class);
                int1.putExtra("level",3);

                startActivity(int1);
                levelDialog.dismiss();
            }
        });
        levelDialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelDialog.dismiss();
                startDialog.show();
            }
        });

    }

    /**
     * Load the custom text font of our app
     * @param t
     */
    public void loadFont(TextView t){
        TextView text=t;
        font = Typeface.createFromAsset(getAssets(),"fonts/ARMY RUST.ttf");
        text.setTypeface(font);



    }

    /**
     * Loads from db all the Users with score and send it to the different levels arrays and sort them too
     * @param l
     */
    public void loadLevels(List<User> l){
        normal.removeAll(normal);
        experts.removeAll(experts);
        easy.removeAll(easy);

        for(User u: l){
            if(u.getLevel().equals("Normal"))
                normal.add(u);
            else if(u.getLevel().equals("Expert"))
                experts.add(u);
            else
                easy.add(u);
        }

        Collections.sort(easy);
        Collections.sort(normal);
        Collections.sort(experts);



    }

    /**
     * Function that receive a list to put it into the list view using an ArrayAdapter
     * @param v
     * @param list
     */
    public void setListToListView(ListView v,List<User> list){

        ArrayAdapter<User> adapter=new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1,android.R.id.text1,list);
        v.setAdapter(adapter);

    }

    /**
     * creates the about dialog and set layouts, contents and listeners
     */
    public void createAboutDialog(){
        aboutDialog = new Dialog(MainMenu.this,R.style.Menu);
        aboutDialog.setContentView(R.layout.about_layout);
        loadFont((TextView)aboutDialog.findViewById(R.id.Title));
        loadFont((TextView)aboutDialog.findViewById(R.id.content));
        loadFont((TextView)aboutDialog.findViewById(R.id.jimmy));
        loadFont((TextView)aboutDialog.findViewById(R.id.rene));
        loadFont((TextView)aboutDialog.findViewById(R.id.accept));

        aboutDialog.findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.dismiss();
                startDialog.show();
            }
        });
        //Adding params to increment the behind screen brightness
        WindowManager.LayoutParams lp = aboutDialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        lp.screenBrightness = 1.0F;
        aboutDialog.getWindow().setAttributes(lp);
        aboutDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    /**
     * Finish the app including the background sound
     */
    @Override
    public void finish() {
        backgroundSound.stop();
        super.finish();

    }
}
