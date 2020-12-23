package edu.olynch.madsimongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;
import android.os.CountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    Button bRed, bBlue, bYellow, bGreen, fb;
    int sequenceCount = 4, n = 0;
    private Object mutex = new Object();
    int[] gameSequence = new int[120];
    int arrayIndex = 0;
    int myCurrentScore = 3;

    CountDownTimer ct = new CountDownTimer(6000,  1500) {

        public void onTick(long millisUntilFinished) {
            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1500);
            oneButton();
            //here you can have your logic to set text to edittext
        }

        public void onFinish() {
            //mTextField.setText("done!");
            //We now have the game sequence

            for (int i = 0; i< arrayIndex; i++)
                Log.d("game sequence", String.valueOf(gameSequence[i]));
            // start next activity

            // put the sequence into the next activity
            // stack overglow https://stackoverflow.com/questions/3848148/sending-arrays-with-intent-putextra
            Intent secondPage = new Intent(MainActivity.this, SecondPagePlayGame.class);
            secondPage.putExtra("colourSequence", arrayIndex);
            secondPage.putExtra("scireToBeat", myCurrentScore);
            startActivity(secondPage);




            // start the next activity
            // int[] arrayB = extras.getIntArray("numbers");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);

        DatabaseHandler db = new DatabaseHandler(this);

        db.emptyHiScores();     // empty table if required

        // Inserting hi scores
        Log.i("Insert: ", "Inserting ..");
        db.addHiScore(new HiScores("20 OCT 2020", "Frodo", 2));
        db.addHiScore(new HiScores("28 OCT 2020", "Dobby", 3));
        db.addHiScore(new HiScores("20 NOV 2020", "DarthV", 2));
        db.addHiScore(new HiScores("20 NOV 2020", "Bob", 1));
        db.addHiScore(new HiScores("22 NOV 2020", "Gemma", 2));
        db.addHiScore(new HiScores("30 NOV 2020", "Joe", 30));
        db.addHiScore(new HiScores("01 DEC 2020", "DarthV", 2));
        db.addHiScore(new HiScores("02 DEC 2020", "Gandalf", 132));


        // Reading all scores
        Log.i("Reading: ", "Reading all scores..");
        List<HiScores> hiScores = db.getAllHiScores();


        for (HiScores hs : hiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        Log.i("divider", "====================");

        HiScores singleScore = db.getHiScore(5);
        Log.i("High Score 5 is by ", singleScore.getPlayer_name() + " with a score of " +
                singleScore.getScore());

        Log.i("divider", "====================");

        // Calling SQL statement
        List<HiScores> top5HiScores = db.getTopFiveScores();

        for (HiScores hs : top5HiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // Writing HiScore to log
            Log.i("Score: ", log);
        }
        Log.i("divider", "====================");

        HiScores hiScore = top5HiScores.get(top5HiScores.size() - 1);
        // hiScore contains the 5th highest score
        Log.i("fifth Highest score: ", String.valueOf(hiScore.getScore()) );

        // simple test to add a hi score

        // if 5th highest score < myCurrentScore, then insert new score
        if (hiScore.getScore() < myCurrentScore) {
            db.addHiScore(new HiScores("08 DEC 2020", "Elrond", 40));
        }

        Log.i("divider", "====================");

        // Calling SQL statement
        top5HiScores = db.getTopFiveScores();

        for (HiScores hs : top5HiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();

            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        //Intent finalPage = new Intent(MainActivity.this, PrintScores.class);
        //finalPage.putExtra(String.valueOf(top5HiScores), "top5HighSocres");

        ArrayList<String> scoresString = new ArrayList<String>();
        getIntent().putExtra("top5ScoresList", (Serializable) top5HiScores);

    }

    public void doPlay(View view) {
        ct.start();
    }

    private void oneButton() {
        n = getRandom(sequenceCount);

        Toast.makeText(this, "Number = " + n, Toast.LENGTH_SHORT).show();

        switch (n) {
            case 1:
                flashButton(bBlue);
                gameSequence[arrayIndex++] = BLUE;
                break;
            case 2:
                flashButton(bRed);
                gameSequence[arrayIndex++] = RED;
                break;
            case 3:
                flashButton(bYellow);
                gameSequence[arrayIndex++] = YELLOW;
                break;
            case 4:
                flashButton(bGreen);
                gameSequence[arrayIndex++] = GREEN;
                break;
            default:
                break;
        }   // end switch
    }

    //
    // return a number between 1 and maxValue
    private int getRandom(int maxValue) {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void flashButton(Button button) {
        fb = button;
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                fb.setPressed(true);
                fb.invalidate();
                fb.performClick();
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable() {
                    public void run() {
                        fb.setPressed(false);
                        fb.invalidate();
                    }
                };
                handler1.postDelayed(r1, 600);

            } // end runnable
        };
        handler.postDelayed(r, 600);
    }


    public void doTest(View view) {
        Intent testFinalPage = new Intent(view.getContext(), PrintScores.class);
        startActivity(testFinalPage);
    }

    public void doTest2(View view) {
        Intent testWritingPage = new Intent(view.getContext(), WriteScoreToDatabase.class);
        startActivity(testWritingPage);
    }
}