package io.github.personalprism.personalprism_droid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import io.github.personalprism.personalprism_droid.LocationSource.Mode;
import java.util.ArrayList;
import java.util.Calendar;
import sohail.aziz.service.MyResultReceiver;

// TODO: Auto-generated Javadoc
/**
 * MainUIScreen - Entry point for the Android Agent.
 *
 * @author Stuart Harvey (stu)
 * @author Hunter Morgan (kp1108) <automaticgiant@gmail.com>
 * @auther Michael Senoyuit (msenoyui)
 * @version 2013.12.8
 */
public class MainUIScreen
    extends Activity
    implements sohail.aziz.service.MyResultReceiver.Receiver
{

    /** The start time calendar . */
    Calendar                    startTime;

    /** The stop time calendar. */
    Calendar                    stopTime;

    /** The Constant DEBUG. */
    public static final boolean DEBUG = true;

    // we'll actually skip this for the CS project. We only have 1 provider now
// /** The source manager. */
// private SourceManager sourceManager;

    /** The location source. */
    private LocationSource      locationSource;

    /** The receiver. */
    private MyResultReceiver    receiver;

    /** The DB4O db filename. */
    public static String        db4oFilename;

    /** The sample rate. */
    private EditText            sampleRate;


    // ----------------------------------------------------------
    /**
     * On create.
     *
     * @param savedInstanceState
     *            the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        startTime = Calendar.getInstance();
        stopTime = Calendar.getInstance();

        db4oFilename =
            getApplicationContext().getDir("prism_db4o", 0)
                + "/PersonalPrism.db4o";

        if (MainUIScreen.DEBUG)
            Log.d(getClass().getSimpleName(), "starting main activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uiscreen);

        receiver = new MyResultReceiver(new Handler());
        receiver.setReceiver(this);

        locationSource = new LocationSource(this, Mode.BACKGROUND);

        sampleRate = (EditText)findViewById(R.id.editCatchRate);
        sampleRate.setText(String.valueOf(locationSource
            .getUpdateNominalInterval() / 1000));
        sampleRate.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(
                TextView v,
                int actionId,
                KeyEvent event)
            {
                int interval = Integer.parseInt(v.getText().toString()) * 1000;
                if (interval > locationSource.getUpdateFastestInterval())
                    locationSource.setUpdateNominalInterval(interval);
                if (locationSource.isEnabled())
                    locationSource.restart();
                return true;
            }
        });
// sourceManager = new SourceManager(this);
    }


    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    // ----------------------------------------------------------
    /**
     * On create options menu.
     *
     * @param menu
     *            the menu
     * @return true, if successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_uiscreen, menu);
        return true;
    }


    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    // ----------------------------------------------------------
    /**
     * On resume.
     */
    @Override
    protected void onResume()
    {
        // probably works for service availability check
        super.onResume();
        int available =
            GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (available != ConnectionResult.SUCCESS)
            GooglePlayServicesUtil.getErrorDialog(available, getParent(), 0);
    }


    /**
     * runs when the dateStart field is clicked opens a dialog box to get the
     * end date then puts the data in the field.
     *
     * @param view
     *            the view
     */
    public void dateClick(View view)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final TextView text = (TextView)view;
        DatePickerDialog mDatePicker;
        mDatePicker =
            new DatePickerDialog(view.getContext(), new OnDateSetListener() {
                public void onDateSet(
                    DatePicker datepicker,
                    int selectedyear,
                    int selectedmonth,
                    int selectedday)
                {

                    // selectedmonth = selectedmonth + 1;
                    text.setText("" + (selectedmonth + 1) + "/" + selectedday
                        + "/" + selectedyear);
                    setCalendar(text, selectedday, selectedmonth, selectedyear);
                    timeMatchCheck(text);
                }
            },
                mYear,
                mMonth,
                mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }


    /**
     * runs when the dateStop field is clicked * opens a dialog box to get the
     * start date then puts the data in the field.
     *
     * @param v
     *            dateStop field's view public void dateStopClick(View view) {
     *            Calendar mcurrentDate = Calendar.getInstance(); int mYear =
     *            mcurrentDate.get(Calendar.YEAR); int mMonth =
     *            mcurrentDate.get(Calendar.MONTH); int mDay =
     *            mcurrentDate.get(Calendar.DAY_OF_MONTH); final EditText text =
     *            (EditText)findViewById(R.id.editDateStop); DatePickerDialog
     *            mDatePicker; mDatePicker = new DatePickerDialog(
     *            view.getContext(), new DatePickerDialog.OnDateSetListener() {
     *            public void onDateSet( DatePicker datepicker, int
     *            selectedyear, int selectedmonth, int selectedday) {
     *            selectedmonth = selectedmonth + 1; text.setText("" +
     *            selectedmonth + "/" + selectedday + "/" + selectedyear); } },
     *            mYear, mMonth, mDay); mDatePicker.setTitle("Select Date");
     *            mDatePicker.show(); }
     */
    /**
     * runs when the timeStart field is clicked * opens a dialog box to get the
     * start time then puts the data in the field
     *
     * @param v
     *            timeStart field's view
     */
    public void timeClick(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TextView text = (TextView)v;
        TimePickerDialog mTimePicker;
        mTimePicker =
            new TimePickerDialog(
                v.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(
                        TimePicker timePicker,
                        int selectedHour,
                        int selectedMinute)
                    {
                        text.setText("" + selectedHour + ":" + selectedMinute);
                        setCalendar(text, selectedHour, selectedMinute);
                        timeMatchCheck(text);
                    }
                }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    /**
     * runs when the timeStop field is clicked * opens a dialog box to get the
     * end time then puts the data in the field.
     *
     * @param v
     *            timeStop field's view public void timeClickStop(View v) {
     *            Calendar mcurrentTime = Calendar.getInstance(); int hour =
     *            mcurrentTime.get(Calendar.HOUR_OF_DAY); int minute =
     *            mcurrentTime.get(Calendar.MINUTE); final EditText text =
     *            (EditText)findViewById(R.id.editTimeStop); TimePickerDialog
     *            mTimePicker; mTimePicker = new TimePickerDialog(
     *            v.getContext(), new TimePickerDialog.OnTimeSetListener() {
     * @return the live map
     * @Override public void onTimeSet( TimePicker timePicker, int selectedHour,
     *           int selectedMinute) { text.setText("" + selectedHour + ":" +
     *           selectedMinute); } }, hour, minute, false);
     *           mTimePicker.setTitle("Select Time"); mTimePicker.show(); }
     */

    /**
     * this is the listener for the live map button.
     *
     * @param v
     *            the view of the button that was pressed
     */
    public void getLiveMap(View v)
    {
        Intent myIntent = new Intent(getApplicationContext(), MapView.class);
        MainUIScreen.this.startActivity(myIntent);
    }


    /**
     * starts historic view for all data points.
     *
     * @param v
     *            the v return the history
     *
     */
    public void getHistory(View v)
    {
        Intent intent = DbHandler.locationQueryAllMaker(receiver);
        startService(intent);
    }


    /**
     * method called when the 'get history' button is pressed. his is just last
     * minute until we hook up the date/time controls. I don't know how they
     * work.
     *
     * @param v
     *            the view of the button that was pressed
     *
     */
    public void getDateRange(View v)
    {
        Intent intent =
            DbHandler.locationQueryByDateMaker(
                startTime.getTime(),
                stopTime.getTime(),
                receiver);
        startService(intent);
    }


    /**
     * This is the callback from the ResultReciever helper class. In this class,
     * we are only using it to get search results from DbHandler and pass them
     * to the historic view, so we don't need to determine where the results
     * came from, just shove them in a map.
     *
     * @param resultCode
     *            this will be the DbHandler.DBHANDLER_QUERY_ID that went with
     *            the query, if any
     * @param resultData
     *            this will be the arraylist in the
     *            DbHandler.DBHANDLER_LOCATION_RESULTS extra
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        ArrayList<Location> results =
            resultData
                .getParcelableArrayList(DbHandler.DBHANDLER_LOCATION_RESULTS);

        Intent myIntent =
            new Intent(getApplicationContext(), AnimatedHistoricView.class);
        myIntent.putExtra(DbHandler.DBHANDLER_LOCATION_RESULTS, results);
        MainUIScreen.this.startActivity(myIntent);
    }


    /**
     * method called when the data collect check box is clicked.
     *
     * @param v
     *            the view of the check box
     */
    public void collectDataToggle(View v)
    {
        CheckBox check = (CheckBox)v;
        if (check.isChecked())
        {
            // do start stuff here
// sourceManager.getLocationSource().enable();
            locationSource.start();
        }
        else
        {
            // do stop stuff here
// sourceManager.getLocationSource().disable();
            locationSource.stop();
        }
    }


    /**
     * Sets the calendar.
     *
     * @param view
     *            the view
     * @param day
     *            the day
     * @param month
     *            the month
     * @param year
     *            the year
     */
    private void setCalendar(TextView view, int day, int month, int year)
    {
        if (view == (TextView)findViewById(R.id.editDateStart))
        {
            startTime.set(year, month, day);
        }
        else
        {
            stopTime.set(year, month, day);
        }
    }


    /**
     * Sets the calendar.
     *
     * @param view
     *            the view
     * @param hour
     *            the hour
     * @param min
     *            the min
     */
    private void setCalendar(View view, int hour, int min)
    {
        if (view == (TextView)findViewById(R.id.editDateStart))
        {
            startTime.set(Calendar.HOUR, hour);
            startTime.set(Calendar.MINUTE, min);
        }
        else
        {
            stopTime.set(Calendar.HOUR, hour);
            stopTime.set(Calendar.MINUTE, min);
        }
    }


    /**
     * Time match check.
     *
     * @param view
     *            the view
     */
    private void timeMatchCheck(TextView view)
    {
        TextView editDateStop = (TextView)findViewById(R.id.editDateStop);
        TextView editDateStart = (TextView)findViewById(R.id.editDateStart);
        TextView editTimeStop = (TextView)findViewById(R.id.editTimeStop);
        TextView editTimeStart = (TextView)findViewById(R.id.editTimeStart);
        if (startTime.after(stopTime))
        {
            if (view == editDateStart || view == editTimeStart)
            {
                stopTime = (Calendar)startTime.clone();

                editDateStop.setText(editDateStart.getText());
                editTimeStop.setText(editTimeStart.getText());
            }
            else
            {
                startTime = (Calendar)startTime.clone();

                editDateStart.setText(editDateStop.getText());
                editTimeStart.setText(editTimeStop.getText());
            }

        }
    }

}
