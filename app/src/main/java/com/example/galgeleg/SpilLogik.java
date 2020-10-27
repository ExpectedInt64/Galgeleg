package com.example.galgeleg;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpilLogik {
    ArrayList<String> ord = new ArrayList<>();
    ArrayList<String> guessOrd = new ArrayList<>();

    public SpilLogik() {
    }

    public void setOrd(String s) {
        for (int i = 0; i < s.length(); i++) {
            guessOrd.add("");
            ord.add(Character.toString(s.charAt(i)));
        }
    }

    public boolean tagTur(String s) {
        if (!vundet()) {
            checkOrd(s);
            return false;
        }
        return true;
    }

    public boolean contains(String s){
        ArrayList<String> tempListe = ord;
        for(int i = 0; i < tempListe.size();i++){
            tempListe.set(i,tempListe.get(i).toLowerCase());
        }
        return tempListe.contains(s);
    }

    private void checkOrd(String s) {
        for (int i = 0; i < ord.size(); i++) {
            if (ord.get(i).toLowerCase().equals(s)) {
                guessOrd.set(i, ord.get(i));
            }
        }
        System.out.println("Gættet ord: " + guessOrd);
    }

    public boolean vundet() {
        if (guessOrd.equals(ord)) return true;
        return false;
    }
}
