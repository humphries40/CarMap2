package com.example.connor.carmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HomeActivity extends Activity {

    private final String TAG = ((Object) this).getClass().getSimpleName();
    private ArrayList<LatLng> points;
    PointArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "+++ In onCreate() +++");
        setContentView(R.layout.activity_homescreen);
        populateList();

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
        if (id == R.id.clear_list) {
            points.clear();
            adapter.notifyDataSetChanged();
            return true;
        }
        if (id == R.id.refresh_list) {
            adapter.notifyDataSetChanged();
            return true;
        }
        if (id == R.id.add_address) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Add an Address");
            alert.setMessage("Enter a fully qualified address or the name of a place");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String addr = input.getText().toString();
                    Context context = getApplicationContext();
                    // Do something with value!
                    if (addr.length() > 0) {

                        LatLng point = new LatLng(0.0,0.0);

                        try {
                            point = new LocationFromAddress(context).execute(addr).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        //LatLng point = getLocationFromAddress(addr);
                        LatLng error = new LatLng(0.0,0.0);
                        if (point.equals(error))
                        {
                            CharSequence text = "Invalid Address";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                        } else {
                            points.add(point);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        populateList();
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

    private void populateList() {

        points = new ArrayList<LatLng>();


        //check for network connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting()));

        //check for gps connection
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsConnection = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (isConnected) {
            //popuate arrayList of LatLngs
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

<<<<<<< HEAD
        //create the adapter
        adapter = new PointArrayAdapter(this, points);
        //attach adapter to view
        ListView listView = (ListView) findViewById(R.id.parkingSpaces);
        listView.setAdapter(adapter);
=======
            //create the adapter
            adapter = new PointArrayAdapter(this, points);
            //attach adapter to view
            ListView listView = (ListView) findViewById(R.id.lv);
            listView.setAdapter(adapter);
        } else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Connectivity Error");
            alert.setMessage("CarMap needs internet and GPS access for most features to function, please connect to the internet before using the map or homepage activities.");

            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });
            alert.show();
        }
>>>>>>> origin/master
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder geoCoder = new Geocoder(this);
        double latitude = 0, longitude = 0;

        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(strAddress, 1);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LatLng(latitude, longitude);
    }

}
