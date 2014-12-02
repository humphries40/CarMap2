package com.example.connor.carmap;

import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Connor on 12/1/2014.
 */
public class MapsActivityTest extends ActivityInstrumentationTestCase2<MapsActivity> {

      private MapsActivity mMapActivity;
      private GoogleMap mMapFrag;
      public MapsActivityTest() {
          super(MapsActivity.class);
      }


      @Override
      protected void setUp() throws Exception {
            super.setUp();
            mMapActivity = getActivity();
            //mMapFrag = mMapActivity.mMap;
        }

        /**
         * Tests the preconditions of this test fixture.
         */
        @MediumTest
        public void testPreconditions() {
            //Start the activity under test in isolation, without values for savedInstanceState and
            //lastNonConfigurationInstance

            //final View map = (View) getActivity().findViewById(R.id.map);
            mMapActivity = getActivity();
            assertNotNull("mMapActivity is null", mMapActivity);
        }

        @MediumTest
        public void testSearchForLocation()  {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditText mAddress = (EditText) getActivity().findViewById(R.id.address_text);
                    mAddress.setText("2323 Indianola");

                    ArrayList<Marker> points = (ArrayList<Marker>) getActivity().markers;
                    int size = points.size();

                    final Button submit = (Button) getActivity().findViewById(R.id.button_submit);
                    submit.performClick();

                    ArrayList<Marker> pointsTwo = (ArrayList<Marker>) getActivity().markers;
                    assertTrue("Marker wasn't Added", size == pointsTwo.size() -1);
                }
            });

        }



}
