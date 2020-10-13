package com.example.galgeleg;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;


public class OrdListe extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ArrayList<String> ordAL = new ArrayList<>();
    Handler uiThread = new Handler(Looper.getMainLooper());
    Dialog dialog = null;

    ArrayAdapter adapter = null;
    ListView listView;
    Runnable opgave = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        ordAL.add("Henter data...");
        adapter = new ArrayAdapter(this, R.layout.ordliste, R.id.textView3, ordAL);
        final ListView listView = new ListView(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        AsyncUpdate asyncUpdate = new AsyncUpdate();
        asyncUpdate.execute();

        opgave = () -> {
            adapter = new ArrayAdapter(this, R.layout.ordliste, R.id.textView3, ordAL);
            listView.setAdapter(adapter);
            setContentView(listView);
            System.out.println("Opdaterer UI");
        };

        setContentView(listView);


    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class AsyncUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Henter data!");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            final OrdData ordData = new OrdData();
            try {
                ordAL = ordData.getOrdListe();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("Ord data hentet!");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            uiThread.postDelayed(opgave, 0);
        }
    }
}
