package com.minesweeper;

/**
 * Created by Jimmy on 11/12/13.
 */
import android.content.Context;
import android.media.MediaPlayer;


public class PermanentAudio {
    /**
     * Instance of media player
     */
    private MediaPlayer mediaPlayer;
    /**
     * is Playing state
     */
    private boolean isPlaying = false;
    /**
     * in Loop state
     */
    private boolean inLoop =false;

    /**
     * Constructor
     * @param ctx
     * @param resID
     */
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

    /**
     * Play the sound
     */
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

    /**
     * Stop the sound
     */
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

    /**
     * Repeat the sound
     */
    public synchronized void loop(){
        inLoop =true;
        isPlaying =true;
        mediaPlayer.start();
    }

    /**
     * Release the sound
     */
    public void release(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }



}