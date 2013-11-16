package io.github.personalprism.personalprism_droid;

import sofia.app.Screen;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.GoogleMapOptions;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
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
    extends Screen
    implements Observer
{
    private GoogleMap      map;
    private LocationSource source;


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

        source = new LocationSource(this);
        source.addObserver(this);
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
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(loc
            .getLatitude(), loc.getLongitude())));

        drawCircle(loc);
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
                        .getLongitude())).fillColor(0x15FF0000).radius(10)
                .strokeWidth(0);

        map.addCircle(circOpt);
    }
}
