package com.example.connor.carmap;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.layout.simple_list_item_1;
import static com.google.maps.android.PolyUtil.containsLocation;

/**
 * Created by palmek4 on 11/20/2014.
 */
public class PointArrayAdapter extends ArrayAdapter<LatLng> {

    List<LatLng> campusPoints = new ArrayList<LatLng>();
    List<LatLng> points = new ArrayList<LatLng>();
    Context context;

    public PointArrayAdapter (Activity context,  ArrayList<LatLng> list) {
        super(context, 0, list);

        this.context = context;

        points = list;

        campusPoints.add(new LatLng(40.018166, -83.023682));
        campusPoints.add(new LatLng(40.017196, -82.996688));
        campusPoints.add(new LatLng(39.986788, -82.994671));
        campusPoints.add(new LatLng(39.987939, -83.025527));
        campusPoints.add(new LatLng(40.018166, -83.023682));


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LatLng point = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_list_item, parent, false);
        }

        TextView tvAddress = (TextView) convertView.findViewById(R.id.list_address);
        TextView tvSS = (TextView) convertView.findViewById(R.id.list_street_sweeping);
        Button delButton = (Button) convertView.findViewById(R.id.list_button_delete);

        String address = point.toString();
        try {
            address = new AddressFromLocation(context).execute(point).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //address = latLongToAddress(point);
        String SS = "There is no upcoming Street Sweeping";

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        StreetSweepingHelper ssh = new StreetSweepingHelper();
        Date thurs = ssh.getNextThursdayStreetSweeping();
        Date fri = ssh.getNextFridayStreetSweeping();



        if (containsLocation(point, campusPoints, false)) SS = "North and East sides of streets: "+format.format(thurs)+"\nSouth and West sides of streets: "+ format.format(fri);

        tvAddress.setText(address);
        tvSS.setText(SS);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                points.remove(point);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private String latLongToAddress (LatLng point){

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses;
        String address = point.toString();

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
