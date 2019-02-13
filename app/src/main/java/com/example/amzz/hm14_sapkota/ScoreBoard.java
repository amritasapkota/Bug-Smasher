package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Amzz on 8/12/2017.
 */

public class ScoreBoard extends Activity{
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_screen);
        tv=(TextView)findViewById(R.id.scoreboard);
        String text= "HIGHEST SCORE : "+Assets.higest_socre;
        tv.setText(text);
        tv.setTextSize(30);
    }
}
