package com.example.amzz.hm14_sapkota;
// Amrita Sapkota
// L20432797
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Amzz on 8/12/2017.
 */

public class Title extends AppCompatActivity  implements View.OnClickListener {
    Button startbtn, scorebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startbtn = (Button) findViewById(R.id.btn_start);
        startbtn.setOnClickListener(this);
        scorebtn = (Button) findViewById(R.id.btn_score);
        scorebtn.setOnClickListener(this);
    }
    @Override
    public void onClick (View v) {
        if(v==scorebtn){
            Score();
        }
        else if(v==startbtn){
            StartGame();
        }

        //Start_SMS_App();
    }
    private void StartGame() {
        Intent startgame = new Intent(Title.this,MainActivity.class);
        startActivity(startgame);
    }
    private void Score() {
        Intent score = new Intent(Title.this,ScoreBoard.class);
        startActivity(score);
    }

}
