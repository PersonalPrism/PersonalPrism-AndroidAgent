package io.github.personalprism.personalprism_droid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.Observable;

// TODO: Auto-generated Javadoc
/**
 * This is a PrismDataSource for Google Play Services location updates. It can
 * run in Observable mode or background update mode. Start and stop with start()
 * and stop(). It is hard-coded to send background updates to DbHandler in
 * background mode. As an observable, just add an observer and you're good to
 * go. To reconfigure, just modify the settings and run restart().
 *
 * @author Stuart Harvey (stu)
 * @author Hunter Morgan <kp1108>
 * @version 2013.12.07
 */
public class LocationSource
    extends Observable
    implements ConnectionCallbacks, OnConnectionFailedListener,
    LocationListener, PrismDataSource
{
    // these are default settings for the updates
    /** The update fastest interval. */
    private int             updateFastestInterval = 10000;

    /** The update nominal interval. */
    private int             updateNominalInterval = 20 * 1000;

    /** The update priority. */
    private int             updatePriority        =
                                                      LocationRequest.PRIORITY_HIGH_ACCURACY;

    /** The m location request. */
    private LocationRequest mLocationRequest;

    /** The m location client. */
    private LocationClient  mLocationClient;

    /** The callback intent. */
    private PendingIntent   callbackIntent;

    /** The m mode. */
    private Mode            mMode;

    /** The m context. */
    private Context         mContext;


    /**
     * The Enum Mode.
     */
    public static enum Mode
    {

        /** The observable. */
        OBSERVABLE,

        /** The background. */
        BACKGROUND
    };


    /**
     * Configure requester.
     */
    private void configureRequester()
    {
        // create a new location requester, set priority and request interval
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(updatePriority);
        mLocationRequest.setInterval(updateNominalInterval);
        mLocationRequest.setFastestInterval(updateFastestInterval);
    }


    /**
     * Instantiates a new location source.
     *
     * @param context
     *            the context
     * @param opMode
     *            the op mode
     */
    public LocationSource(Context context, Mode opMode)
    {
        mMode = opMode;
        mContext = context;
        // create the location client
        mLocationClient = new LocationClient(context, this, this);
    }


    /**
     * Google Play Services callback. Implementation detail. Called when the
     * phone's location updates. (DO NOT CALL)
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
     * Google Play Services callback. Implementation detail. If the connection
     * to google maps fails, handle it appropriately. (DO NOT CALL)
     *
     * @param result
     *            the result of the failed connection.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        String message =
            "LocationSource could not connect to Google Play Services.";
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Log.e(getClass().getSimpleName(), message);
        stop();
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
        String message = "Connected LocationSource to Google Play Services.";
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), message);
        }
        switch (mMode)
        {
            case BACKGROUND:
                Intent notificationIntent =
                    new Intent(mContext, DbHandler.class);
                callbackIntent =
                    PendingIntent
                        .getService(mContext, 0, notificationIntent, 0);
                mLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    callbackIntent);
                break;
            case OBSERVABLE:
                mLocationClient.requestLocationUpdates(mLocationRequest, this);
                break;
            default:
                break;
        }
    }


    /**
     * Google Play Services callback. Implementation detail. What to do when the
     * location client disconnects (DO NOT CALL).
     */
    @Override
    public void onDisconnected()
    {
        String message =
            "LocationSource disconnected from Google Play Services unexpectedly.";
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Log.e(getClass().getSimpleName(), message);
        stop();
    }


    @Override
    public void start()
    {
        configureRequester();
        mLocationClient.connect();
    }


    // ----------------------------------------------------------
    @Override
    public void stop()
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
        String message =
            "Disconnected LocationSource from Google Play Services.";
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), message);
        }
    }


    /**
     * Gets the update fastest interval.
     *
     * @return the update fastest interval
     */
    public int getUpdateFastestInterval()
    {
        return updateFastestInterval;
    }


    /**
     * Sets the update fastest interval.
     *
     * @param updateFastestInterval
     *            the new update fastest interval
     */
    public void setUpdateFastestInterval(int updateFastestInterval)
    {
        this.updateFastestInterval = updateFastestInterval;
    }


    /**
     * Gets the update nominal interval.
     *
     * @return the update nominal interval
     */
    public int getUpdateNominalInterval()
    {
        return updateNominalInterval;
    }


    /**
     * Sets the update nominal interval.
     *
     * @param updateNominalInterval
     *            the new update nominal interval
     */
    public void setUpdateNominalInterval(int updateNominalInterval)
    {
        this.updateNominalInterval = updateNominalInterval;
    }


    /**
     * Gets the update priority.
     *
     * @return the update priority
     */
    public int getUpdatePriority()
    {
        return updatePriority;
    }


    /**
     * Sets the update priority.
     *
     * @param updatePriority
     *            the new update priority
     */
    public void setUpdatePriority(int updatePriority)
    {
        // these are the specced constants in LocationRequest
        if (updatePriority == LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            || updatePriority == LocationRequest.PRIORITY_HIGH_ACCURACY
            || updatePriority == LocationRequest.PRIORITY_LOW_POWER
            || updatePriority == LocationRequest.PRIORITY_NO_POWER)
        {
            this.updatePriority = updatePriority;
        }
    }


    // ----------------------------------------------------------
    @Override
    public void restart()
    {
        stop();
        configureRequester();
        start();
    }

}
