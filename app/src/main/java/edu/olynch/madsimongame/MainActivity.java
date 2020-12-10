package edu.olynch.madsimongame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        db.emptyHiScores();     // empty table if required

        // Inserting hi scores
        Log.i("Insert: ", "Inserting ..");
        db.addHiScore(new HiScores("20 OCT 2020", "Frodo", 12));
        db.addHiScore(new HiScores("28 OCT 2020", "Dobby", 16));
        db.addHiScore(new HiScores("20 NOV 2020", "DarthV", 20));
        db.addHiScore(new HiScores("20 NOV 2020", "Bob", 18));
        db.addHiScore(new HiScores("22 NOV 2020", "Gemma", 22));
        db.addHiScore(new HiScores("30 NOV 2020", "Joe", 30));
        db.addHiScore(new HiScores("01 DEC 2020", "DarthV", 22));
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
        Log.i("divider", "Top 5 Scores");

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
    }

    public void doPlay(View view) {
    }
}