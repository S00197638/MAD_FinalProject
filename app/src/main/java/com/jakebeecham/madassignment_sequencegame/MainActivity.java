package com.jakebeecham.madassignment_sequencegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    Animation animation, flashPlay;
    StringBuilder sBSequence;
    CountDownTimer countDownTimer;
    Button btnRed, btnBlue, btnYellow, btnGreen, btnPlay;
    TextView tvInstruction, tvRound, tvSequenceCount;
    Integer sequenceIncreaser = 3000;

    int colorCount = 4, sequenceCount = 2, n = 0;
    int[] gameSequence = new int[120];
    int arrayIndex = 0, round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveData();
        setupElements();
        setStartingValues();
    }

    public void doPlay(View view) {
        doPlayButtonLogic();
        showSequence();
    }

    private void retrieveData() {
        round = getIntent().getIntExtra("Round", 1);
    }

    private void setupElements()
    {
        btnRed = findViewById(R.id.btnRed);
        btnBlue = findViewById(R.id.btnBlue);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        btnPlay = findViewById(R.id.btnPlay);
        tvInstruction = findViewById(R.id.tvInstruction);
        tvRound = findViewById(R.id.tvRound);
        tvSequenceCount = findViewById(R.id.tvSequenceCount);
    }

    private void setStartingValues()
    {
        sequenceCount = sequenceCount + (round * 2);
        tvInstruction.setText("Press Play to Begin!");
        tvRound.setText("Round " + String.valueOf(round));
        tvSequenceCount.setText("Sequence: " + String.valueOf(sequenceCount));
        startPlayButtonAnimation();
    }

    private void startPlayButtonAnimation()
    {
        flashPlay = new AlphaAnimation(1, 0);
        flashPlay.setDuration(200);
        flashPlay.setStartOffset(2000);
        flashPlay.setRepeatCount(10);
        btnPlay.startAnimation(flashPlay);
    }

    private void doPlayButtonLogic()
    {
        btnPlay.clearAnimation();
        sequenceIncreaser *= round;
        sequenceIncreaser += 3000;
        tvInstruction.setText("Remember the Sequence!");
        tvSequenceCount.setText("");
    }

    private void showSequence()
    {
        sBSequence = new StringBuilder("");
        countDownTimer = new CountDownTimer(sequenceIncreaser,  1500)
        {
            public void onTick(long millisUntilFinished) {
                oneButton();
            }

            public void onFinish()
            {
                //Log Sequence
                for (int i = 0; i < arrayIndex; i++)
                    Log.d("game sequence", String.valueOf(gameSequence[i]));

                //Start Next Activity
                callPlayScreen();
            }
        };
        countDownTimer.start();
    }

    private void oneButton()
    {
        n = getRandom(colorCount);
        sBSequence.append(n);

        switch (n)
        {
            case 1:
                flashButton(btnBlue);
                gameSequence[arrayIndex++] = BLUE;
                break;
            case 2:
                flashButton(btnRed);
                gameSequence[arrayIndex++] = RED;
                break;
            case 3:
                flashButton(btnYellow);
                gameSequence[arrayIndex++] = YELLOW;
                break;
            case 4:
                flashButton(btnGreen);
                gameSequence[arrayIndex++] = GREEN;
                break;
            default:
                break;
        }//End of Switch
    }

    //Returns a number between 1 and maxValue
    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void flashButton(Button button)
    {
        animation = new AlphaAnimation(1,0);
        animation.setDuration(500);//Blinking Time
        animation.setRepeatCount(0);
        button.startAnimation(animation);
    }

    private void callPlayScreen()
    {
        String s = String.valueOf(sBSequence);
        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("GameSequence", s);
        playIntent.putExtra("SequenceCount", sequenceCount);
        startActivity(playIntent);
    }
}