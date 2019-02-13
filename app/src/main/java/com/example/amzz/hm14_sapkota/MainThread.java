package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797


/**
 * Created by Amzz on 7/29/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import java.util.Random;


public class MainThread extends Thread {
    private SurfaceHolder holder;
    private boolean isRunning = false;
    int x1, y1,x2,y2,bx,by,tX,tY;
    private static final Object lock = new Object();
    private Handler handler;
    boolean initialized;
    Bitmap background;
    Bitmap bug1, bug2,lifeImage, bugDead,superBug1,superBug2,superBugDead;
    boolean touched;
    boolean bug_dead1,bug_dead2,super_bug_flag=false;
    float bug_radius,super_bug_radius;
    SoundPool sp;
    int bg_play,super_bug_hit=0, dead_sound1,dead_sound2,dead_sound3,super_dead_sound, missed_sound;
    Context context;
    Canvas canvas=null;
    Random r =null;


    public MainThread (SurfaceHolder surfaceHolder,Context context) {
        holder = surfaceHolder;
        handler = new Handler();
        x1=x2 = y1=y2=bx=by = 0;
        r = new Random();
        initialized = false;
        this.context = context;
        touched = false;
        bug_dead1=bug_dead2 = false;
        super_bug_flag=true;
        int ranInt = r.nextInt(500 - 0) + 0;
        x1=ranInt;
        ranInt = r.nextInt(500 - 0) + 0;
        x2=ranInt;
        ranInt = r.nextInt(500 - 0) + 0;
        bx=ranInt;

        //loading sound resources
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        } else {
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            sp = new SoundPool.Builder().setAudioAttributes(attributes).build();
        }

 bg_play=sp.load(this.context,R.raw.bg_sound,1);
        dead_sound1 = sp.load(this.context,R.raw.squashed_sound,1);
        dead_sound2 = sp.load(this.context,R.raw.squash_1,1);
        dead_sound3 = sp.load(this.context,R.raw.squashed_sound,1);
        super_dead_sound = sp.load(this.context,R.raw.squash_3,1);
        missed_sound = sp.load(this.context,R.raw.thumped_sound,1);
    }


    public void setRunning(boolean b) {
        isRunning = b;
    }
    public void setXY (int x, int y) {
        synchronized (lock) {
                this.tX = x;
                this.tY = y;
                touched = true;
        }
    }

    @Override
    public void run() {
        while(isRunning) {
            // Lock the canvas before drawing
            if (holder != null) {
                 this.canvas = holder.lockCanvas();
                if (canvas != null) {
                    // Perform drawing operations on the canvas
                    if(Assets.game_running)
                    {
                        if(Assets.life==0){
                            endThisGame();
                        }
                        render(canvas);


                    }else {
                        float currentTime = System.nanoTime() / 1000000000f;
                        if (currentTime - Assets.gameTimer >= 3){
                        Assets.game_running=true;
                            Assets.soundPool.play(Assets.bg_play,1,1,1,0,1);
                        }
                    }
                    // After drawing, unlock the canvas and display it
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void loadGraphics(Canvas canvas) {
        if (initialized == false) {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach1_250);
            int newWidth = (int) (canvas.getWidth() * 0.2f);
            int newBWidth = (int) (canvas.getWidth() * 0.25f);

            bug_radius = newWidth * 1f;
            super_bug_radius = newWidth * 1f;
            float scaleFactor = (float) newWidth / bitmap.getWidth();
            float scaleBFactor = (float) newBWidth / bitmap.getWidth();

            // What was the scaling factor to get to this?
            // Compute the new height
            int newHeight = (int) (bitmap.getHeight() * scaleFactor);
            int newBHeight = (int) (bitmap.getHeight() * scaleBFactor);

            // Scale it to a new size
            bug1 = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            superBug1 = Bitmap.createScaledBitmap(bitmap, newBWidth, newBHeight, false);

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach2_250);
            bug2 = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            superBug2 = Bitmap.createScaledBitmap(bitmap, newBWidth, newBHeight, false);

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach3_250);
            bugDead = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            superBugDead = Bitmap.createScaledBitmap(bitmap, newBWidth, newBHeight, false);

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_image);
            newWidth = canvas.getWidth();
            newHeight = canvas.getHeight();
            background = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.life_icon);
            lifeImage = Bitmap.createScaledBitmap(bitmap, 50, 50, false);
            restartRoch(1);
            restartRoch(2);
            restartRoch(3);
            Assets.current_score=0;
//            Assets.soundPool.play()
            initialized = true;

        }
    }

    private void render (Canvas canvas) {
        int i,lx,ly, xx1,xx2,yy1, yy2,xx3,yy3,whigh=canvas.getHeight()-50;
        // Fill the entire canvas' bitmap with 'black'
//        canvas.drawColor(Color.BLACK);
        // Instantiate a Paint object
        loadGraphics(canvas);
        canvas.drawBitmap(background,0,0,null);
//        Paint paint = new Paint();
        // Set the paint color to 'white'
//        paint.setColor(Color.WHITE);
        // Draw a white circle at position (100, 100) with a radius of 100
        int lifHigh = 50;
        int spacing = 8; // spacing in between circles

        int r1,r2,r3;
            r1=r.nextInt(6)-3;
            r2=r.nextInt(6)-3;
            r3=r.nextInt(6)-3;


        synchronized (lock) {
            x1 = (x1 + r1) % (canvas.getWidth())+1;
            y1 = (y1 + 2);
            xx1 = x1;
            yy1 = y1;
            x2 = (x2 + r2) % (canvas.getWidth())+1;
            y2 = (y2 + 3);
            xx2 = x2;
            yy2 = y2;

            bx = (bx + r3) % canvas.getWidth();
            by = (by + 4);
            xx3 = bx;
            yy3 = by;
            lx = canvas.getWidth()- lifHigh-spacing;	// coordinates for rightmost circle to draw
            ly =spacing;

            if(!super_bug_flag){
            restartRoch(3);
            }
        }

        for (i=0; i<Assets.life; i++) {
            canvas.drawBitmap(lifeImage, lx, ly, null);
            // Reposition to draw the next circle to the left
            lx -= (lifHigh + spacing);
        }
        checkBigBug();

        if(yy1>whigh){
            Assets.life--;
            restartRoch(1);
            Assets.soundPool.play(Assets.reached_bottom, 1, 1, 1, 0, 1);
        }else if (yy2>whigh){
            restartRoch(2);
            Assets.soundPool.play(Assets.reached_bottom, 1, 1, 1, 0, 1);
            Assets.life--;
        }else if (yy3>whigh){
            Assets.life--;
            restartRoch(3);
            Assets.big_bug_start_time = System.nanoTime() / 1000000000f;
            Assets.soundPool.play(Assets.reached_bottom, 1, 1, 1, 0, 1);
            super_bug_flag=false;
            super_bug_hit=0;
        }

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        String scoreText="SCORE: "+Assets.current_score;
        canvas.drawText(scoreText, spacing, 35, paint);
        long curTime = System.currentTimeMillis() / 100 % 10;
        if (!bug_dead1) {
            curTime = System.currentTimeMillis() / 100 % 10;
            if (curTime % 2 == 0)
                canvas.drawBitmap(bug1, xx1, yy1, null);
            else
                canvas.drawBitmap(bug2, xx1, yy1, null);
        } else {
            Assets.current_score=Assets.current_score+Assets.points;
            restartRoch(1);
            canvas.drawBitmap(bugDead, tX, tY, null);
            bug_dead1=false;
        }
        if (!bug_dead2) {
            curTime = System.currentTimeMillis() / 100 % 10;
            if (curTime % 2 == 0)
                canvas.drawBitmap(bug1, xx2, yy2, null);
            else
                canvas.drawBitmap(bug2, xx2, yy2, null);
        } else {
            Assets.current_score=Assets.current_score+Assets.points;
            restartRoch(2);
            canvas.drawBitmap(bugDead, tX, tY, null);
            bug_dead2=false;
        }
        if(super_bug_flag){
            if (super_bug_hit<=3) {
                if (curTime % 2 == 0)
                    canvas.drawBitmap(superBug1, xx3, yy3, null);
                else
                    canvas.drawBitmap(superBug2, xx3, yy3, null);
            } else {
                Assets.current_score=Assets.current_score+Assets.big_points;
                Assets.big_bug_start_time = System.nanoTime() / 1000000000f;
                canvas.drawBitmap(superBugDead, tX, tY, null);
                super_bug_flag=false;
                super_bug_hit=0;
            }
        }
    }
    public void update(){
        if (touched) {
            if (CircleTouch(x1,y1,bug_radius)) {
                killed();
                bug_dead1 = true;
            } else {
                Assets.soundPool.play(Assets.sound_thump, 1, 1, 1, 0, 1);
            }
            if (CircleTouch(x2,y2,bug_radius)) {
                killed();
                bug_dead2 = true;
            } else {
                Assets.soundPool.play(Assets.sound_thump, 1, 1, 1, 0, 1);
            }
            if(super_bug_flag){
                if (CircleTouch(bx,by,super_bug_radius)) {
                    if(super_bug_hit>3){
                        super_killed();
                    }else{
                        super_bug_hit++;
                    }
                } else {
                    Assets.soundPool.play(Assets.sound_thump, 1, 1, 1, 0, 1);
                }
            }
            touched = false;
        }
    }
    private boolean CircleTouch(int x,int y,float bug_radius){
        double dis = Math.sqrt (((tX - x) * (tX - x)) + ((tY - y) * (tY - y)));
        if(dis <= bug_radius)
            return true;
        else
            return false;
    }

    public void super_killed(){
        Assets.soundPool.play(Assets.squashed_sound, 1, 1, 1, 0, 1);
    }
    private void killed(){
        int i = r.nextInt(6) + 3;
        switch (i){
            case 1:
                    sp.play(dead_sound1,1,1,1,0,1);
                break;
            case 2:
                    sp.play(dead_sound2,1,1,1,0,1);
                break;
            case 3:
                sp.play(dead_sound1,1,1,1,0,1);
                break;
        }
    }
    private void restartRoch(int i){
        int ranInt = r.nextInt(500 - 0) + 0;
        switch (i){
            case 1:
                x1=ranInt;
                y1=0;
                break;
            case 2:
                x2=ranInt;
                y2=0;
                break;
            case 3:
                bx=ranInt;
                by=0;
                break;
        }
    }
    private void checkBigBug(){
        float currentTime = System.nanoTime() / 1000000000f;
        if (!super_bug_flag && (currentTime - Assets.big_bug_start_time) >= 20){
            super_bug_flag=true;
            super_bug_hit=0;
            restartRoch(3);
        }
    }

    private void endThisGame(){
        if(Assets.current_score>Assets.higest_socre){
            Assets.higest_socre=Assets.current_score;
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, "New High Score:"+Assets.current_score, Toast.LENGTH_LONG).show();
                    Assets.soundPool.play(Assets.game_over,1,1,1,0,1);
                }
            });
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT)
                            .show();
                  //  Assets.soundPool.play(Assets.game_over, 1, 1, 1, 0, 1);
                }
            });
        }
        isRunning=false;
        MainThread.interrupted();
    }

}
