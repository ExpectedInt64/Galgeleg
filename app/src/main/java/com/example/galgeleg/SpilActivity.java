package com.example.galgeleg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.gridlayout.widget.GridLayout;

import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SpilActivity extends AppCompatActivity {

    Executor bgThread = Executors.newSingleThreadExecutor();
    Handler uiThread = new Handler();
    ArrayList<Button> buttons = new ArrayList<>();
    ProgressBar progressBar;
    TextView textView;
    SpilLogik spilLogik;
    ImageView imageView;
    SharedPreferences appSharedPrefs;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private long lastShakeTime = 0;
    private ArrayList<Integer> screenSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        textView = findViewById(R.id.textView2);
        TextView attempts = findViewById(R.id.textView4);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView3);
        //Sesor https://stackoverflow.com/questions/2317428/how-to-refresh-app-upon-shaking-the-device
        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        screenSize = getScreenResolution(this);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(6);
        gridLayout.setRowCount(5);
        String sArray[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Æ", "Ø", "Å", ""};
        createButtons(sArray);
        addButtonsToGrid(gridLayout);

        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        spilLogik = new SpilLogik();
        attempts.setText("Forsøg:" + spilLogik.getGuess() + "/" + spilLogik.getMaxGuess());
        for (Button b : buttons) {
            if (b.getText() != "") {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (progressBar.getVisibility() != View.VISIBLE) {
                            if (!spilLogik.vundet() && spilLogik.getGuess() < spilLogik.getMaxGuess()) {
                                System.out.println("Trykket på knap: " + b.getText());
                                b.setClickable(false);
                                b.setEnabled(false);
                                int color = (spilLogik.contains(((String) b.getText()).toLowerCase()) ? Color.GREEN : Color.GRAY);
                                b.setBackgroundColor(color);
                                spilLogik.tagTur(b.getText().toString().toLowerCase());
                                attempts.setText("Forsøg:" + spilLogik.getGuess() + "/" + spilLogik.getMaxGuess());
                                textView.setText(spilLogik.getGuessOrd());
                                try {
                                    setImage(spilLogik.getGuess());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                if (spilLogik.vundet()) {
                                    Toast.makeText(SpilActivity.this, "Du har vundet", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SpilActivity.this, WinnerActivity.class);
                                    i.putExtra("attempts", spilLogik.getGuess() + "");
                                    i.putExtra("word", spilLogik.getOrd());
                                    startActivity(i);
                                } else if (spilLogik.getGuess() >= spilLogik.getMaxGuess()) {
                                    Toast.makeText(SpilActivity.this, "Du har tabt", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SpilActivity.this, LooserActivity.class);
                                    i.putExtra("attempts", spilLogik.getGuess() + "");
                                    i.putExtra("word", spilLogik.getOrd());
                                    startActivity(i);
                                }
                            }
                        }
                    }
                });
            }
        }

        String sessionID = getIntent().getStringExtra("ValgtOrd");
        if (sessionID != null) {
            //textView.setText(sessionID);
            Log.d("Valgt ord", sessionID);
            progressBar.setVisibility(View.GONE);
            spilLogik.setOrd(sessionID);
            textView.setText(spilLogik.getGuessOrd());
        } else {
            playWithOnlineWord();
        }
    }

    private void setImage(int img) throws IllegalAccessException {
        ArrayList<Integer> hangman = new ArrayList<>();
        Field[] fields = R.drawable.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().startsWith("hang")) hangman.add(fields[i].getInt(i));
        }
        imageView.setImageResource(hangman.get(img));
    }

    private void playWithOnlineWord() {
        bgThread.execute(() -> {
            OrdData ordData = new OrdData();
            ArrayList<String> ord = null;
            uiThread.post(() -> {
                progressBar.setVisibility(View.VISIBLE);
                //textView.setText("Henter ord!");
            });
            try {
                ord = ordData.hentOrdListe();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addLocalWord(ord);
            Random rand = new Random();
            if (ord.size() < 1) ord.add("Internet");
            String ordet = ord.get(rand.nextInt(ord.size()));
            uiThread.post(() -> {
                progressBar.setVisibility(View.GONE);
                //textView.setText(ordet);
            });
            System.out.println("Ord hentet: " + ordet);
            spilLogik.setOrd(ordet);
            uiThread.post(() -> {
                textView.setText(spilLogik.getGuessOrd());
            });
        });
    }


    private void addLocalWord(ArrayList<String> localWords) {
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ordListe", "");
        ArrayList<String> newWords = gson.fromJson(json, new TypeToken<ArrayList<String>>() {
        }.getType());
        if(newWords == null) return;
        if (newWords.contains(localWords)) {
            for (int i = 0; i < newWords.size(); i++) {
                if (!localWords.contains(newWords.get(i))) localWords.add(newWords.get(i));
            }
        } else{
            localWords.addAll(newWords);
        }
    }

    private void createButton(String s) {
        Button button = new Button(SpilActivity.this);
            if(screenSize.get(0) < 600){
                button.setMinimumWidth(60);
                button.setMinimumHeight(60);
                button.setMaxWidth(80);
                button.setMaxHeight(80);
                button.setWidth(15);
                button.setHeight(30);
                System.out.println("Screensize Small: "+ screenSize);
            }else if(screenSize.get(0) < 800 && screenSize.get(0) >=600){
                button.setMinimumWidth(80);
                button.setMinimumHeight(80);
                button.setMaxWidth(80);
                button.setMaxHeight(80);
                button.setWidth(15);
                button.setHeight(30);
                System.out.println("Screensize Medium: "+ screenSize);
            }else{
                button.setMinimumWidth(120);
                button.setMinimumHeight(120);
                button.setMaxWidth(80);
                button.setMaxHeight(80);
                System.out.println("Screensize Large: "+ screenSize);
                button.setWidth(15);
                button.setHeight(30);
            }
        button.setText(s);
        buttons.add(button);
    }

    private void createButtons(String[] s) {
        for (int i = 0; i < s.length; i++) {
            createButton(s[i]);
        }
    }

    private void addButtonsToGrid(GridLayout grid) {
        uiThread.post(() -> {
            for (Button b : buttons) {
                grid.addView(b);
            }
        });
    }

    private static ArrayList<Integer> getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        ArrayList<Integer> wh = new ArrayList<>();
        wh.add(width);
        wh.add(height);

        return wh;
    }

    //https://stackoverflow.com/questions/2317428/how-to-refresh-app-upon-shaking-the-device
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {

            if (SystemClock.elapsedRealtime() - lastShakeTime < 1000){
                return;
            }

            lastShakeTime = SystemClock.elapsedRealtime();

            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 8) {
                Toast toast = Toast.makeText(getApplicationContext(), "Telefon rystet, resetter spil", Toast.LENGTH_LONG);
                toast.show();
                recreate();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}