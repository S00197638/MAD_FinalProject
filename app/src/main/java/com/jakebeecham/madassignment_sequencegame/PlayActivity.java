package com.jakebeecham.madassignment_sequencegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int BLUE, RED, YELLOW, GREEN;

    StringBuilder sBMyGuessSequence;
    Button btnRed, btnBlue, btnYellow, btnGreen;
    TextView tvPlayInstruction, tvPlayInstruction2;

    int selectCounter, sequenceCount, score;
    String sequenceToGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        retrieveData();
        setupElements();
        setStartingValues();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];

        if (x < 0.3f & x > 0.0f) { pickBlue(); }
        else if (x > 0.6f & x < 0.9f){ pickRed(); }

        if (y < 0.2f) { pickYellow(); }
        else if (y > 0.7f){ pickGreen(); }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    public void doSelectBlue(View view) { pickBlue(); }

    public void doSelectRed(View view) { pickRed(); }

    public void doSelectYellow(View view) { pickYellow(); }

    public void doSelectGreen(View view) { pickGreen(); }

    //App running, but not on screen - in the background
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this); //Turn off listener to save power
    }

    //When the app is brought to the foreground - using app on screen
    protected void onResume() {
        super.onResume();
        //Turn on the Sensor
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void retrieveData() {
        sequenceToGuess = getIntent().getStringExtra("GameSequence");
        sequenceCount = getIntent().getIntExtra("SequenceCount", 0);
    }

    private void setupElements()
    {
        btnRed = findViewById(R.id.btnRed);
        btnBlue = findViewById(R.id.btnBlue);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        tvPlayInstruction = findViewById(R.id.tvPlayInstruction);
        tvPlayInstruction2 = findViewById(R.id.tvPlayInstruction2);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sBMyGuessSequence = new StringBuilder("");
    }

    private void setStartingValues()
    {
        BLUE = 1;
        RED = 2;
        YELLOW = 3;
        GREEN = 4;
        selectCounter = 0;
        score = 0;
        tvPlayInstruction.setText("Select Colors in Order of Sequence!");
        tvPlayInstruction2.setText("Tilt / Tap to Select a Color!");
    }

    private void pickBlue()
    {
        sBMyGuessSequence.append(BLUE);
        Toast.makeText(this, "BLUE", Toast.LENGTH_SHORT).show();
        checkIfSelectedEnoughColors();
    }

    private void pickRed()
    {
        sBMyGuessSequence.append(RED);
        Toast.makeText(this, "RED", Toast.LENGTH_SHORT).show();
        checkIfSelectedEnoughColors();
    }

    private void pickYellow()
    {
        sBMyGuessSequence.append(YELLOW);
        Toast.makeText(this, "YELLOW", Toast.LENGTH_SHORT).show();
        checkIfSelectedEnoughColors();
    }

    private void pickGreen()
    {
        sBMyGuessSequence.append(GREEN);
        Toast.makeText(this, "GREEN", Toast.LENGTH_SHORT).show();
        checkIfSelectedEnoughColors();
    }

    private void checkIfSelectedEnoughColors()
    {
        selectCounter++;
        if(selectCounter == sequenceCount) { checkIfCorrect(); }
    }

    private void checkIfCorrect()
    {
        String myGuess = String.valueOf(sBMyGuessSequence);
        if (myGuess.compareTo(sequenceToGuess) == 0) { doPlayNextRound(); }
        else { doCallGameOver(); }
    }

    private void doPlayNextRound()
    {
        finish();
        int r = sequenceCount / 2;
        Intent sequenceIntent = new Intent(this, MainActivity.class);
        sequenceIntent.putExtra("Round", r);
        sequenceIntent.putExtra("SequenceCount", sequenceCount);
        startActivity(sequenceIntent);
    }

    private void doCallGameOver()
    {
        finish();
        if (sequenceCount == 4){ score = 0; }
        else { score = sequenceCount - 2; }
        Intent gameOverIntent = new Intent(this, GameOverActivity.class);
        gameOverIntent.putExtra("Score", score);
        startActivity(gameOverIntent);
    }
}