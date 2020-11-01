package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HovedMenu extends AppCompatActivity implements View.OnClickListener {
    Button btn_select_word, btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoved_menu);
        btn_select_word = (Button) findViewById(R.id.btn_select_word);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_select_word.setOnClickListener(this);
        btn_start.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_select_word) {
            Intent i = new Intent(this, OrdListe.class);
            startActivity(i);
        }
        if (v == btn_start) {
            Intent i = new Intent(this,SpilActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Tilbage:", "onBackPressed Called");
        finishAffinity();
        finish();
    }
}