package edu.olynch.madsimongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintScores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_scores);

        ListView scoresList = findViewById(R.id.listScores);

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
        int myCurrentScore = 3;
        // if 5th highest score < myCurrentScore, then insert new score
        if (hiScore.getScore() < myCurrentScore) {
            db.addHiScore(new HiScores("08 DEC 2020", "Elrond", 40));
        }

        Log.i("divider", "====================");

        // Calling SQL statement
        top5HiScores = db.getTopFiveScores();
        List<String> scoresStr;
        scoresStr = new ArrayList<>();

        int j = 1;
        for (HiScores hs : top5HiScores) {
            String log =
                    "Id: " + hs.getScore_id() +
                            ", Date: " + hs.getGame_date() +
                            " , Player: " + hs.getPlayer_name() +
                            " , Score: " + hs.getScore();
            // Store score in string array
            scoresStr.add(j++ + " : " +
                    hs.getPlayer_name() + "\t" +
                    hs.getScore());
            // Writing HiScore to log
            Log.i("Score: ", log);
        }

        Log.i("divider", "===============");
        Log.i("divider", "Scores in list <>");
        for (String ss : scoresStr) {
            Log.i("Score: ", ss);
        }

        //Print SQLLite database to the ListView
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoresStr);
        scoresList.setAdapter(itemsAdapter);

    }


    public void doPlayAgain(View view) {
        Intent playAgain = new Intent(PrintScores.this, MainActivity.class);
        startActivity(playAgain);
    }
}