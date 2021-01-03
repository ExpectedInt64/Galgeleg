package com.example.galgeleg;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HovedMenu extends AppCompatActivity implements View.OnClickListener {
    Button btn_select_word, btn_start, btn_add_word;
    AlertDialog.Builder builder;
    SharedPreferences appSharedPrefs;
    SharedPreferences.Editor prefsEditor;
    ArrayList<String> ordListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoved_menu);
        btn_select_word = (Button) findViewById(R.id.btn_select_word);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_add_word = (Button) findViewById(R.id.btn_userWord);
        btn_select_word.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_add_word.setOnClickListener(this);
        builder = new AlertDialog.Builder(this);
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        prefsEditor = appSharedPrefs.edit();
        ordListe = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_select_word) {
            Intent i = new Intent(this, OrdListe.class);
            startActivity(i);
        }
        if (v == btn_start) {
            Intent i = new Intent(this, SpilActivity.class);
            startActivity(i);
        }
        if (v == btn_add_word) {
            builder.setTitle("Tilføj ord");
            View viewInflated = LayoutInflater.from(this).inflate(R.layout.add_word, (ViewGroup) findViewById(android.R.id.content).getRootView(), false);
            final EditText input = (EditText) viewInflated.findViewById(R.id.input);
            builder.setView(viewInflated);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    System.out.println(input.getText().toString());
                    addOrdTilShared(input.getText().toString());
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Tilbage:", "onBackPressed Called");
        finishAffinity();
        finish();
    }


    private void addOrdTilShared(String ord) {
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ordListe","");
        ordListe = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        if(ordListe == null){
            ordListe = new ArrayList<String>();
        }
        ordListe.add(ord);
        System.out.println(json);
        json = gson.toJson(ordListe);
        prefsEditor.putString("ordListe",json);
        prefsEditor.commit();
    }
}