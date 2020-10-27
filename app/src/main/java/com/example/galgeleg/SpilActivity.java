package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.gridlayout.widget.GridLayout;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SpilActivity extends AppCompatActivity implements View.OnClickListener {

    Executor bgThread = Executors.newSingleThreadExecutor();
    Handler uiThread = new Handler();
    ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        TextView textView = findViewById(R.id.textView2);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(6);
        gridLayout.setRowCount(5);
        String sArray[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","Æ","Ø","Å",""};
        createButtons(sArray);
        addButtonsToGrid(gridLayout);
        SpilLogik spilLogik = new SpilLogik();
        for(Button b : buttons){
            if(b.getText() != ""){
                b.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(progressBar.getVisibility() != View.VISIBLE){
                            if(!spilLogik.vundet()){
                                System.out.println("Trykket på knap: "+b.getText());
                                b.setClickable(false);
                                b.setEnabled(false);
                                int color = (spilLogik.contains(((String) b.getText()).toLowerCase())?Color.GREEN : Color.GRAY);
                                b.setBackgroundColor(color);
                                spilLogik.tagTur(b.getText().toString().toLowerCase());
                                if(spilLogik.vundet()) Toast.makeText(SpilActivity.this,"Du har vundet",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }

        String sessionID = getIntent().getStringExtra("ValgtOrd");
        if (sessionID != null) {
            textView.setText(sessionID);
            Log.d("Valgt ord", sessionID);
            progressBar.setVisibility(View.GONE);
            spilLogik.setOrd(sessionID);
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
                if (ord.size() < 1) ord.add("Internet");
                String ordet = ord.get(rand.nextInt(ord.size()));
                uiThread.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    textView.setText(ordet);
                });
                System.out.println("Ord hentet: " + ordet);
                spilLogik.setOrd(ordet);
            });
        }

    }


    private void createButton(String s){
        Button button = new Button(SpilActivity.this);
        button.setMinimumWidth(120);
        button.setMinimumHeight(120);
        button.setWidth(15);
        button.setHeight(30);
        button.setText(s);
        buttons.add(button);
    }

    private void createButtons(String[] s) {
        for(int i = 0; i < s.length; i++){
            createButton(s[i]);
        }
    }

    private void addButtonsToGrid(GridLayout grid){
        uiThread.post(() -> {
            for(Button b : buttons){
                grid.addView(b);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}