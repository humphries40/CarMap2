package com.example.connor.carmap;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
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
//            assertNotNull("mLaunchActivity is null", getActivity());
  //          assertNotNull("mLaunchNextButton is null", launchNextButton);

        }
    }
}
