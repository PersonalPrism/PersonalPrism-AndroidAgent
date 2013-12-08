package io.github.personalprism.personalprism_droid;

import android.content.Context;
import io.github.personalprism.personalprism_droid.LocationSource.Mode;
import java.util.Observable;
import java.util.Observer;

/**
 * The Class SourceManager. This is supposed to handle the data sources -
 * configuration and initialization. It will be wired up to an alarm to make
 * sure settings are correct and send poll triggers to things that need
 * polled(we wouldn't want to have bunches of data sources that all have alarms
 * for polling, would we?). The purpose of this class is for extensibility. I
 * inted it to hold various PrismSources and facilitate programmatic
 * configuration menus and preferences storage behavior.
 * @author Hunter Morgan <kp1108>
 * @version 2013-12-07
 */
public class SourceManager
    implements Observer
{

    private LocationSource locationSource;


    /**
     * Gets the location source. We need a getter in case we need to turn it on
     * and off and reconfigure it. For now,
     *
     * @return the location source
     */
    public LocationSource getLocationSource()
    {
        return locationSource;
    }


    /**
     * Instantiates a new source manager. It will be set to log.
     *
     * @param context
     *            the context
     */
    public SourceManager(Context context)
    {
        locationSource = new LocationSource(context, Mode.BACKGROUND);
    }


    @Override
    public void update(Observable arg0, Object arg1)
    {
        // TODO log updates that can't self-log (like google play services)
        // also maybe watch application preferences here for updates?
    }

}
