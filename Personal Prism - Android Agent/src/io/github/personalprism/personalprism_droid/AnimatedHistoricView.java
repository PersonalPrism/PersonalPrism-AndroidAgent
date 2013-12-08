package io.github.personalprism.personalprism_droid;

import sofia.util.Timer;
import java.util.Date;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import java.util.ListIterator;
import android.widget.Button;
import java.util.LinkedList;
import android.location.Location;
import java.util.ArrayList;
import sofia.app.Screen;
import com.google.android.gms.maps.GoogleMap;
import android.os.Bundle;
import com.google.android.gms.maps.MapFragment;

/**
 * // -------------------------------------------------------------------------
 * /** Displays an animation of the old location data.
 *
 * @author Stuart Harvey (stu)
 * @version 2013.12.8
 */
public class AnimatedHistoricView
    extends Screen
    implements OnMarkerClickListener
{
    private GoogleMap              map;
    private LinkedList<Location>   locationList;
    private ListIterator<Location> iterator;
    private Button                 next;
    private Button                 previous;
    private Button                 animate;


    /**
     * On creation of the activity, start the animation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_historic_view);

        // create map fragment (requires min API level 11)
        map =
            ((MapFragment)getFragmentManager().findFragmentById(
                R.id.historicmap)).getMap();

//        createLinkedLocationList(DbHandler.getLocationList());
        ArrayList<Location> results = getIntent().getParcelableArrayListExtra(DbHandler.DBHANDLER_LOCATION_RESULTS);
        createLinkedLocationList(results);

        // disallow buttons from being used initially

        map.getUiSettings().setZoomControlsEnabled(false);
    }


    /**
     * Converts an arraylist of locations into a LinkedList of locations.
     */
    private void createLinkedLocationList(ArrayList<Location> oldLocations)
    {
        locationList = new LinkedList<Location>();
        for (Location loc : oldLocations)
        {
            locationList.add(loc);
            drawMarker(loc);
        }
        // set the list iterator.
        iterator = locationList.listIterator();
        // if the list is not null, allow next and animate to be clicked.
        if (iterator.hasNext())
        {
            next.setEnabled(true);
            animate.setEnabled(true);
            previous.setEnabled(false);
        }
    }


    /**
     * When the next button is clicked, animate to the next position.
     * @return holypants
     */
    public boolean nextClicked()
    {
        Location nextLocation = iterator.next();
        LatLng target =
            new LatLng(nextLocation.getLatitude(), nextLocation.getLongitude());
        CameraPosition cameraPosition =
            new CameraPosition.Builder()
                .target(target)
                .zoom(18)
                .bearing(nextLocation.getBearing())
                .tilt(55)
                .build();
        map.animateCamera(CameraUpdateFactory
            .newCameraPosition(cameraPosition), 500, null);

        if (!iterator.hasNext())
        {
            next.setEnabled(false);
            animate.setEnabled(false);
            return true;
        }

        previous.setEnabled(true);
        return false;
    }

    /**
     * When the previous button is clicked, animate to the previous position.
     */
    public void previousClicked()
    {
        Location nextLocation = iterator.previous();
        LatLng target =
            new LatLng(nextLocation.getLatitude(), nextLocation.getLongitude());
        CameraPosition cameraPosition =
            new CameraPosition.Builder()
                .target(target)
                .zoom(18)
                .bearing(nextLocation.getBearing())
                .tilt(55)
                .build();
        map.animateCamera(CameraUpdateFactory
            .newCameraPosition(cameraPosition), 500, null);

        if (!iterator.hasPrevious())
        {
            previous.setEnabled(false);
        }

        next.setEnabled(true);
        animate.setEnabled(true);
    }

    /**
     * Iterate through all data points.
     */
    public void animateClicked()
    {
        Timer.callRepeatedly(this, "nextClicked", 500);
    }

    /**
     * Draws markers on the map where old location data is.
     */
    private void drawMarker(Location loc)
    {
        LatLng latlng =
            new LatLng(loc.getLatitude(), loc.getLongitude());

        String titleData = new Date(loc.getTime()).toString();

        map.addMarker(new MarkerOptions()
            .position(latlng)
            .draggable(false)
            .title(titleData)
            .snippet(
                "Latitude: " + latlng.latitude + "\nLongtidue: "
                    + latlng.longitude));
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

/*    private class AnimateTask extends TimerTask {

        *//**
         * The operations to be executed each time the timer executes a task.
         *//*
        @Override
        public void run()
        {
            if (iterator.hasNext())
            {
                nextClicked();
            }
            else
            {
                this.cancel();
            }
        }

    }*/
}
