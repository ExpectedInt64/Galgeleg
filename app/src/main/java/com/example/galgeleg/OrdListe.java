package com.example.galgeleg;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;



public class OrdListe extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ArrayList<String> ordAL = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter adapter = null;
        final OrdData ordData = new OrdData();
        ordData.execute("");
        while(!ordData.getDataReady()){
        }
        ordAL = ordData.getOrdListe();
        adapter = new ArrayAdapter(this, R.layout.ordliste, R.id.textView3, ordAL);

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
