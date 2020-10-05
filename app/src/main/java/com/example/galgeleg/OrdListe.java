package com.example.galgeleg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class OrdListe extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /*Handler uiThread = new Handler(Looper.getMainLooper());
    Runnable opgave;
    ArrayList<String> ordAL = new ArrayList<String>();*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter adapter;
        OrdData ordData = new OrdData();
        ordData.execute("");
        while(!ordData.getDataReady()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String[] ord = new String[ordData.getOrdListe().size()];
        for(int i = 0; i < ordData.getOrdListe().size();i++){
            ord[i] = ordData.getOrdListe().get(i);
        }

        adapter = new ArrayAdapter(this,R.layout.ordliste,R.id.textView3, ord);


        ListView listView = new ListView(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        setContentView(listView);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
