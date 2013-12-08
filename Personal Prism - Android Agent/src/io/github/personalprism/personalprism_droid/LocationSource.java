package io.github.personalprism.personalprism_droid;

import android.widget.Toast;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.Observable;

/**
 * Handles the constant gathering of location data. Create from a service or
 * activity context by calling: new
 * LocationService(this.getApplicationContext()); Your source must implement
 * Observer architecture, and after instantiating LocationService, call
 * locationService.addObserver(this); You will receive location updates in your
 * implemented update method.
 *
 * @author Stuart Harvey (stu)
 * @author Hunter Morgan <kp1108>
 * @version 2013.12.01
 */
public class LocationSource
    extends Observable
    implements ConnectionCallbacks, OnConnectionFailedListener,
    LocationListener, PrismDataSource
{
    private   int updateFastestInterval = 10000;
    private   int updateNominalInterval = 20 * 1000;
    private   int updatePriority = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private LocationRequest mLocationRequest;
    private LocationClient  mLocationClient;
    private PendingIntent   callbackIntent;
    private Mode mMode;
    private Context context;
    public static final String COMPONENT_NAME = "Location";

    public static enum Mode { OBSERVABLE, BACKGROUND};



    private void configureRequester()
    {
        // create a new location requester, set priority and request interval
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(updatePriority);
        mLocationRequest.setInterval(updateNominalInterval);
        mLocationRequest.setFastestInterval(updateFastestInterval);
    }

    public LocationSource(Context context, Mode opMode)
    {
        // create the location client
        mLocationClient = new LocationClient(context, this, this);
    }

    public void reconfigure()
    {
        disable();
        configureRequester();
        enable();
    }


    /**
     * Google Play Services callback. Implementation detail. Called when the phone's location updates. (DO NOT CALL)
     *
     * @param location
     *            the new location data.
     */
    @Override
    public void onLocationChanged(Location location)
    {
        setChanged();
        notifyObservers(location);
    }


    /**
     * Google Play Services callback. Implementation detail. If the connection to google maps fails, handle it appropriately. (DO NOT
     * CALL)
     *
     * @param result
     *            the result of the failed connection.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        String message = "Could not connect to Google Play Services.";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.e(getClass().getSimpleName(), message);
        disable();
    }


    /**
     * Google Play Services callback. Implementation detail. Called
     * automatically when the client successfully connects (DO NOT CALL).
     *
     * @param connectionHint
     *            the data bundle for the connection.
     */
    @Override
    public void onConnected(Bundle connectionHint)
    {
        String message = "Connected to Google Play Services.";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), message);
        }
        switch (mMode)
        {
            case BACKGROUND:
                Intent notificationIntent = new Intent(context, DbHandler.class);
                callbackIntent =
                    PendingIntent.getService(context, 0, notificationIntent, 0);
                mLocationClient.requestLocationUpdates(mLocationRequest, callbackIntent);
                break;
            case OBSERVABLE:
                mLocationClient.requestLocationUpdates(mLocationRequest, this);
                break;
            default:
                break;
        }
    }


    /**
     * Google Play Services callback. Implementation detail. What to do when the location client disconnects (DO NOT CALL).
     */
    @Override
    public void onDisconnected()
    {
        String message = "Disconnected from Google Play Services unexpectedly.";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.e(getClass().getSimpleName(), message);
        disable();
    }


    /**
     * Stops requests to the location.
     */
    public void enable()
    {
        configureRequester();
        mLocationClient.connect();
    }

    public void disable()
    {
        switch (mMode)
        {
            case BACKGROUND:
                mLocationClient.removeLocationUpdates(callbackIntent);
                break;
            case OBSERVABLE:
                mLocationClient.removeLocationUpdates(this);
                break;
            default:
                break;
        }
        mLocationClient.disconnect();
    }

}
