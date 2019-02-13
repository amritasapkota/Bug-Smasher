package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.SoundPool;

/**
 * Created by Amzz on 7/29/2017.
 */

public class Assets extends Application{
    public static int life;
    public static int points;
    public static int big_points;
    public static int higest_socre=0;
    static float big_bug_start_time;
    static float gameTimer;
    static boolean game_running;
    static int current_score;
    static SoundPool soundPool;
    static int game_over;
    static int sound_getready;
    static int sound_squish1;
    static int sound_squish2;
    static int sound_squish3;
    static int squashed_sound;
    static int reached_bottom;
    static int sound_thump;
    static int bg_play;
    static Context context;
}
