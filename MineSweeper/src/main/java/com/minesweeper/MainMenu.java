package com.minesweeper;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by ReneAlexander on 07/12/13.
 */
public class MainMenu extends Activity {
    private Bundle bundle;
    private Typeface font;
    private Dialog startDialog;
    private Dialog levelDialog;
    private UserDataSource source;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);
        source = new UserDataSource(this);
        source.open();
        User u = source.createUser("hello",3,"normal");

        createInitialDialog();
        createLevelDialog();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        startDialog.show();
        AnimationDrawable a = (AnimationDrawable)(findViewById(R.id.local_layout)).getBackground();
        a.start();
    }

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

    public void createInitialDialog(){
        startDialog = new Dialog(MainMenu.this,R.style.Menu);
        startDialog.setContentView(R.layout.menudialoglayout);
        loadFont((TextView) startDialog.findViewById(R.id.high_scores));
        loadFont((TextView)startDialog.findViewById(R.id.exit));
        loadFont((TextView)startDialog.findViewById(R.id.start_game));
        loadFont((TextView) startDialog.findViewById(R.id.about));
        setInitialButtonAction();
    }

    public void setInitialButtonAction(){
        startDialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenu.this.finish();
            }
        });
        startDialog.findViewById(R.id.start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
                levelDialog.show();
            }
        });
    }

    public void setLevelButtonAction(){
        levelDialog.findViewById(R.id.easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainMenu.this,MineSweeper.class);
                int1.putExtra("level",1);
                startActivity(int1);

            }
        });
        levelDialog.findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainMenu.this,MineSweeper.class);
                int1.putExtra("level",2);
                startActivity(int1);
            }
        });
        levelDialog.findViewById(R.id.expert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainMenu.this,MineSweeper.class);
                int1.putExtra("level",3);
                startActivity(int1);
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






    public void loadFont(TextView t){
        TextView text=t;
        font = Typeface.createFromAsset(getAssets(),"fonts/ARMY RUST.ttf");
        text.setTypeface(font);
        text.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                    Animation anim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.button_animation);
                    v.startAnimation(anim);

                }
                return true;
            }
        });

    }


}
