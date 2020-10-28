package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        TextView attempts = findViewById(R.id.textView7);
        TextView ord = findViewById(R.id.textView10);
        attempts.setText(getIntent().getStringExtra("attempts"));
        ord.setText(getIntent().getStringExtra("word"));
    }
    @Override
    public void onBackPressed() {
        Log.d("Tilbage:", "onBackPressed Called");
        Intent i = new Intent(WinnerActivity.this,HovedMenu.class);
        startActivity(i);
    }
}