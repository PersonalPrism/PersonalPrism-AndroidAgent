package io.github.personalprism.personalprism_droid;

import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import com.google.android.gms.maps.GoogleMap;

/**
 * // -------------------------------------------------------------------------
 * /** A series of tests to assert that the map view is working as intended.
 * 
 * @author Stuart Harvey (stu)
 * @version 2013.12.8
 */
public class MapViewTest
    extends student.AndroidTestCase<MapView>
{
    private MapView view;
    private GoogleMap map;
    
    /**
     * Necessary constructor for android JUnit tests.
     */
    public MapViewTest()
    {
        super(MapView.class);
    }


    /**
     * Sets up the test instrumentation.
     */
    public void setUp()
    {
        view = new MapView();
        view.onCreate(null);
        view.disableUpdates();
    }
    
    /**
     * Assert that the map is started and located on the right coords.
     */
    public void testInit()
    {
        map = view.getMap();
        LatLng result = map.getCameraPosition().target;
        LatLng expected = new LatLng(37.216667, -80.416667);
        assertEquals(expected, result);
    }
    
    /**
     * Test the location update method.
     */
    public void testUpdate()
    {
        Location locationToAdd = new Location("");
        locationToAdd.setLatitude(30);
        locationToAdd.setLongitude(20);
        view.update(null, locationToAdd);
        
        map = view.getMap();
        LatLng result = map.getCameraPosition().target;
        LatLng expected = new LatLng(30, 20);
        assertEquals(expected, result);
    }
    
    // Cannot assert that elements are drawn.
}
