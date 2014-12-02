package com.example.connor.carmap;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Connor on 12/1/2014.
 */
public class HomeActivityTest extends ActivityUnitTestCase<HomeActivity>{

      private Intent mLaunchIntent;

      public HomeActivityTest() {
          super(HomeActivity.class);
      }


      @Override
      protected void setUp() throws Exception {
            super.setUp();
            //Create an intent to launch target Activity
            mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
                    HomeActivity.class);
        }

        /**
         * Tests the preconditions of this test fixture.
         */
        @MediumTest
        public void testPreconditions() {
            //Start the activity under test in isolation, without values for savedInstanceState and
            //lastNonConfigurationInstance
            startActivity(mLaunchIntent, null, null);
            final ListView parkingSpacesList = (ListView) getActivity().findViewById(R.id.lv);

            assertNotNull("mLaunchActivity is null", getActivity());
            assertNotNull("mLaunchNextButton is null", parkingSpacesList);

        }
}
