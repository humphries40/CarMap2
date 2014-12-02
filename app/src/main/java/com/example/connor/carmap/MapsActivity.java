package com.example.connor.carmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.location.Location;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Geocoder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.maps.android.PolyUtil.containsLocation;


public class MapsActivity extends FragmentActivity implements View.OnClickListener{

    public ArrayList<Marker> markers;//this stores all the markers on the map for saving them and restoring them
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final String TAG = ((Object) this).getClass().getSimpleName();
    public List<LatLng> campusPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "+++ In onCreate() +++");
        setContentView(R.layout.activity_maps);

        View btnSubmit = findViewById(R.id.button_submit);
        btnSubmit.setOnClickListener(this);

        setUpMapIfNeeded();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.help_settings) {
            startActivity(new Intent(this, MapHelpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        markers = new ArrayList<Marker>();
        Log.e(TAG, "+++ In onStart() +++");
      //  setContentView(R.layout.activity_maps);
         setUpMapIfNeeded();
    }
    protected void onResume() {
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
                MarkerOptions temp = new MarkerOptions().position(new LatLng(latitude, longitude));

                Marker mark = mMap.addMarker(temp);
                markers.add(mark);
                mark.remove();
            }
            din.close();
            loadMarkers(markers);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        super.onResume();
        Log.e(TAG, "+++ In onResume() +++");
        setUpMapIfNeeded();

    }

    protected void onPause() {
        try {

            //Write the Array of markers to file

            // Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE

            FileOutputStream output = openFileOutput("latlngpoints.txt",Context.MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(output);
            dout.writeInt(markers.size()); // Save line count
            for (Marker curMark : markers) {
                LatLng point = curMark.getPosition();
                dout.writeUTF(point.latitude + "," + point.longitude);
                Log.v("write", point.latitude + "," + point.longitude);
            }
            dout.flush(); // Flush stream ...
            dout.close(); // ... and close.
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        super.onPause();
        Log.e(TAG, "+++ In onPause() +++");

    }

    protected void onDestroy() {
        super.onDestroy();
    }

     /* Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.

        //check for network connection
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = ((activeNetwork != null) && (activeNetwork.isConnectedOrConnecting()));

        if (isConnected) {
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        } else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Connectivity Error");
            alert.setMessage("CarMap needs internet access for most features to function, please connect to the internet before using the map or homepage activities.");
            alert.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });
            alert.show();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {


        LatLng mylocation = new LatLng(39.961309, -82.999197);

        //check for gps connection
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsConnection = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsConnection){

            Location myLocation = null;
            Criteria c = new Criteria();
            c.setAccuracy(Criteria.ACCURACY_FINE);

            String bestProvider = manager.getBestProvider(c, true);

            if (myLocation == null) {
                myLocation = manager.getLastKnownLocation("network");
            }

            double lat= myLocation.getLatitude();
            double longe = myLocation.getLongitude();
            mylocation = new LatLng(lat, longe);
        }else {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("No GPS");
            alert.setMessage("CarMap needs GPS to be enabled to display your current location, click okay to enable GPS.");

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            alert.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Cancelled
                }
            });
            alert.show();
        }

        final Polygon campus = mMap.addPolygon(new PolygonOptions()
            .add(new LatLng(40.018166, -83.023682), new LatLng(40.017196, -82.996688), new LatLng(39.986788, -82.994671), new LatLng
       (39.987939, -83.025527), new LatLng(40.018166,-83.023682)).visible(false));

        campusPoints = campus.getPoints();



        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

                Context context = getApplicationContext();
                CharSequence text = "Added Marker at  " + point.toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                MarkerOptions temp = new MarkerOptions().position((point));
                if (containsLocation(point, campusPoints, false)) {
                    temp.snippet("campus");
                    temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
                Marker mark = mMap.addMarker(temp);
                markers.add(mark);

            }
        });

        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng point = marker.getPosition();
                Context context = getApplicationContext();
                CharSequence text = "Deleted Marker at " + point.toString();
                int duration = Toast.LENGTH_SHORT;


                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                for (int i = 0; i < markers.size(); i++) {
                    Marker mark = markers.get(i);
                    LatLng point22 = mark.getPosition();
                    if (point.equals(point22)) markers.remove(i);
                }
                marker.remove();
                return false;
            }
        });


        if (gpsConnection) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 15));
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    public void onClick(View v){
        if (v.getId() == R.id.button_submit) {

            EditText mAddress = (EditText)findViewById(R.id.address_text);
            String addr = mAddress.getText().toString();

            Context context = getApplicationContext();
            CharSequence text = "Address = "+ addr;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            LatLng point = new LatLng(0.0,0.0);
            if (addr.length() > 0){
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
                    text = "Invalid Address";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {

                    MarkerOptions temp = new MarkerOptions().position((point));
                    if (containsLocation(point, campusPoints, false)) {
                        temp.snippet("campus");
                        temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }

                    Marker marker = mMap.addMarker(temp);
                    markers.add(marker);
                }
            }
        }
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

    private void loadMarkers(ArrayList<Marker> toLoad){
        int count = 0;
        while(count < toLoad.size()) {

            Marker mark = toLoad.get(count);
            LatLng point = mark.getPosition();
            MarkerOptions temp = new MarkerOptions().position((point));
            if (containsLocation(point, campusPoints, false)) {
                temp.snippet("campus");
                temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
            mMap.addMarker(temp);
            count++;
        }
    }
}
