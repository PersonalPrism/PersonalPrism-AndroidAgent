package io.github.personalprism.personalprism_droid;

import com.google.android.gms.maps.CameraUpdateFactory;
import android.os.Handler;
import sohail.aziz.service.MyResultReceiver;
import android.content.Intent;
import java.util.Date;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

// -------------------------------------------------------------------------
/**
 * A map view for Personal Prism.
 *
 * @author Stuart Harvey (stu)
 * @version 2013.11.15
 */
public class MapView
    extends Activity
    implements Observer, OnMarkerClickListener
    //sohail.aziz.service.MyResultReceiver.Receiver
{

    //protected sohail.aziz.service.MyResultReceiver receiver;

    private GoogleMap           map;          // map display
    private LocationSource      source;       // where realtime location
// updates are found
    private ArrayList<Location> locations;    // allows clickable markers


    /**
     * Creates a google map view, adds location gatherer.
     *
     * @param savedInstanceState
     *            ...?
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        // create map fragment (requires min API level 11)
        map =
            ((MapFragment)getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setOnMarkerClickListener(this);

        locations = new ArrayList<Location>(0);

        //
        source = new LocationSource(this, null);
        // observe location updater to receive new locations in update()
        source.addObserver(this);

        //resultreceiver for db queries
        /*receiver = new MyResultReceiver(new Handler());
        receiver.setReceiver(this);*/

        // display old locations on map
        /*Intent intent =
            DbHandler.locationQueryAllMaker(receiver);
        startService(intent);*/
        
        drawOldLocations();
    }


    /**
     * Called when the location is updated.
     *
     * @param locationSource
     *            Observed location gatherer.
     * @param location
     *            Contemporary location data.
     */
    @Override
    public void update(Observable locationSource, Object location)
    {
        Location loc = (Location)location;
        locations.add(loc);

        // center camera
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            new LatLng(loc.getLatitude(), loc.getLongitude()),
            18));//maybe dynamically calculate zoom in future

        drawCircle(loc);
        drawMarker(loc);
//        drawLine(new LatLng(loc.getLatitude(), loc.getLongitude()));

    }


    /**
     * Draws a circle with opacity 15/255 at the latest location.
     *
     * @param lastLocation
     *            The latest location update.
     */
    private void drawCircle(Location lastLocation)
    {
        CircleOptions circOpt =
            new CircleOptions()
                .center(
                    new LatLng(lastLocation.getLatitude(), lastLocation
                        .getLongitude())).fillColor(0x15FF0000).radius(5)
                .strokeWidth(0);

        map.addCircle(circOpt);
    }

    /**
     * Draws markers on the map where old location data is.
     */
    private void drawMarker(Location latest)
    {
        LatLng latlng =
            new LatLng(latest.getLatitude(), latest.getLongitude());
        
        String titleData = new Date(latest.getTime()).toString();
        
        map.addMarker(new MarkerOptions()
            .position(latlng)
            .draggable(false)
            .title(titleData)
            .snippet(
                "Latitude: " + latlng.latitude + "\nLongtidue: "
                    + latlng.longitude).alpha(0f));
    }


    /**
     * Handling for a marker click.
     *
     * @param marker
     *            the marker that has been clicked.
     * @return true if using custom behavior, else false.
     */
    @Override
    public boolean onMarkerClick(Marker marker)
    {
        marker.showInfoWindow();
        return true;
    }


    /**
     * Displays old locations as blue circles on the map.
     */
    private void drawOldLocations()
    {
        ArrayList<Location> oldLocations = DbHandler.getLocationList();
        if (oldLocations.size() > 1)
        {
            for (Location loc : oldLocations)
            {
                CircleOptions circOpt =
                    new CircleOptions()
                        .center(
                            new LatLng(loc.getLatitude(), loc.getLongitude()))
                        .fillColor(0x15FF0000).radius(5).strokeWidth(0);

                map.addCircle(circOpt);

                drawMarker(loc);
            }
        }
    }
}
