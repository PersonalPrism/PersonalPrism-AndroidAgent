package io.github.personalprism.personalprism_droid;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import java.util.ArrayList;
import android.location.Location;

/**
 * A series of tests to assert that the animated map view is working as
 * intended.
 * 
 * @author Stuart Harvey (stu)
 * @version 2013.12.8
 */
public class AnimatedHistoricViewTest
    //extends student.AndroidTestCase<AnimatedHistoricView>
    extends ActivityInstrumentationTestCase2<AnimatedHistoricView>
{
    private AnimatedHistoricView view;
    private Button               next;
    private Button               previous;
    private Button               animate;


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
        
        view = this.launchActivity(
            "io.github.personalprism.personalprism_droid",
            AnimatedHistoricView.class,
            bundle);
        
        System.out.println("Activity about to start");
        System.out.println("Activity started");

    }


    /**
     * Assert that the map is started and located on the right coords.
     */
    public void testInit()
    {
        LatLng result = view.getCameraPosition().target;
        LatLng expected = new LatLng(37.216667, -80.416667);
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
        //click(this.next);

        LatLng result = view.getCameraPosition().target;
        LatLng expected = new LatLng(0, 0);
        assertEquals(expected, result);
    }


    /**
     * Test previous button.
     */
    public void testPrevious()
    {
        //click(this.next);
        //click(this.next);
        //click(this.previous);
        LatLng result = view.getCameraPosition().target;
        LatLng expected = new LatLng(0, 0);
        assertEquals(expected, result);
        assertFalse(previous.isEnabled());
    }
}
