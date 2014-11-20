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
import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;
import static com.google.maps.android.PolyUtil.containsLocation;

/**
 * Created by palmek4 on 11/20/2014.
 */
public class PointArrayAdapter extends BaseAdapter {

    ArrayList<LatLng> points;
    Activity context;

    LayoutInflater myInflator;
    List<LatLng> campusPoints = new ArrayList<LatLng>();

    public PointArrayAdapter (Activity context,  ArrayList<LatLng> list) {
        super();
        this.context = context;
        this.points = list;

        myInflator = LayoutInflater.from(context);


        CharSequence text = "In Adapter " + points.toString();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        campusPoints.add(new LatLng(40.018166, -83.023682));
        campusPoints.add(new LatLng(40.017196, -82.996688));
        campusPoints.add(new LatLng(39.986788, -82.994671));
        campusPoints.add(new LatLng(39.987939, -83.025527));
        campusPoints.add(new LatLng(40.018166, -83.023682));


    }

    @Override
    public int getCount() {

        CharSequence text = "Size of Points = " + points.size();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        return points.size();
    }

    @Override
    public LatLng getItem(int position) {
        CharSequence text = "Getting item: " + points.get(position).toString();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        return points.get(position);
    }

    @Override
    public long getItemId(int index) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        CharSequence text = "In getView!!!!!!!!!!!!!!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        LatLng point = getItem(i);

        if (view == null) {
            view = myInflator.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            view.setTag(android.R.id.text1,view.findViewById(android.R.id.text1));
        }


        String address = latLongToAddress(point);

        TextView addr = (TextView)view.getTag(android.R.id.text1);
        addr.setText(address);

        //TextView ss = (TextView)v.findViewById(android.R.id.t);

        //if (containsLocation(point, campusPoints, false)){
        //    ss.setText(R.string.street_sweeping);
       // }
        //else {
       //     ss.setText(R.string.no_street_sweeping);
        //}

        return view;
    }

    private String latLongToAddress (LatLng point){

        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        String address = point.toString();

        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);

            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;

    }
}
