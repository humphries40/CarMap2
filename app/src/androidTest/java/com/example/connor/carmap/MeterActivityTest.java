package com.example.connor.carmap;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Connor on 12/1/2014.
 */
public class MeterActivityTest extends ActivityUnitTestCase<MeterActivity>{

      private Intent mLaunchIntent;

      public MeterActivityTest() {
          super(MeterActivity.class);
      }


      @Override
      protected void setUp() throws Exception {
            super.setUp();
            //Create an intent to launch target Activity
            mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
                    MeterActivity.class);
        }

        /**
         * Tests the preconditions of this test fixture.
         */
        @MediumTest
        public void testPreconditions() {
            //Start the activity under test in isolation, without values for savedInstanceState and
            //lastNonConfigurationInstance
            startActivity(mLaunchIntent, null, null);
            final Button startTimerButton = (Button) getActivity().findViewById(R.id.startTimer);
            final Button stopTimeButton = (Button) getActivity().findViewById(R.id.stopTimer);
            final Button resetTimerButton = (Button) getActivity().findViewById(R.id.resetTimer);

            assertNotNull("mLaunchActivity is null", getActivity());
            assertNotNull("startTimerButton is null", startTimerButton);
            assertNotNull("stopTimeButton is null", stopTimeButton);
            assertNotNull("resetTimerButton is null", resetTimerButton);

        }

        @MediumTest
        public void testCountDownWasStartedOnClick() {
            startActivity(mLaunchIntent, null, null);
            final Button startTimerButton = (Button) getActivity().findViewById(R.id.startTimer);
            final EditText meterMin = (EditText) getActivity().findViewById(R.id.meterMins);
            meterMin.setText("10");
            final EditText meterHr = (EditText) getActivity().findViewById(R.id.meterHrs);
            meterHr.setText("10");

            //Because this is an isolated ActivityUnitTestCase we have to directly click the
            //button from code
            startTimerButton.performClick();


            //Verify the countdown Timer was not null.
            assertNotNull("Timer was null", getActivity().timer);
            //Verify that LaunchActivity was finished after button click
        }

        @MediumTest
        public void testCountDownWasStoppedOnClick() {
            startActivity(mLaunchIntent, null, null);
            final Button startTimerButton = (Button) getActivity().findViewById(R.id.startTimer);
            final Button stopTimerButton = (Button) getActivity().findViewById(R.id.stopTimer);
            final EditText meterMin = (EditText) getActivity().findViewById(R.id.meterMins);
            meterMin.setText("10");
            final EditText meterHr = (EditText) getActivity().findViewById(R.id.meterHrs);
            meterHr.setText("10");

            //Because this is an isolated ActivityUnitTestCase we have to directly click the
            //button from code
            startTimerButton.performClick();
            stopTimerButton.performClick();


            //Verify the countdown Timer was not null.
            assertNotNull("Timer was null", getActivity().timer);
            String timeCanceled = "Meter Canceled";
            final TextView time = (TextView) getActivity().findViewById(R.id.meterTime);
            assertTrue("Timer wasn't cancelled", 0==timeCanceled.compareTo((String)time.getText()));

        }

        @MediumTest
        public void testCountDownWasResetOnClick() {
            startActivity(mLaunchIntent, null, null);
            final Button startTimerButton = (Button) getActivity().findViewById(R.id.startTimer);
            final Button resetTimerButton = (Button) getActivity().findViewById(R.id.resetTimer);
            final EditText meterMin = (EditText) getActivity().findViewById(R.id.meterMins);
            meterMin.setText("10");
            final EditText meterHr = (EditText) getActivity().findViewById(R.id.meterHrs);
            meterHr.setText("10");

            //Because this is an isolated ActivityUnitTestCase we have to directly click the
            //button from code
            startTimerButton.performClick();
            resetTimerButton.performClick();



            //Verify the countdown Timer was not null.
            assertNotNull("Timer was null", getActivity().timer);
            String timeCanceled = "No time on Meter";
            final TextView time = (TextView) getActivity().findViewById(R.id.meterTime);
            assertTrue("Timer wasn't reset", 0==timeCanceled.compareTo((String)time.getText()));

        }
    }

