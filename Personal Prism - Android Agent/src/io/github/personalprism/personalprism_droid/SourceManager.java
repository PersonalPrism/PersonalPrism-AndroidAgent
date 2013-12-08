package io.github.personalprism.personalprism_droid;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import io.github.personalprism.personalprism_droid.LocationSource.Mode;
import android.content.Context;
import java.util.Observable;
import java.util.Observer;

/**
 * The Class SourceManager. This is going to handle the data sources -
 * configuration and initialization.
 */
public class SourceManager
    implements Observer
{

    Map<String, PrismDataSource> sources;

    @Override
    public void update(Observable arg0, Object arg1)
    {
        // TODO log updates that can't self-log (like google play services)
    }


    public SourceManager(Context context)
    {
        sources = new HashMap<String, PrismDataSource>();
        sources.put(LocationSource.COMPONENT_NAME, new LocationSource(context, Mode.BACKGROUND));
    }



}
