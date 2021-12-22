package com.jakebeecham.madassignment_sequencegame;

public class HighScore {
    int _id;
    String _name;
    String _date;
    int _score;

    public HighScore(){   }

    public HighScore(String name, int score){
        this._name = name;
        this._score = score;
    }

    public HighScore(String name, String date, int score){
        this._name = name;
        this._date = date;
        this._score = score;
    }

    public HighScore(int id, String name, String date, int score){
        this._id = id;
        this._name = name;
        this._date = date;
        this._score = score;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getDate() {
        return this._date;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public int getScore(){
        return this._score;
    }

    public void setScore(int score){
        this._score = score;
    }
}
