package io.github.personalprism.personalprism_droid;

import android.content.Context;
import java.util.Observable;
import java.util.Observer;

/**
 * The Class SourceManager. This is going to handle the data sources -
 * configuration and initialization. TODO: figure out how to stop updates from
 * here
 */
public class SourceManager
    implements Observer
{

    private LocationSource locationSource;


    @Override
    public void update(Observable arg0, Object arg1)
    {
        // TODO log updates that can't self-log (like google play services)
    }


    public SourceManager(Context context)
    {
        locationInit(context);
    }


    private void locationInit(Context context)
    {
        locationSource = new LocationSource(context, DbHandler.class);
    }

}
