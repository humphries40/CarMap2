package com.example.connor.carmap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MeterActivity extends Activity {

    private CountDownTimer timer;
    private CountDownTimer pausedTimer;

    private NotificationCompat.Builder timeOut =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.abc_ab_transparent_dark_holo)
                    .setContentTitle("Meter Out of Time")
                    .setContentText("The meter you set is out of time!");

    private NotificationCompat.Builder fiveMinLeft =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.abc_ab_transparent_dark_holo)
                    .setContentTitle("Meter Running Low")
                    .setContentText("Meter has 5 minutes left!");
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


    public void startTimer(View temp){

        EditText mEditMin = (EditText)findViewById(R.id.meterMins);
        EditText mEditHrs = (EditText)findViewById(R.id.meterHrs);
        final TextView time = (TextView)findViewById(R.id.meterTime);
        int secs = 0;
        int hours = 0;
        if(mEditHrs.getText().toString() != null)
        {
            hours = Integer.parseInt(mEditHrs.getText().toString());}
        int mins = 0;
        if(mEditMin.getText().toString() != null){
            mins = Integer.parseInt(mEditMin.getText().toString());
        }

        int milli = ((((hours * 60) + mins) * 60) + secs) *1000;
        String hrs, mns, sc;
        if(timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(milli, 1000) {
            public void onTick(long millisUntilFinished) {
                int secs = (int)(millisUntilFinished / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int hours = mins / 60;
                mins = mins % 60;

                time.setText(hours + "hrs " + mins + "mins " + secs + "secs left on Meter");
                if(hours == 0 && mins == 5 && secs == 0){
                    int mNotificationId = 002;
                    // Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(mNotificationId, fiveMinLeft.build());
                }

            }

            public void onFinish() {
                int mNotificationId = 001;
              // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, timeOut.build());
                time.setText("Time out on Meter!");
            }
        }.start();
    }

    public void stopTimer(View temp)
    {
        timer.cancel();
        final TextView time = (TextView)findViewById(R.id.meterTime);
        time.setText("Meter Canceled");
    }
    public void resetTimer(View temp)
    {
        timer.cancel();
        EditText mEditMin = (EditText)findViewById(R.id.meterMins);
        EditText mEditHrs = (EditText)findViewById(R.id.meterHrs);
        final TextView time = (TextView)findViewById(R.id.meterTime);

        mEditHrs.setText("");
        mEditMin.setText("");
        time.setText("No time on Meter");
    }
}
