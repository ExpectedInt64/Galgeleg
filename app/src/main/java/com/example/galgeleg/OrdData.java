package com.example.galgeleg;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class OrdData extends AsyncTask<String,Void,String> {
    private ArrayList<String> ord = new ArrayList<>();
    private boolean dataReady;

    public OrdData(){
        dataReady = false;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            loadOrdFraSheets();
            dataReady = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String loadURL(String url) throws IOException {
        System.out.println("Henter data fra " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }

    private void loadOrdFraSheets() throws IOException{
        String sheetsID = "1EDCCIGeC7MS0Egm4bx3clXymTu5FcGxe_SDigTD4sBs";
        String data = loadURL("https://docs.google.com/spreadsheets/d/" + sheetsID + "/export?format=csv&id=" + sheetsID);
        ord.clear();

        for(String linje : data.split("\n")){
            String[] felter = linje.split(",",-1);
            String ordet = felter[0].trim();
            ord.add(ordet);
        }
        System.out.println(ord.toString());
    }

    public ArrayList<String> getOrdListe(){
        if(!ord.isEmpty()){
            return ord;
        } else return null;
    }
    public boolean getDataReady(){
        return dataReady;
    }
    public void setDataReady(boolean state){
        this.dataReady=state;
    }
}