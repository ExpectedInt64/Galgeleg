package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class WinnerActivity extends AppCompatActivity {

    Animation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.victory);
        mp.start();
        TextView attempts = findViewById(R.id.textView7);
        TextView ord = findViewById(R.id.textView10);
        ImageView image = findViewById(R.id.imageView5);
        attempts.setText(getIntent().getStringExtra("attempts"));
        ord.setText("Ordet var: "+getIntent().getStringExtra("word"));

        rotateAnimation= AnimationUtils.loadAnimation(this,R.anim.rotate);
        image.startAnimation(rotateAnimation);

    }
    @Override
    public void onBackPressed() {
        Log.d("Tilbage:", "onBackPressed Called");
        Intent i = new Intent(WinnerActivity.this,HovedMenu.class);
        startActivity(i);
    }
}