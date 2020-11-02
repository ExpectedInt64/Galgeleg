package com.example.galgeleg;

import java.util.ArrayList;

public class SpilLogik {
    private ArrayList<String> ord = new ArrayList<>();
    private ArrayList<String> guessOrd = new ArrayList<>();
    private int guess = 0;
    private int maxGuess = 6;

    public SpilLogik() {
    }

    public void setOrd(String s) {
        for (int i = 0; i < s.length(); i++) {
            guessOrd.add("_ ");
            ord.add(Character.toString(s.charAt(i)));
        }
    }

    public int getMaxGuess(){
        return maxGuess;
    }

    public void tagTur(String s) {
        if (!vundet()) {
            checkOrd(s);
            if(!contains(s)) guess++;
        }
    }

    public boolean contains(String s){
        ArrayList<String> tempListe = ord;
        for(int i = 0; i < tempListe.size();i++){
            tempListe.set(i,tempListe.get(i).toLowerCase());
        }
        return tempListe.contains(s);
    }

    public String getGuessOrd(){
        String temp = "";
        for(String s : guessOrd){
            temp += s;
        }
        return temp;
    }

    public String getOrd(){
        String temp = "";
        for(String s : ord){
            temp += s;
        }
        return temp;
    }

    private void checkOrd(String s) {
        for (int i = 0; i < ord.size(); i++) {
            if (ord.get(i).toLowerCase().equals(s)) {
                guessOrd.set(i, ord.get(i));
            }
        }
        System.out.println("Gættet ord: " + guessOrd);
    }

    public int getGuess(){
        return guess;
    }

    public boolean vundet() {
        if (guessOrd.equals(ord)) return true;
        return false;
    }
}
