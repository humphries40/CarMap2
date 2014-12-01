package com.example.connor.carmap;

import android.app.Fragment;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;

/**
 * Created by Connor on 12/1/2014.
 */
public class MapsActivityTest extends ActivityUnitTestCase<MapsActivity>{

      private Intent mLaunchIntent;

      public MapsActivityTest() {
          super(MapsActivity.class);
      }


      @Override
      protected void setUp() throws Exception {
            super.setUp();
            //Create an intent to launch target Activity
            mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
                    MapsActivity.class);
        }

        /**
         * Tests the preconditions of this test fixture.
         */
        @MediumTest
        public void testPreconditions() {
            //Start the activity under test in isolation, without values for savedInstanceState and
            //lastNonConfigurationInstance
            startActivity(mLaunchIntent, null, null);

            //final View map = (View) getActivity().findViewById(R.id.map);
            assertNotNull("mMapActivity is null", getActivity());
            

        }
}
