package com.example.connor.carmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


public class NavigationActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //check for network connection
        ConnectivityManager cm =(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting()));

        //check for gps connection
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean gpsConnection = manager.isProviderEnabled( LocationManager.GPS_PROVIDER );



        if (!isConnected && !gpsConnection) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Connectivity Error");
            alert.setMessage("CarMap needs internet and GPS access for most features to function, please connect to the internet before using the map or homepage activities.");

            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();
        } else if (!isConnected) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Connectivity Error");
            alert.setMessage("CarMap needs internet access for most features to function, please connect to the internet before using the map or homepage activities.");

            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();
        } else if (!gpsConnection) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Connectivity Error");
            alert.setMessage("CarMap needs GPS access for most features to function, please connect to the internet before using the map or homepage activities.");

            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();
        }



        View btnNewGame = findViewById(R.id.home);
        btnNewGame.setOnClickListener(this);
        View btnAudio = findViewById(R.id.map);
        btnAudio.setOnClickListener(this);
        View btnVideo = findViewById(R.id.meter);
        btnVideo.setOnClickListener(this);
        View btnExit = findViewById(R.id.exit);
        btnExit.setOnClickListener(this);
    }


    private void quitApplication(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit)
                .setMessage("Quit CarMap?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
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

    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.home:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.map:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.meter:
                startActivity(new Intent(this, MeterActivity.class));
                break;
            case R.id.exit:
            {
                quitApplication();
            }
            break;
        }
    }
}
