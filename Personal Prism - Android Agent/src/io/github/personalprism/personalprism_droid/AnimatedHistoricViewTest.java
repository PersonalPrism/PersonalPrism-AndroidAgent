package io.github.personalprism.personalprism_droid;

import android.widget.Button;
import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import java.util.ArrayList;
import android.location.Location;
import com.google.android.gms.maps.GoogleMap;

/**
 * // -------------------------------------------------------------------------
 * /** A series of tests to assert that the animated map view is working as
 * intended.
 * 
 * @author Stuart Harvey (stu)
 * @version 2013.12.8
 */
public class AnimatedHistoricViewTest
    extends student.AndroidTestCase<AnimatedHistoricView>
{
    private AnimatedHistoricView view;
    private GoogleMap            map;
    private Button next;
    private Button previous;
    private Button animate;


    /**
     * Necessary constructor for android JUnit tests.
     */
    public AnimatedHistoricViewTest()
    {
        super(AnimatedHistoricView.class);
    }


    /**
     * Sets up the test instrumentation.
     */
    public void setUp()
    {
        view = new AnimatedHistoricView();

        ArrayList<Location> locationsToAdd = new ArrayList<Location>();

        // add a bunch of locations
        for (int i = 0; i <= 5; i++)
        {
            Location location = new Location("");
            location.setLatitude(i * 10);
            location.setLongitude(i * 10);
            locationsToAdd.add(location);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(
            DbHandler.DBHANDLER_LOCATION_RESULTS,
            locationsToAdd);
        
        view.onCreate(bundle);
    }
    

    /**
     * Assert that the map is started and located on the right coords.
     */
    public void testInit()
    {
        map = view.getMap();
        LatLng result = map.getCameraPosition().target;
        LatLng expected = new LatLng(37.2281706, -80.4139393);
        assertEquals(expected, result);
        
        assertTrue(next.isEnabled());
        assertFalse(previous.isEnabled());
        assertTrue(animate.isEnabled());
    }
    
    /**
     * Test next button.
     */
    public void testNext()
    {
        click(next);
        map = view.getMap();
        LatLng result = map.getCameraPosition().target;
        LatLng expected = new LatLng(0, 0);
        assertEquals(expected, result);
    }
    
    /**
     * Test previous button.
     */
    public void testPrevious()
    {
        click(next);
        click(next);
        map = view.getMap();
        LatLng result = map.getCameraPosition().target;
        LatLng expected = new LatLng(0, 0);
        assertEquals(expected, result);
        assertFalse(previous.isEnabled());
    }
}
