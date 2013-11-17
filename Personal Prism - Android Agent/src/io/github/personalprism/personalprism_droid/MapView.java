package io.github.personalprism.personalprism_droid;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import sofia.app.Screen;
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
    implements Observer, OnMarkerClickListener
{
    private GoogleMap      map;
    private LocationSource source;
    private ArrayList<Node> data;
    
    private boolean hasFirstPoint;
    private ArrayList<LatLng> points;
    private Polyline line;

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
        
        data = new ArrayList<Node>(0);

        source = new LocationSource(this);
        source.addObserver(this);
        
        hasFirstPoint = false;
        points = new ArrayList<LatLng>(0);
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
        data.add(new Node(loc));
        
        // center camera
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc
            .getLatitude(), loc.getLongitude()), 18));

        drawCircle(loc);
        drawMarker(loc);
        drawLine(new LatLng(loc.getLatitude(), loc.getLongitude()));

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
    
    private void drawMarker(Location lastLocation)
    {
        LatLng latlng =
            new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        Node latestNode = data.get(data.size() - 1);
        
        map.addMarker(new MarkerOptions()
            .position(latlng)
            .draggable(false)
            .title(latestNode.getDate().toString())
            .snippet("Latitude: " + latlng.latitude + "\nLongtidue: " + latlng.longitude)
            .alpha(0f));
    }
    
    private void drawLine(LatLng latlng)
    {
        points.add(latlng);
        if (!hasFirstPoint)
        {
            PolylineOptions lineOptions = new PolylineOptions()
                .add(latlng);
            
            line = map.addPolyline(lineOptions);
            hasFirstPoint = true;
        }
        else
        {
            line.setPoints(points);
        }
    }

    /**
     * Handling for a marker click.
     * @param marker the marker that has been clicked.
     * @return true if using custom behavior, else false.
     */
    @Override
    public boolean onMarkerClick(Marker marker)
    {
        marker.showInfoWindow();
        return true;
    }
}
