package io.github.personalprism.personalprism_droid;

import java.util.Observable;
import java.util.Observer;
import android.util.Log;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Hunter on 11/13/13.
 */
public class DbHandler
    extends Service
    implements Observer
{

    private LocationSource locationSource;
    public LocationSource getLocationSource()
    {
        return locationSource;
    }


    public IBinder onBind(Intent intent)
    {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(getPackageName(), "starting service");
//        new LocationSource(getApplicationContext()).addObserver(this);
        locationSource = new LocationSource(getApplicationContext());
        locationSource.addObserver(this);

        return Service.START_NOT_STICKY;
    }


    @Override
    public void onDestroy()
    {
        Log.d(getPackageName(), "stopping service");
        super.onDestroy();
    }


    @Override
    public void update(Observable observable, Object data)
    {
        Log.d(getPackageName(), data.toString());
    }
}
