package com.jakebeecham.madassignment_sequencegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sequenceGame";
    private static final String TABLE_HIGH_SCORES = "high_scores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_SCORE = "score";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase myDb) {
        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE " + TABLE_HIGH_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_DATE + " TEXT NOT NULL,"
                + KEY_SCORE + " INTEGER NOT NULL"
                + ")";
        myDb.execSQL(CREATE_HIGHSCORES_TABLE);
    }

    //Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase myDb, int oldVersion, int newVersion) {
        // Drop older table if exists
        myDb.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES);

        // Create Tables Again
        onCreate(myDb);
    }

    /*
     * CRUD Helper Methods
     */

    //Add New HighScore
    void addAHighScore(HighScore highScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highScore.getName()); //HighScore Name
        values.put(KEY_DATE, highScore.getDate()); //Date HighScore Made
        values.put(KEY_SCORE, highScore.getScore()); //Score

        //Inserting Row
        db.insert(TABLE_HIGH_SCORES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); //Closing database connection
    }

    //Update a single HighScore
    public int updateAHighScore(HighScore highScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highScore.getName());
        values.put(KEY_DATE, highScore.getDate());
        values.put(KEY_SCORE, highScore.getScore());

        //Updating Row
        return db.update(TABLE_HIGH_SCORES, values,
                KEY_ID + " = ?",
                new String[]{ String.valueOf(highScore.getID()) });
    }

    //Deleting a single HighScore
    public void deleteAHighScore(HighScore highScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIGH_SCORES, KEY_ID + " = ?",
                new String[]{ String.valueOf(highScore.getID()) });
        db.close();
    }

    //Get a single HighScore
    private HighScore getAHighScore(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HIGH_SCORES, new String[] {
                        KEY_ID,
                        KEY_NAME,
                        KEY_DATE,
                        KEY_SCORE },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HighScore highScore = new HighScore(Integer.parseInt(
                cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3));
        //Return HighScore
        return highScore;
    }

    //Get all HighScores in a list view
    public List<HighScore> getAllHighScores()
    {
        List<HighScore> highScoresList = new ArrayList<HighScore>();
        //Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_HIGH_SCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.setID(Integer.parseInt(cursor.getString(0)));
                highScore.setName(cursor.getString(1));
                highScore.setDate(cursor.getString(2));
                highScore.setScore(cursor.getInt(3));
                //Adding HighScore to list
                highScoresList.add(highScore);
            } while (cursor.moveToNext());
        }

        //Return HighScores list
        return highScoresList;
    }

    //Get 5 HighScores in a list view
    public List<HighScore> getTop5HighScores()
    {
        List<HighScore> highScoresList = new ArrayList<HighScore>();
        //Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_HIGH_SCORES
                + " ORDER BY SCORE DESC "
                + " LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.setID(Integer.parseInt(cursor.getString(0)));
                highScore.setName(cursor.getString(1));
                highScore.setDate(cursor.getString(2));
                highScore.setScore(cursor.getInt(3));
                //Adding HighScore to list
                highScoresList.add(highScore);
            } while (cursor.moveToNext());
        }

        //Return HighScore list
        return highScoresList;
    }

    //Empty Database (Drop Table)
    public void emptyHighScores()
    {
        //Drop older table if exists
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGH_SCORES);

        // Create tables again
        onCreate(db);
    }
}
