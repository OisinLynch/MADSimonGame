package edu.olynch.madsimongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WriteScoreToDatabase extends AppCompatActivity {

    DatabaseHandler mDatabaseHandler;
    Button btnDone, btnFinish;
    EditText editName;
    TextView score;
    int yourScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_score_to_database);

        editName = findViewById(R.id.etYourName);
        btnDone = findViewById(R.id.btnDone);
        btnFinish = findViewById(R.id.btnFinish);
        score = findViewById(R.id.tvScore);

        yourScore = getIntent().getIntExtra("finalScore", 0 );

        score.setText(String.valueOf(yourScore));

        mDatabaseHandler = new DatabaseHandler(this);

        if (yourScore > 2) {
            editName.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.VISIBLE);

        }

    }

    public void AddData(String newName) {
        boolean insertData = mDatabaseHandler.addData(newName);

        if (insertData) {
            Toast.makeText(this, "Score Successfully Saved", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }


    public void doFinish(View view) {
        Intent toFourthPage = new Intent(view.getContext(), PrintScores.class);
        startActivity(toFourthPage);
    }

    public void doPlayAgain(View view) {
        Intent playAgain = new Intent(WriteScoreToDatabase.this, MainActivity.class);
        startActivity(playAgain);
    }

    public void doDone(View view) {
        String newName = editName.getText().toString();
        int newScore = yourScore;
        if (editName.length() != 0) {
            AddData(newName);
            editName.setText("");
        }
        else {
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_LONG).show();
        }
    }
}