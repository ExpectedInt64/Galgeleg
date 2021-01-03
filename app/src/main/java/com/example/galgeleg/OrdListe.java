package com.example.galgeleg;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;



public class OrdListe extends AppCompatActivity implements  AdapterView.OnItemClickListener {
    ArrayList<String> ordAL = new ArrayList<>();
    Handler uiThread = new Handler(Looper.getMainLooper());
    Dialog dialog = null;
    AlertDialog.Builder builder;
    ArrayAdapter adapter = null;
    Runnable opgave = null;
    SharedPreferences appSharedPrefs;
    SharedPreferences.Editor prefsEditor;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(this);
        dialog = new ProgressDialog(this);
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        prefsEditor = appSharedPrefs.edit();
        ordAL.add("Henter data...");
        adapter = new ArrayAdapter(this, R.layout.ordliste, R.id.textView3, ordAL);
        final ListView listView = new ListView(this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        AsyncUpdate asyncUpdate = new AsyncUpdate();
        asyncUpdate.execute();

        opgave = () -> {
            addLocalWord(ordAL);
            adapter = new ArrayAdapter(this, R.layout.ordliste, R.id.textView3, ordAL);
            listView.setAdapter(adapter);
            setContentView(listView);
            System.out.println("Opdaterer UI");
        };

        setContentView(listView);


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Klikket på: " + parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
        OrdListe ordListe = this;
        builder.setTitle("Valgt ord");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.select_word, (ViewGroup) findViewById(android.R.id.content).getRootView(), false);
        builder.setView(viewInflated);
        builder.setPositiveButton("Spil med ord", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent(ordListe,SpilActivity.class);
                String ordet = (String) parent.getItemAtPosition(position);
                i.putExtra("ValgtOrd", ordet);
                startActivity(i);
            }
        });
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ordListe","");
        ArrayList<String> lokalOrd = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        if(lokalOrd.contains((String)parent.getItemAtPosition(position))){
            builder.setNegativeButton("Fjern ord", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    for(int i = 0; i < lokalOrd.size();i++){
                        if(lokalOrd.get(i).equals((String)parent.getItemAtPosition(position))) {
                            System.out.println("Removed " + lokalOrd.get(i));
                            lokalOrd.remove(i);
                            for(int j = 0; j < ordAL.size(); j++){
                                if(ordAL.get(j).equals((String)parent.getItemAtPosition(position)))
                                {
                                    System.out.println("Removed " + ordAL.get(j));
                                    ordAL.remove(j);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    String json2 = gson.toJson(lokalOrd);
                    prefsEditor.putString("ordListe",json2);
                    prefsEditor.commit();
                    uiThread.post(opgave);
                }
            });
        }
        builder.show();
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
                ordAL = ordData.hentOrdListe();
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
            uiThread.post(opgave);
        }
    }

    private void addLocalWord(ArrayList<String> localWords){
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ordListe", "");
        ArrayList<String> newWords = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        if(newWords.contains(localWords)){
            for(int i = 0; i < newWords.size(); i++){
                if(!localWords.contains(newWords.get(i))) localWords.add(newWords.get(i));
            }
        } else {
            localWords.addAll(newWords);
        }
    }
}
