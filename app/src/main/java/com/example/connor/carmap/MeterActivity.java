package com.example.connor.carmap;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;


public class MeterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createMeterTimer(int hours, int mins, int secs){

        int milli = ((((hours * 60) + mins) * 60) + secs) *1000;
        String hrs, mns, sc;
        new CountDownTimer(milli, 1000) {
            public void onTick(long millisUntilFinished) {
                int secs = (int)(millisUntilFinished / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int hours = mins / 60;
                mins = mins % 60;

                //Needs to return these fields to the clock
                // Need to have this return warnings at certain intervals
            }

            public void onFinish() {
                //Need to have this return an alarm
            }
        }.start();
    }
}
