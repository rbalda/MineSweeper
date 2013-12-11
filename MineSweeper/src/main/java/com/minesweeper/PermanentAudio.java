package com.minesweeper;

/**
 * Created by Jimmy on 11/12/13.
 */
import android.content.Context;
import android.media.MediaPlayer;


public class PermanentAudio {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean inLoop =false;

    public PermanentAudio(Context ctx, int resID)
    {
        mediaPlayer= MediaPlayer.create(ctx, resID);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying =false;
                if(inLoop)
                {
                    mp.start();
                }

            }
        });
        mediaPlayer.start();

    }

    public synchronized void play()
    {
        if(isPlaying)
            return;
        if(mediaPlayer!=null)
        {
            isPlaying =true;
            mediaPlayer.start();
        }
    }
    public synchronized void stop()
    {
        try{
            inLoop =false;
            if(isPlaying)
            {
                isPlaying =false;
                mediaPlayer.pause();
            }
        }catch (Exception e){
            System.err.println("Error");
        }

    }
    public synchronized void loop(){
        inLoop =true;
        isPlaying =true;
        mediaPlayer.start();
    }
    public void release(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }



}