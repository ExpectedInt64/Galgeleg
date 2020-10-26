package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SpilActivity extends AppCompatActivity {

    Executor bgThread = Executors.newSingleThreadExecutor();
    Handler uiThread = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        TextView textView = findViewById(R.id.textView2);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        //gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnCount(5);
        gridLayout.setRowCount(5);
        createButtons(gridLayout);
        String sessionID = getIntent().getStringExtra("ValgtOrd");
        if (sessionID != null) {
            textView.setText(sessionID);
            Log.d("Valgt ord", sessionID);
            progressBar.setVisibility(View.GONE);
        } else {
            bgThread.execute(() -> {
                OrdData ordData = new OrdData();
                ArrayList<String> ord = null;
                uiThread.post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setText("Henter ord!");
                });
                try {
                    ord = ordData.hentOrdListe();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Random rand = new Random();
                String ordet = ord.get(rand.nextInt(ord.size()));
                uiThread.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    textView.setText(ordet);
                });
                System.out.println("Ord hentet: " + ordet);
            });
        }
    }

    private void createButtons(GridLayout gridLayout) {
        uiThread.post(()->{
            for (char c = 'A'; c <= 'Z'; c++) {
                Button button = new Button(SpilActivity.this);
                button.setMinimumWidth(120);
                button.setMinimumHeight(120);
                button.setWidth(15);
                button.setHeight(30);
                button.setText(c+"");
                gridLayout.addView(button);
            }
            //Todo make for loop for adding æøå
            Button button_ae = new Button(SpilActivity.this);
            Button button_oe = new Button(SpilActivity.this);
            Button button_aa = new Button(SpilActivity.this);
            button_ae.setMinimumWidth(120);
            button_ae.setMinimumHeight(120);
            button_ae.setWidth(15);
            button_ae.setHeight(30);
            button_oe.setMinimumWidth(120);
            button_oe.setMinimumHeight(120);
            button_oe.setWidth(15);
            button_oe.setHeight(30);
        });
    }
}