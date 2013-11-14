package io.github.personalprism.personalprism_droid;

import android.util.Log;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainUIScreen
    extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(getPackageName(), "starting main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uiscreen);
        Intent serviceIntent = new Intent(getApplicationContext(), DbHandler.class);
//        serviceIntent.setComponent(new ComponentName(getApplicationContext(), DbHandler.class));
        ComponentName component = getApplicationContext().startService(serviceIntent);
        if (component == null) Log.e(getPackageName(), "service failed to start fail");
        else Log.d(getPackageName(), component.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_uiscreen, menu);
        return true;
    }

}
