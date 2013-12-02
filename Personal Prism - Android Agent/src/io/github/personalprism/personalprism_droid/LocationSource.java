package io.github.personalprism.personalprism_droid;

import java.util.Observable;

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
public class LocationSource extends Observable implements ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener {
	private LocationRequest requester;
	private LocationClient client;
	private boolean observerRequired;
	private PendingIntent callbackIntent;

	/**
	 * Pass a context to begin location updates.
	 * 
	 * @param context
	 *            the context the listener is started from.
	 */
	@SuppressWarnings("rawtypes")
	public LocationSource(Context context, Class listener) {
		observerRequired = (listener == null);
		// create a new location requester, set priority and request interval
		requester = LocationRequest.create();
		requester.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		requester.setInterval(20 * 1000);
		requester.setFastestInterval(10000);

		// create the location client
		client = new LocationClient(context, this, this);
		// connect the location client
		client.connect();
		if (listener != null) 
			{
			Intent notificationIntent = new Intent(context, listener);
			callbackIntent = PendingIntent.getService(context, 0,
				notificationIntent, 0);
			}

	}

	/**
	 * Called when the phone's location updates. (DO NOT CALL)
	 * 
	 * @param location
	 *            the new location data.
	 */
	@Override
	public void onLocationChanged(Location location) {
		setChanged();
		notifyObservers(location);
	}

	/**
	 * If the connection to google maps fails, handle it appropriately. (DO NOT
	 * CALL)
	 * 
	 * @param result
	 *            the result of the failed connection.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Handle a failed connection
	}

	/**
	 * Called automatically when the client successfully connects (DO NOT CALL).
	 * 
	 * @param connectionHint
	 *            the data bundle for the connection.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "connected, observer "
					+ observerRequired);
		if (observerRequired)
			client.requestLocationUpdates(requester, this);
		else
			client.requestLocationUpdates(requester, callbackIntent);
	}

	/**
	 * What to do when the location client disconnects (DO NOT CALL).
	 */
	@Override
	public void onDisconnected() {
		System.out.println("Location client has disconnected.");
		client.removeLocationUpdates(this);
	}

	/**
	 * Stops requests to the location.
	 */
	public void stopLocationUpdates() {
		client.disconnect();
	}

}
