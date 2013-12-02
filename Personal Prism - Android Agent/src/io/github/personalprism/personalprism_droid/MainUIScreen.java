package io.github.personalprism.personalprism_droid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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

		text = (TextView) findViewById(R.id.text); // in case we want to output

		sourceManager = new SourceManager(this);

		mapButton = (Button) findViewById(R.id.mapButton);
		
		mapButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), MapView.class);
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

//	/**
//	 * Create a map view.
//	 */
//	public void mapButtonClicked() {
//		Intent myIntent = new Intent(getApplicationContext(), MapView.class);
//		this.startActivity(myIntent);
//	}
	


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

}
