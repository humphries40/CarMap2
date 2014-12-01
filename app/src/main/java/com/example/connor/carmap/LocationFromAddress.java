package com.example.connor.carmap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by palmek4 on 12/1/2014.
 */
public class LocationFromAddress extends AsyncTask<String, Void, LatLng> {

    Context mContext;

    public LocationFromAddress(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected LatLng doInBackground(String... address) {
        Geocoder geocoder =  new Geocoder(mContext, Locale.getDefault());

        Geocoder geoCoder = new Geocoder(mContext);
        double latitude = 0, longitude = 0;

        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(address[0], 1);
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
