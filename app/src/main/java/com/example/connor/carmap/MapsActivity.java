package com.example.connor.carmap;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements View.OnClickListener{

    private ArrayList<MarkerOptions> markers;//this stores all the markers on the map for saving them and restoring them
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final String TAG = ((Object) this).getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "+++ In onCreate() +++");
        setContentView(R.layout.activity_maps);

        View btnNewGame = findViewById(R.id.button_submit);
        btnNewGame.setOnClickListener(this);

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
        Log.e(TAG, "+++ In onStart() +++");
      //  setContentView(R.layout.activity_maps);
         setUpMapIfNeeded();
    }
    protected void onResume() {
        Context context = getApplicationContext();
        String ser = SerializeObject.ReadSettings(context, "carMapMarkers.dat");
        if (ser != null && !ser.equalsIgnoreCase("")) {
            Object obj = SerializeObject.stringToObject(ser);
            // Then cast it to your object and
            if (obj instanceof ArrayList) {
                // Do something
                markers = (ArrayList<MarkerOptions>)obj;
            }
        }
        int count = 0;
        while(count < markers.size())
        {
            mMap.addMarker(markers.get(count));
            count++;
        }
        super.onResume();
        Log.e(TAG, "+++ In onResume() +++");
        setUpMapIfNeeded();
    }

    protected void onPause() {
        String ser = SerializeObject.objectToString(markers);
        Context context = getApplicationContext();
        if (ser != null && !ser.equalsIgnoreCase("")) {
            SerializeObject.WriteSettings(context, ser, "carMapMarkers.dat");
        } else {
            SerializeObject.WriteSettings(context, "", "carMapMarkers.dat");
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
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location myLocation = locationmanager.getLastKnownLocation(locationProvider);

        double lat= myLocation.getLatitude();
        double longe = myLocation.getLongitude();
        LatLng mylocation = new LatLng(lat, longe);

        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                MarkerOptions temp = new MarkerOptions().position((point));
                mMap.addMarker(temp);
                markers.add(temp);

            }
        });

        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                LatLng point = marker.getPosition();
                Context context = getApplicationContext();
                CharSequence text = "Deleting Marker at position: " + point.toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                if(markers.contains(marker))
                {
                    markers.remove(marker);
                }
                marker.remove();
                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 15));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    public void onClick(View v){
        if (v.getId() == R.id.button_submit) {

            EditText mAddress = (EditText)findViewById(R.id.address);
            String addr = mAddress.getText().toString();

            Context context = getApplicationContext();
            CharSequence text = "Address = "+ addr;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            if (addr.length() > 0){

                LatLng point = getLocationFromAddress(addr);
                MarkerOptions temp = new MarkerOptions().position((point));
                mMap.addMarker(temp);
                markers.add(temp);
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder geoCoder = new Geocoder(this);
        List<Address> address;
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

        LatLng pos = new LatLng(latitude, longitude);
        return pos;
    }
}
