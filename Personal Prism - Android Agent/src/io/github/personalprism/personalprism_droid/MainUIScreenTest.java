package io.github.personalprism.personalprism_droid;

import java.util.Calendar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.*;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author Michael Senoyuit
 * @version Dec 8, 2013
 */

public class MainUIScreenTest
    extends student.AndroidTestCase<MainUIScreen>
{

    MainUIScreen     screen;
    private CheckBox checkBox1;
    private TextView editTimeStart;
    private TextView editTimeStop;
    private TextView editDateStart;
    private TextView editDateStop;
    private EditText editCatchRate;
    private Button   liveMapButton;
    private Button   dateRangeButton;
    private Button   historyButton;

    public MainUIScreenTest()
    {
        super(MainUIScreen.class);
        // TODO Auto-generated constructor stub
    }

//    class ContextWrapper extends Context {
//        public void setContext(Context newContext) {
//
//        }
//    }

    // ----------------------------------------------------------
    protected void setUp()
        throws Exception
    {
        super.setUp();
        screen = this.getActivity();

    }





    /**
     * Test method for dateclick
     */
    public void testDateClick()
    {
       // prepareForUpcomingActivity(Intent.ACTION_VIEW);
  //      click(editDateStart);
    //    assertTrue(screen.mDatePicker.isShowing());
    //    screen.mDatePicker.dismiss();
        click(editDateStop);
        screen.mDatePicker.onDateChanged(null, 2000, 3, 1);
        screen.mDatePicker.dismiss();
        assertEquals(2000, screen.stopTime.get(Calendar.YEAR));
        assertEquals(3, screen.stopTime.get(Calendar.MONTH));
        assertEquals(1, screen.stopTime.get(Calendar.DAY_OF_MONTH));

        click(editDateStart);
        screen.mDatePicker.onDateChanged(null, 2008, 5, 2);
        assertEquals(2008, screen.stopTime.get(Calendar.YEAR));
        assertEquals(5, screen.stopTime.get(Calendar.MONTH));
        assertEquals(2, screen.stopTime.get(Calendar.DAY_OF_MONTH));
        click(editDateStop);

        screen.mDatePicker.onDateChanged(null, 2007, 5, 1);
        screen.mDatePicker.dismiss();
        assertEquals(2008, screen.startTime.get(Calendar.YEAR));
        assertEquals(5, screen.startTime.get(Calendar.MONTH));
        assertEquals(2, screen.startTime.get(Calendar.DAY_OF_MONTH));
        screen.mDatePicker.dismiss();
    }


    /**
     * Test method for timeclick
     */
    public void testTimeClick()
    {
        //prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(editTimeStop);
        screen.mTimePicker.onTimeChanged(null, 19, 10);
        assertEquals(19, screen.stopTime.get(Calendar.HOUR_OF_DAY));
        assertEquals(10, screen.stopTime.get(Calendar.MINUTE));
        screen.mTimePicker.dismiss();
        click(editTimeStart);
        screen.mTimePicker.
        screen.mTimePicker.onTimeChanged(null, 1, 56);
        assertEquals(1, screen.startTime.get(Calendar.HOUR_OF_DAY));
        assertEquals(56, screen.startTime.get(Calendar.MINUTE));

    }




    /**
     * Test method for getdatarange
     */




    /**
     * Test method for testcollectDataToggle
     */
    public void testCollectDataToggle()
    {
      //  prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(checkBox1);
        assertTrue(checkBox1.isChecked());

      //  prepareForUpcomingActivity(Intent.ACTION_VIEW);
        click(checkBox1);
        assertFalse(checkBox1.isChecked());
    }



}
