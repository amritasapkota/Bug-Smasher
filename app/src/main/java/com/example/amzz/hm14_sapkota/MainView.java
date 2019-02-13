package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797


/**
 * Created by Amzz on 7/29/2017.
 */

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder = null;
    int x, y;
    private MainThread t = null;
    Context context;
    // Constructor
    public MainView (Context context) {
        super(context);

        this.context = context;
        // Init variables
        x = y = 0;
        // Retrieve the SurfaceHolder instance associated with this SurfaceView.
        holder = getHolder();

        // Load the sound effects
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Assets.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            Assets.soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();

        }
        Assets.big_bug_start_time = System.nanoTime() / 1000000000f;
        Assets.gameTimer = System.nanoTime() / 1000000000f;
        Assets.game_running=false;
        Assets.big_bug_start_time=20;
        Assets.life = 3;
        Assets.big_points=10;
        Assets.points=1;
        Assets.current_score=0;
        Assets.game_over = Assets.soundPool.load(context, R.raw.game_over, 1);
        Assets.sound_getready = Assets.soundPool.load(context, R.raw.get_ready, 1);
        Assets.sound_squish1 = Assets.soundPool.load(context, R.raw.squash_1, 1);
        Assets.sound_squish2 = Assets.soundPool.load(context, R.raw.squash_2, 1);
        Assets.sound_squish3 = Assets.soundPool.load(context, R.raw.squash_3, 1);
        Assets.bg_play= Assets.soundPool.load(context, R.raw.bg_sound, 1);
        Assets.squashed_sound= Assets.soundPool.load(context, R.raw.squash_1, 1);
        Assets.sound_thump = Assets.soundPool.load(context, R.raw.thumped_sound, 1);
        Assets.reached_bottom = Assets.soundPool.load(context, R.raw.reach_bottom, 1);

        Assets.soundPool.play(Assets.sound_getready,1,1,1,0,1);


        // Specify this class (MainView) as the class that implements the three callback methods required by SurfaceHolder.Callback.
        holder.addCallback(this);
    }

    public void pause ()
    {
        t.setRunning(false);
        while (true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume ()
    {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x, y;
        int action = event.getAction();
        x = event.getX();
        y = event.getY();
//		if (action==MotionEvent.ACTION_MOVE) {
//		}
//		if (action==MotionEvent.ACTION_DOWN){
//		}
        if (action == MotionEvent.ACTION_DOWN) {
            if (t != null){
                t.setXY ((int)x, (int)y);
                t.update();
            }

        }

        return true; // to indicate we have handled this event
    }

    @Override
    public void surfaceCreated (SurfaceHolder holder) {
        // Create and start a drawing thread whose Runnable object is defined by this class (MainView)
        if (t == null) {
            t = new MainThread(holder,context);
            t.setRunning(true);
            t.start();
            setFocusable(true); // make sure we get events
        }
    }
    // Neither of these two methods are used in this example, however, their definitions are required because SurfaceHolder.Callback was implemented
    @Override public void surfaceChanged(SurfaceHolder sh, int f, int w, int h) {}
    @Override public void surfaceDestroyed(SurfaceHolder sh) {}
}
