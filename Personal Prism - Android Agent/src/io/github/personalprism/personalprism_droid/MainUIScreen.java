package io.github.personalprism.personalprism_droid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainUIScreen
    extends Activity
// implements Observer
{

    public static final boolean DEBUG = true;
	TextView              text;
    private SourceManager sourceManager;
    public static String DB4OFILENAME;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        DB4OFILENAME = getApplicationContext().getDir("prism_db4o", 0) + "/PersonalPrism.db4o";
        if (MainUIScreen.DEBUG) Log.d(getClass().getSimpleName(), "starting main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uiscreen);

        text = (TextView)findViewById(R.id.text); //in case we want to output

        sourceManager = new SourceManager(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_uiscreen, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
    	// probably works for service availability check
    	super.onResume();
    	int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
    	if (available != ConnectionResult.SUCCESS)
    		GooglePlayServicesUtil.getErrorDialog(available, getParent(), 0);
    }



}
