package io.github.personalprism.personalprism_droid;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
 * @author Hunter Morgan <kp1108>
 * @version 2013.12.01
 */
public class MainUIScreen extends Activity {

	/** The Constant DEBUG. */
	public static final boolean DEBUG = true;

	/** The text. */
	TextView text;
	Button mapButton;

	/** The source manager. */
	private SourceManager sourceManager;

	/** The DB4O db filename. */
	public static String DB4OFILENAME;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DB4OFILENAME = getApplicationContext().getDir("prism_db4o", 0)
				+ "/PersonalPrism.db4o";
		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "starting main activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_uiscreen);

		// text = (TextView) findViewById(R.id.text); // in case we want to
		// output

		sourceManager = new SourceManager(this);

		mapButton = (Button) findViewById(R.id.mapButton);

		mapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(),
						MapView.class);
				MainUIScreen.this.startActivity(myIntent);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_uiscreen, menu);
		return true;
	}

	// /**
	// * Create a map view.
	// */
	// public void mapButtonClicked() {
	// Intent myIntent = new Intent(getApplicationContext(), MapView.class);
	// this.startActivity(myIntent);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// probably works for service availability check
		super.onResume();
		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (available != ConnectionResult.SUCCESS)
			GooglePlayServicesUtil.getErrorDialog(available, getParent(), 0);
	}

	public void dateStartClick(View view) {
		// TODO Auto-generated method stub
		// To show current date in the datepicker
		Calendar mcurrentDate = Calendar.getInstance();
		int mYear = mcurrentDate.get(Calendar.YEAR);
		int mMonth = mcurrentDate.get(Calendar.MONTH);
		int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
		final TextView text = (EditText) findViewById(R.id.editDateStart);
		DatePickerDialog mDatePicker;
		mDatePicker = new DatePickerDialog(view.getContext(),
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker datepicker,
							int selectedyear, int selectedmonth, int selectedday) {
						// TODO Auto-generated method stub
						/* Your code to get date and time */
						selectedmonth = selectedmonth + 1;
						text.setText("" + selectedday + "/" + selectedmonth
								+ "/" + selectedyear);
					}
				}, mYear, mMonth, mDay);
		mDatePicker.setTitle("Select Date");
		mDatePicker.show();
	}

	public void dateStopClick(View view) {
		// TODO Auto-generated method stub
		// To show current date in the datepicker
		Calendar mcurrentDate = Calendar.getInstance();
		int mYear = mcurrentDate.get(Calendar.YEAR);
		int mMonth = mcurrentDate.get(Calendar.MONTH);
		int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
		final EditText text = (EditText) findViewById(R.id.editDateStop);
		DatePickerDialog mDatePicker;
		mDatePicker = new DatePickerDialog(view.getContext(),
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker datepicker,
							int selectedyear, int selectedmonth, int selectedday) {
						// TODO Auto-generated method stub
						/* Your code to get date and time */
						selectedmonth = selectedmonth + 1;
						text.setText("" + selectedday + "/" + selectedmonth
								+ "/" + selectedyear);
					}
				}, mYear, mMonth, mDay);
		mDatePicker.setTitle("Select Date");
		mDatePicker.show();
	}

	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.editDateStart:
			if (hasFocus) {
				Calendar mcurrentDate = Calendar.getInstance();
				int mYear = mcurrentDate.get(Calendar.YEAR);
				int mMonth = mcurrentDate.get(Calendar.MONTH);
				int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
				final EditText text = (EditText) findViewById(R.id.editDateStart);
				DatePickerDialog mDatePicker;
				mDatePicker = new DatePickerDialog(v.getContext(),
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker datepicker,
									int selectedyear, int selectedmonth, int selectedday) {
								// TODO Auto-generated method stub
								/* Your code to get date and time */
								selectedmonth = selectedmonth + 1;
								text.setText("" + selectedday + "/" + selectedmonth
										+ "/" + selectedyear);
							}
						}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Select Date");
				mDatePicker.show();
			}
			break;
		case R.id.editDateStop:
			if (hasFocus) {
				Calendar mcurrentDate = Calendar.getInstance();
				int mYear = mcurrentDate.get(Calendar.YEAR);
				int mMonth = mcurrentDate.get(Calendar.MONTH);
				int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
				final EditText text = (EditText) findViewById(R.id.editDateStop);
				DatePickerDialog mDatePicker;
				mDatePicker = new DatePickerDialog(v.getContext(),
						new DatePickerDialog.OnDateSetListener() {
							public void onDateSet(DatePicker datepicker,
									int selectedyear, int selectedmonth, int selectedday) {
								// TODO Auto-generated method stub
								/* Your code to get date and time */
								selectedmonth = selectedmonth + 1;
								text.setText("" + selectedmonth + "/" + selectedday
										+ "/" + selectedyear);
							}
						}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Select Date");
				mDatePicker.show();
			}
			break;
			
		case R.id.editTimeStart:
			if (hasFocus) {
			 Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            final EditText text = (EditText) findViewById(R.id.editTimeStart);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
	               
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                    text.setText("" + selectedHour + ":" + selectedMinute);
	                }

					
	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Time");
	            mTimePicker.show();
			}
			break;
			
		case R.id.editTimeStop:
			//if (hasFocus) {
			 Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            final EditText text = (EditText) findViewById(R.id.editTimeStart);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
	               
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                    text.setText("" + selectedHour + ":" + selectedMinute);
	                }

					
	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Time");
	            mTimePicker.show();
			//}

		default:
			break;
		}

	}

}
