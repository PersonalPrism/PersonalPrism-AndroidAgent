package io.github.personalprism.personalprism_droid;

import java.util.ArrayList;
import android.location.Location;
import android.os.Handler;
import sohail.aziz.service.MyResultReceiver;
import java.util.Date;
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
public class MainUIScreen
    extends Activity
    implements sohail.aziz.service.MyResultReceiver.Receiver
{

    protected sohail.aziz.service.MyResultReceiver receiver;

	/** The Constant DEBUG. */
	public static final boolean DEBUG = true;

    /** The Constant PKGNAME. */
    public static final String PKGNAME = "io.github.personalprism.personalprism_droid";

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

		receiver = new MyResultReceiver(new Handler());
		receiver.setReceiver(this);

		Button testDateSearchButton = (Button) findViewById(R.id.testDateSearch);
		testDateSearchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Intent intent = DbHandler.locationQueryByDateMaker(new Date(
                    new Date().getTime() - 60000
                    ), new Date(), receiver);
                startService(intent);
            }
        });
	}


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData)
    {
        ArrayList<Location> results = resultData.getParcelableArrayList(DbHandler.DBHANDLER_LOCATION_RESULTS);
        StringBuilder string = new StringBuilder();
        string.append(resultData.keySet().toString());
        if (results != null)
            {
            string.append("got " + results.size());

            if (results.size() != 0)
            {
            string.append(" results: ");
        if (results.get(0) != null) string.append(results.toString());
            //text.setText(results.length + " results: " + Arrays.toString(results));
        }}

        text.setText(string.toString());
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
