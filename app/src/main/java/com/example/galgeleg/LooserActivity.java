package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looser);
        TextView attempts = findViewById(R.id.textView7);
        TextView ord = findViewById(R.id.textView10);
        attempts.setText(getIntent().getStringExtra("attempts"));
        ord.setText("Ordet var: "+getIntent().getStringExtra("word"));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LooserActivity.this,HovedMenu.class);
        Log.d("Tilbage:", "onBackPressed Called");
        startActivity(i);
    }
}