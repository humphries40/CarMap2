package com.example.connor.carmap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;




public class HomeActivity extends Activity {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    private ArrayList<LatLng> points;//this stores all the markers on the map for saving them and restoring them
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "+++ In onCreate() +++");
        setContentView(R.layout.activity_homescreen);
        points = new ArrayList<LatLng>();

        try {
            FileInputStream input = openFileInput("latlngpoints.txt");
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) {
                String str = din.readUTF();
                Log.v("read", str);
                String[] stringArray = str.split(",");
                double latitude = Double.parseDouble(stringArray[0]);
                double longitude = Double.parseDouble(stringArray[1]);
                LatLng temp = new LatLng(latitude, longitude);

                points.add(temp);
            }
            din.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }



        CharSequence text = "In Home Activity " + points.toString();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        lv = (ListView)findViewById(R.id.lv);
        PointArrayAdapter adapter = new PointArrayAdapter(this, points);
        lv.setAdapter(adapter);

        LatLng test = adapter.getItem(0);

        text = "In Home Activity test getItem " + test.toString();
        toast = Toast.makeText(this, text, duration);
        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        points = new ArrayList<LatLng>();
        Log.e(TAG, "+++ In onStart() +++");
        setContentView(R.layout.activity_homescreen);
    }

    protected void onResume() {
        super.onResume();
        Log.e(TAG, "+++ In onResume() +++");
        try {
            FileInputStream input = openFileInput("latlngpoints.txt");
            DataInputStream din = new DataInputStream(input);
            int sz = din.readInt(); // Read line count
            for (int i = 0; i < sz; i++) {
                String str = din.readUTF();
                Log.v("read", str);
                String[] stringArray = str.split(",");
                double latitude = Double.parseDouble(stringArray[0]);
                double longitude = Double.parseDouble(stringArray[1]);
                LatLng temp = new LatLng(latitude, longitude);

                points.add(temp);
            }
            din.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }
    protected void onPause() {
        try {

            //Write the Array of markers to file

            // Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE

            FileOutputStream output = openFileOutput("latlngpoints.txt", Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(points.size()); // Save line count
            for (LatLng curMark : points) {
                dout.writeUTF(curMark.latitude + "," + curMark.longitude);
                Log.v("write", curMark.latitude + "," + curMark.longitude);
            }
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        super.onPause();
        Log.e(TAG, "+++ In onPause() +++");

    }
}
