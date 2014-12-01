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
public class AddressFromLocation extends AsyncTask<LatLng, Void, String>{

    Context mContext;

    public AddressFromLocation(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected String doInBackground(LatLng... points) {
        Geocoder geocoder =  new Geocoder(mContext, Locale.getDefault());

        LatLng point = points[0];
        String address = point.toString();

        // Create a list to contain the result address
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

            if (addresses .size() > 0) {
                address = addresses.get(0).getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
