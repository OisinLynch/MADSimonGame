package edu.olynch.madsimongame;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SecondPagePlayGame extends AppCompatActivity implements SensorEventListener {

    private final double NORTH_MOVE_FORWARD = 9.0;     // upper mag limit
    private final double NORTH_MOVE_BACKWARD = 6.0;    // lower mag limit
    private final double SOUTH_MOVE_FORWARD = 4.0;
    private final double SOUTH_MOVE_BACKWARD = -2.0;
    private final double WEST_MOVE_FORWARD = -3.0;
    private final double WEST_MOVE_BACKWARD = 0.0;
    private final double EAST_MOVE_FORWARD = 2.0;
    private final double EAST_MOVE_BACKWARD = 0.0;
    boolean highLimit = false;      // detect high limit
    int counterNorth = 0, counterSouth = 0, counterWest = 0, counterEast = 0; // direction counters

    TextView tvx, tvy, tvz, tvNorth, tvSouth, tvWest, tvEast;
    private SensorManager mSensorManager;
    private Sensor mSensor;

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

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
            tvNorth.setText(String.valueOf(counterNorth));
            highLimit = false;

        } else if ((z > SOUTH_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;

        } else if ((z < SOUTH_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the south
            counterSouth++;
            tvSouth.setText(String.valueOf(counterSouth));
            highLimit = false;

        } else if ((y > WEST_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;

        } else if ((y < WEST_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the west
            counterWest++;
            tvWest.setText(String.valueOf(counterWest));
            highLimit = false;

        } else if ((y > EAST_MOVE_FORWARD) && (highLimit == false)) {
            highLimit = true;

        } else if ((y < EAST_MOVE_BACKWARD) && (highLimit == true)) {
            // we have a tilt to the east
            counterEast++;
            tvEast.setText(String.valueOf(counterEast));
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

}