package edu.olynch.madsimongame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondPagePlayGame extends AppCompatActivity implements SensorEventListener {

    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    private final double NORTH_MOVE_FORWARD = 9.0;     // upper mag limit
    private final double NORTH_MOVE_BACKWARD = 6.0;    // lower mag limit
    private final double SOUTH_MOVE_FORWARD = 4.0;
    private final double SOUTH_MOVE_BACKWARD = -2.0;
    private final double WEST_MOVE_FORWARD = -3.0;
    private final double WEST_MOVE_BACKWARD = 2.0;
    private final double EAST_MOVE_FORWARD = 3.0;
    private final double EAST_MOVE_BACKWARD = -2.0;
    boolean highLimit = false;      // detect high limit
    int counterNorth = 0, counterSouth = 0, counterWest = 0, counterEast = 0; // direction counters
    Button bRed, bBlue, bYellow, bGreen, fb;

    TextView tvx, tvy, tvz, tvNorth, tvSouth, tvWest, tvEast;
    int finalScore = 0;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    int[] resultSequence = new int[120];
    int arrayIndex = 0;
    int[] firstPageSequence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page_play_game);

        tvx = findViewById(R.id.tvX);
        tvy = findViewById(R.id.tvY);
        tvz = findViewById(R.id.tvZ);
        tvNorth = findViewById(R.id.tvNorth);
        tvSouth = findViewById(R.id.tvSouth);
        tvWest = findViewById(R.id.tvWest);
        tvEast = findViewById(R.id.tvEast);

        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Get the colour sequence from the first page
        Bundle extras = getIntent().getExtras();
        firstPageSequence = extras.getIntArray("colourSequence");
    }


    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }

    public void onFinish() {
        if (resultSequence == firstPageSequence) {
            Intent intent = new Intent(SecondPagePlayGame.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (resultSequence != firstPageSequence) {
                Intent toThirdPage = new Intent(SecondPagePlayGame.this, WriteScoreToDatabase.class);
                toThirdPage.putExtra("finalScore", finalScore);
                startActivity(toThirdPage);
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        tvx.setText(String.valueOf(x));
        tvy.setText(String.valueOf(y));
        tvz.setText(String.valueOf(z));


        // Can we get a north movement
        // you need to do your own mag calculating
        if ((x > NORTH_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;
        }

        if ((x < NORTH_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the north
            counterNorth++;
            flashButton(bRed);
            resultSequence[arrayIndex++] = RED;
            finalScore++;
            tvNorth.setText(String.valueOf(counterNorth));
            highLimit = false;

        } else if ((z > SOUTH_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;

        } else if ((z < SOUTH_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the south
            counterSouth++;
            flashButton(bYellow);
            resultSequence[arrayIndex++] = YELLOW;
            finalScore++;
            tvSouth.setText(String.valueOf(counterSouth));
            highLimit = false;

        } else if ((y < WEST_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;
        }
        else if ((y > WEST_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the north
            counterWest++;
            flashButton(bGreen);
            resultSequence[arrayIndex++] = GREEN;
            finalScore++;
            tvEast.setText(String.valueOf(counterWest));
            highLimit = false;
        }

        else if ((y > EAST_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;
        }
        else if ((y < EAST_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the north
            counterEast++;
            flashButton(bBlue);
            resultSequence[arrayIndex++] = BLUE;
            finalScore++;
            tvWest.setText(String.valueOf(counterEast));
            highLimit = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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

    public void doFinish2(View view) {
        if (resultSequence == firstPageSequence) {
            Intent intent = new Intent(SecondPagePlayGame.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (resultSequence != firstPageSequence) {
            Intent toThirdPage = new Intent(SecondPagePlayGame.this, WriteScoreToDatabase.class);
            toThirdPage.putExtra("finalScore", finalScore);
            startActivity(toThirdPage);
        }
        /*else if (resultSequence != firstPageSequence && finalScore <= 2) {
            Intent toFourthPage = new Intent(SecondPagePlayGame.this, PrintScores.class);
            startActivity(toFourthPage);
        }*/
    }

}