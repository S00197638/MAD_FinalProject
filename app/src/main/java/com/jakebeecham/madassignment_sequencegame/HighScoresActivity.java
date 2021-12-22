package com.jakebeecham.madassignment_sequencegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HighScoresActivity extends AppCompatActivity {

    ListView lvNames, lvScores;
    Button btnRestartGame;
    DatabaseHandler db;
    ArrayAdapter<String> itemsAdapterNames, itemsAdapterScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        retrieveData();
        setupElements();
    }

    public void doRestartGame(View view) {
        finish();
        Intent startGameIntent = new Intent(this, MainActivity.class);
        startGameIntent.putExtra("Round", 1);
        startGameIntent.putExtra("SequenceCount", 4);
        startActivity(startGameIntent);
    }

    private void retrieveData() {
        db = new DatabaseHandler(this);
        List<HighScore> top5HighScores = db.getTop5HighScores();
        List<String> namesString = new ArrayList<>();
        List<String> scoresString = new ArrayList<>();
        int j = 1;
        for (HighScore hs : top5HighScores) {
            namesString.add(j++ + " : "  + hs.getName());
        }
        for (HighScore hs : top5HighScores) {
            scoresString.add(String.valueOf(hs.getScore()));
        }
        itemsAdapterNames = new ArrayAdapter<>(this, R.layout.listview_row_names, namesString);
        itemsAdapterScores = new ArrayAdapter<>(this, R.layout.listview_row_scores, scoresString);
    }

    private void setupElements()
    {
        btnRestartGame = findViewById(R.id.btnRestartGame);
        lvNames = findViewById(R.id.lvNames);
        lvScores = findViewById(R.id.lvScores);
        lvNames.setAdapter(itemsAdapterNames);
        lvScores.setAdapter(itemsAdapterScores);
    }
}