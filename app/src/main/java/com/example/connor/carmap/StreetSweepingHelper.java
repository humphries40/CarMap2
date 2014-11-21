package com.example.connor.carmap;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by palmek4 on 11/21/2014.
 */
public class StreetSweepingHelper {

    public StreetSweepingHelper () {

    }

    public Date getNextThursdayStreetSweeping () {

        Calendar c = Calendar.getInstance();
        Date current = c.getTime();
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        if (month >= Calendar.NOVEMBER) {
            c.set(year +1, Calendar.APRIL, 8);//set the date to the first of april of the next year
        } else if (month < Calendar.APRIL){
            c.set(year, Calendar.APRIL, 8);//set the date to the first of april
        } else {
            c.set(Calendar.DAY_OF_MONTH, 8);//else set the date to the first of the current month
        }

        while ((c.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)){
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        Date next = c.getTime();

        if (current.after(next)) {
            c.add(Calendar.MONTH, 1);//go to the next month
            c.set(Calendar.DAY_OF_MONTH,8);//else set the date to the first of the next month
            while ((c.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY)){
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            next = c.getTime();
        }
        return next;
    }

    public Date getNextFridayStreetSweeping () {

        Calendar c = Calendar.getInstance();
        Date current = c.getTime();
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        if (month >= Calendar.NOVEMBER) {
            c.set(year +1, Calendar.APRIL, 8);//set the date to the first of april of the next year
        } else if (month < Calendar.APRIL){
            c.set(year, Calendar.APRIL, 8);//set the date to the first of april
        } else {
            c.set(Calendar.DAY_OF_MONTH,8);//else set the date to the first of the current month
        }

        while ((c.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY)){
                c.add(Calendar.DAY_OF_MONTH, 1);
        }

        Date next = c.getTime();

        if (current.after(next)) {
            c.add(Calendar.MONTH, 1);//go to the next month
            c.set(Calendar.DAY_OF_MONTH, 8);//else set the date to the first of the next month
            while ((c.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY)){
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            next = c.getTime();
        }
        return next;
    }
}
