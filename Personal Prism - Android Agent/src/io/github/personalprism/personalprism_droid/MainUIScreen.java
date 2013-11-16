package io.github.personalprism.personalprism_droid;

import sofia.app.Screen;
import android.util.Log;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author Stuart Harvey (stu)
 *  @version 2013.11.15
 */
public class MainUIScreen
    extends Screen
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(getPackageName(), "starting main activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_uiscreen);
        
        // attempt add db service
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
    
    /**
     * Create a map view.
     */
    public void mapButtonClicked()
    {
        Intent myIntent = new Intent(getApplicationContext(), MapView.class);
        this.startActivity(myIntent);
    }
}
