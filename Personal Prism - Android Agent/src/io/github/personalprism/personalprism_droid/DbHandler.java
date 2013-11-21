package io.github.personalprism.personalprism_droid;

import com.db4o.ext.StoredClass;
import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.google.android.gms.location.LocationClient;

// -------------------------------------------------------------------------
/**
 * This class now mostly just does record creation on the db4o. Follow it with
 * additional details about its purpose, what abstraction it represents, and how
 * to use it.
 *
 * @author Hunter
 * @version Nov 16, 2013
 */
public class DbHandler
    extends IntentService
{

    /**
     * Create a new DbHandler object.
     *
     * @param name the name
     */
    public DbHandler(String name) // I don't know if anything needs done here.
    {
        super("DbHandler");
        // TODO Auto-generated constructor stub
    }

    public DbHandler()
    {
        super("DbHandler");
    }


    private ObjectContainer     db;


    /**
     * Here, onHandleIntent tries to retrieve a location from the intent and
     * store it.
     * This definitely needs to be generalized later and support more than
     * requested location updates.
     *
     * @param intent the intent
     */
    @SuppressWarnings("cast")
    @Override
    protected void onHandleIntent(Intent intent)
    {

        Log.d(getClass().getSimpleName() ,"intent handling, " + intent.toString());

        try
        {
            db.store(((Location)intent
                .getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED)));
            Log.d(getClass().getSimpleName(), "logging " + (Location)intent
                .getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED));
        }
        catch (DatabaseClosedException e)
        {
            Log.e(getClass().getSimpleName(), "db exception in onHandleIntent", e);
        }
        catch (DatabaseReadOnlyException e)
        {
            Log.e(getClass().getSimpleName(), "db exception in onHandleIntent", e);
        }
        catch (ClassCastException e)
        {
            Log.e(
                getClass().getSimpleName(),
                "Exception from casting a parcelable extra to a Location",
                e);
        }
    }


    // ----------------------------------------------------------
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(getClass().getSimpleName(), "try db open");
        db =
            Db4oEmbedded
                .openFile(Db4oEmbedded.newConfiguration(), MainUIScreen.DB4OFILENAME);
        Log.d(getClass().getSimpleName(),"db open, " + db.toString());
    }


    // ----------------------------------------------------------
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(getClass().getSimpleName(),"db close with " + records(db) + " records");
        db.close();
    }

    public static int records(ObjectContainer db)
    {
        int records = 0;
        for(StoredClass storedClass : db.ext().storedClasses()){
            // Filter out db4o internal objects
            // and filter out object which have a parent-class, because these are in the count of the parent
            if(!storedClass.getName().startsWith("com.db4o") &&
                    null==storedClass.getParentStoredClass()) {
                records += storedClass.instanceCount();
            }
        }
        return records;
    }

}
