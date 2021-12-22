package com.jakebeecham.madassignment_sequencegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GameOverActivity extends AppCompatActivity {

    Button btnPlayAgain, btnHighScores;
    TextView tvMyScore, tvNameTitle, tvScoreTitle, tvMyScoreCenter, tvScoreTitleCenter;
    EditText etMyName;

    DatabaseHandler db;
    HighScore scoreToReplace = null;
    int score;
    boolean isAHighScore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        retrieveData();
        setupElements();
        setStartingValues();
    }

    public void doPlayAgain(View view) {
        if (isAHighScore)
        {
            if (!etMyName.getText().toString().equals(""))
            {
                addMyScoreToDatabase();
                restartGame();
            }
            else
            {
                //Toast Error
                Toast.makeText(this, "Please Enter your Name!", Toast.LENGTH_SHORT).show();
            }
        }
        else { restartGame(); }
    }

    public void doCheckHighScores(View view) {
        if (isAHighScore)
        {
            if (!etMyName.getText().toString().equals(""))
            {
                addMyScoreToDatabase();
                seeHighScores();
            }
            else
            {
                //Toast Error
                Toast.makeText(this, "Please Enter your Name!", Toast.LENGTH_SHORT).show();
            }
        }
        else { seeHighScores(); }
    }

    private void retrieveData() {
        score = getIntent().getIntExtra("Score", -1);
        db = new DatabaseHandler(this);
        checkIfHighScore();
    }

    private void checkIfHighScore()
    {
        List<HighScore> allHighScores = db.getAllHighScores();
        if (!allHighScores.isEmpty())
        {
            List<HighScore> top5HighScores = db.getTop5HighScores();
            for (HighScore hs : top5HighScores) {
                if (score > hs.getScore()) {
                    isAHighScore = true;
                    scoreToReplace = hs;
                    break;//Found Score to Replace
                }
            }
            if (scoreToReplace == null) { isAHighScore = false; }
        }
        else {
            setupInitialHighScores();
            checkIfHighScore();
        }
    }

    private void setupInitialHighScores()
    {
        db.addAHighScore(new HighScore("Wanda", "08 JAN 2021", 124));
        db.addAHighScore(new HighScore("Steven", "27 MAR 2021", 14));
        db.addAHighScore(new HighScore("Yelena", "24 MAY 2021", 12));
        db.addAHighScore(new HighScore("Peter", "15 DEC 2021", 8));
        db.addAHighScore(new HighScore("Kate", "13 SEP 2021", 6));
    }

    private void setupElements()
    {
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnHighScores = findViewById(R.id.btnHighScores);
        tvScoreTitleCenter = findViewById(R.id.tvScoreTitleCenter);
        tvMyScoreCenter = findViewById(R.id.tvMyScoreCenter);
        tvScoreTitle = findViewById(R.id.tvScoreTitle);
        tvNameTitle = findViewById(R.id.tvNameTitle);
        tvMyScore = findViewById(R.id.tvMyScore);
        etMyName = findViewById(R.id.etMyName);
    }

    private void setStartingValues()
    {
        if (isAHighScore)
        {
            tvScoreTitleCenter.setVisibility(View.INVISIBLE);
            tvMyScoreCenter.setVisibility(View.INVISIBLE);
            tvScoreTitle.setVisibility(View.VISIBLE);
            tvNameTitle.setVisibility(View.VISIBLE);
            tvMyScore.setVisibility(View.VISIBLE);
            etMyName.setVisibility(View.VISIBLE);
            tvMyScore.setText(String.valueOf(score));
            etMyName.setEnabled(true);
            etMyName.setText("");
        }
        else
        {
            tvScoreTitle.setVisibility(View.INVISIBLE);
            tvNameTitle.setVisibility(View.INVISIBLE);
            tvMyScore.setVisibility(View.INVISIBLE);
            etMyName.setVisibility(View.INVISIBLE);
            etMyName.setEnabled(false);
            tvScoreTitleCenter.setVisibility(View.VISIBLE);
            tvMyScoreCenter.setVisibility(View.VISIBLE);
            tvMyScoreCenter.setText(String.valueOf(score));
        }
    }

    private void addMyScoreToDatabase()
    {
        //If Score to Replace < my Score, then insert new score
        if (scoreToReplace.getScore() < score) {
            db.addAHighScore(new HighScore(
                    etMyName.getText().toString(),
                    new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()),
                    score));
        }
    }

    private void restartGame()
    {
        finish();
        Intent startGameIntent = new Intent(this, MainActivity.class);
        startGameIntent.putExtra("Round", 1);
        startGameIntent.putExtra("SequenceCount", 4);
        startActivity(startGameIntent);
    }

    private void seeHighScores()
    {
        finish();
        Intent highScoresIntent = new Intent(this, HighScoresActivity.class);
        startActivity(highScoresIntent);
    }
}