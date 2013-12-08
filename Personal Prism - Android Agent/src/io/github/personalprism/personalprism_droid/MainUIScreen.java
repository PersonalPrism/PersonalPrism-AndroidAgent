package io.github.personalprism.personalprism_droid;

import io.github.personalprism.personalprism_droid.LocationSource.Mode;
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.TimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * MainUIScreen - Entry point for the Android Agent.
 *
 * @author Stuart Harvey (stu)
 * @author Hunter Morgan (kp1108)
 * @auther Michael Senoyuit (msenoyui)
 * @version 2013.12.7
 */
public class MainUIScreen
    extends Activity
{

    /** The Constant DEBUG. */
    public static final boolean DEBUG = true;

    /** The text. */
    TextView                    text;
    Button                      mapButton;

    // we'll actually skip this for the CS project. We only have 1 provider now
// /** The source manager. */
// private SourceManager sourceManager;

    private LocationSource      locationSource;

    /** The DB4O db filename. */
    public static String        DB4OFILENAME;


    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        DB4OFILENAME =
            getApplicationContext().getDir("prism_db4o", 0)
                + "/PersonalPrism.db4o";
        if (MainUIScreen.DEBUG)
            Log.d(getClass().getSimpleName(), "starting main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uiscreen);

        locationSource = new LocationSource(this, Mode.BACKGROUND);
//        sourceManager = new SourceManager(this);
    }


    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
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
     * end date then puts the data in the field
     *
     * @param v
     *            dateStop field's view
     */
    public void dateStartClick(View view)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final EditText text = (EditText)findViewById(R.id.editDateStart);
        DatePickerDialog mDatePicker;
        mDatePicker =
            new DatePickerDialog(view.getContext(), new OnDateSetListener() {
                public void onDateSet(
                    DatePicker datepicker,
                    int selectedyear,
                    int selectedmonth,
                    int selectedday)
                {
                    selectedmonth = selectedmonth + 1;
                    text.setText("" + selectedmonth + "/" + selectedday + "/"
                        + selectedyear);
                }
            }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }


    /**
     * runs when the dateStop field is clicked * opens a dialog box to get the
     * start date then puts the data in the field
     *
     * @param v
     *            dateStop field's view
     */
    public void dateStopClick(View view)
    {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        final EditText text = (EditText)findViewById(R.id.editDateStop);
        DatePickerDialog mDatePicker;
        mDatePicker =
            new DatePickerDialog(
                view.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(
                        DatePicker datepicker,
                        int selectedyear,
                        int selectedmonth,
                        int selectedday)
                    {
                        selectedmonth = selectedmonth + 1;
                        text.setText("" + selectedmonth + "/" + selectedday
                            + "/" + selectedyear);
                    }
                }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }


    /**
     * runs when the timeStart field is clicked * opens a dialog box to get the
     * start time then puts the data in the field
     *
     * @param v
     *            timeStart field's view
     */
    public void timeClickStart(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final EditText text = (EditText)findViewById(R.id.editTimeStart);
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
                    }
                }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


    /**
     * runs when the timeStop field is clicked * opens a dialog box to get the
     * end time then puts the data in the field
     *
     * @param v
     *            timeStop field's view
     */
    public void timeClickStop(View v)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final EditText text = (EditText)findViewById(R.id.editTimeStop);
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
                    }
                }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    /**
     * method called when the 'show map' button is pressed
     *
     * @param v
     *            the view of the button that was pressed
     */
    public void getMap(View v)
    {
        Intent myIntent = new Intent(getApplicationContext(), MapView.class);
        MainUIScreen.this.startActivity(myIntent);
    }


    /**
     * method called when the 'get history' button is pressed
     *
     * @param v
     *            the view of the button that was pressed
     */
    public void getHistory(View v)
    {

// Intent myIntent = new Intent(getApplicationContext(),
// AnimatedHistoricView.class);
// MainUIScreen.this.startActivity(myIntent);

    }


    /**
     * method called when the data collect check box is clicked
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

}
