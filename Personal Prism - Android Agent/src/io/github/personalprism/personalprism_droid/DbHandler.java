package io.github.personalprism.personalprism_droid;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.StoredClass;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;
import com.google.android.gms.location.LocationClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class manages the local DB4O database that we keep records in. It is an
 * IntentService, so it uses Intents for activation and message passing. You
 * feed it a command in an intent and, depending on the command, a PendingIntent
 * to send a return message.
 *
 * @author Hunter Morgan <kp1108> <automaticgiant@gmail.com>
 * @version Dec, 8, 2013
 */
public class DbHandler
    extends IntentService
{

    /**
     * The Command Enum. This specifies the action requested in an intent.
     */
    public enum Command
    {
        LOCATION_LOG,
        LOCATION_SEARCH,
        NONE
    }

    /** The Constant DBHANDLER_REQUEST_CALLBACK for a bundle extra key. */

    public static final String  DBHANDLER_REQUEST_CALLBACK          =
                                                                        "io.github.personalprism.personalprism_droid.DbHandler."
                                                                            + "DBHANDLER_REQUEST_CALLBACK";

    /** The Constant DBHANDLER_LOCATION_RESULTS for a bundle extra key. */

    public static final String  DBHANDLER_LOCATION_RESULTS          =
                                                                        "io.github.personalprism.personalprism_droid.DbHandler."
                                                                            + "DBHANDLER_LOCATION_RESULTS";

    /** The Constant DBHANDLER_LOCATION_QUERY for a bundle extra key. */

    public static final String  DBHANDLER_LOCATION_QUERY            =
                                                                        "io.github.personalprism.personalprism_droid.DbHandler."
                                                                            + "DBHANDLER_LOCATION_QUERY";

    /** The Constant DBHANDLER_LOCATION_QUERY_COMPARATOR for a bundle extra key. */

    public static final String  DBHANDLER_LOCATION_QUERY_COMPARATOR =
                                                                        "io.github.personalprism.personalprism_droid.DbHandler."
                                                                            + "DBHANDLER_LOCATION_QUERY_COMPARATOR";

    /** The Constant DBHANDLER_QUERY_ID for a bundle extra key/resultCode. */
    public static final String DBHANDLER_QUERY_ID                  =
                                                                        "io.github.personalprism.personalprism_droid.DbHandler."
                                                                            + "DBHANDLER_QUERY_ID";

    private static final String PKGNAME = DbHandler.class.getPackage().getName();

    // DB4O db handle
    private ObjectContainer     db;


    /** Instantiates a new DbHandler. */
    public DbHandler() // or here.
    {
        super("DbHandler");
    }


    /**
     * Instantiates a new named DbHandler.
     *
     * @param name
     *            the name
     */
    public DbHandler(String name) // I don't think anything needs done here
    {
        super("DbHandler");
    }


    /**
     * Log location command helper.
     *
     * @param intent
     *            the intent
     */
    private void logLocationHelper(Intent intent)
    {
        Location location =
            intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), "logging " + location);
        }
        try
        {
            db.store(location);
        }
        catch (DatabaseClosedException e)
        {
            if (MainUIScreen.DEBUG)
            {
                Log.e(
                    getClass().getSimpleName(),
                    "db exception in onHandleIntent",
                    e);
            }
        }
        catch (DatabaseReadOnlyException e)
        {
            if (MainUIScreen.DEBUG)
            {
                Log.e(
                    getClass().getSimpleName(),
                    "db exception in onHandleIntent",
                    e);
            }
        }
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), "try db open");
        }
        db =
            Db4oEmbedded.openFile(
                Db4oEmbedded.newConfiguration(),
                MainUIScreen.db4oFilename);
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), "db open, " + db.toString());
        }
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), "db close with " + records(db)
                + " records");
        }
        db.close();
    }


    /**
     * This method parses the sent Intent to get a command and optional callback
     * PendingIntent, and respond to the command.
     *
     * @param intent
     *            the intent
     */
    @Override
    protected void onHandleIntent(Intent intent)
    {
        // this makes sure if no command, no NPE
        Command command = Command.NONE;
        if (intent.getAction() != null)
        {
            command = Command.valueOf(intent.getAction());
        }

        // log
        if (MainUIScreen.DEBUG)
        {
            Log.d(getClass().getSimpleName(), "handling command: " + command
                + ", intent: " + intent.toString());
        }

        switch (command)
        {
            case LOCATION_LOG:
                logLocationHelper(intent);
                break;
            case LOCATION_SEARCH:
                searchLocationHelper(intent);
                break;
            default:
                // this is a hack because background updates don't have a
// command
                if (intent
                    .getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED) != null)
                {
                    logLocationHelper(intent);
                }


                break;
        }
    }


    /**
     * Search location command helper. Ok - this isn't going to be super simple.
     * We need to pass in a DB4O native query Predicate and optional Comparator
     * to run the query. You will get back an array of Locations. Luckily, there
     * is a static method to create the intent.
     *
     * @param intent
     *            the intent
     */
    @SuppressWarnings("unchecked")
    // it happens
    private void searchLocationHelper(Intent intent)
    {
        Predicate<Location> predicate =
            (Predicate<Location>)intent
                .getSerializableExtra(DBHANDLER_LOCATION_QUERY);
        QueryComparator<Location> comparator;

        List<Location> results;
        // Query DB, with or without comparator
        if (intent.getSerializableExtra(DBHANDLER_LOCATION_QUERY_COMPARATOR) != null)
        {
            comparator =
                (QueryComparator<Location>)intent
                    .getSerializableExtra(DBHANDLER_LOCATION_QUERY_COMPARATOR);
            results = db.query(predicate, comparator);
        }
        else
        {
            results = db.query(predicate);
        }

        // Set results arraylist
        ArrayList<Location> locations = new ArrayList<Location>(results);

        ResultReceiver callback = intent // we expect a RR stored here
            .getParcelableExtra(DBHANDLER_REQUEST_CALLBACK);
        Bundle resultData = new Bundle();
        resultData
            .putParcelableArrayList(DBHANDLER_LOCATION_RESULTS, locations);
        // tag it as a result of a specific query
        callback.send(intent.getIntExtra(DBHANDLER_QUERY_ID, 0), resultData);
    }


    /**
     * Location query by date maker. Generates an intent to use for DbHandler
     * activation and query.
     *
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     * @param receiver
     *            (probably this)
     * @return the query intent
     */
    public static Intent locationQueryByDateMaker(
        final Date startDate,
        final Date endDate,
        final ResultReceiver receiver)
    {
        Intent intent = new Intent(Command.LOCATION_SEARCH.toString());
        // set target explicitly
        intent.setComponent(new ComponentName(
            PKGNAME,
            PKGNAME + ".DbHandler"));
        // package receiver
        intent.putExtra(DBHANDLER_REQUEST_CALLBACK, receiver);

        // create DB4O stuff
        Predicate<Location> predicate = new Predicate<Location>() {

            @Override
            public boolean match(Location arg0)
            {
                // we want to make sure it's within range
                return arg0.getTime() > startDate.getTime()
                    && arg0.getTime() < endDate.getTime();
            }
        };

        QueryComparator<Location> comparator = new QueryComparator<Location>() {

            @Override
            public int compare(Location arg0, Location arg1)
            {
                // and let's sort chronologically, for fun
                return (int)(arg0.getTime() - arg1.getTime());
            }
        };
        // package DB4O stuff
        intent.putExtra(DBHANDLER_LOCATION_QUERY, predicate);
        intent.putExtra(DBHANDLER_LOCATION_QUERY_COMPARATOR, comparator);
        return intent;
    }


    /**
     * Location query maker to dump all locations. NOTE: this implementation is
     * sub-optimal. It should really be a QBE, not NQ so DB doesn't have to
     * parse all Locations. C'est la vie. Be careful. This doesn't implement
     * paging either, so this could get out of hand if we get a big data set.
     *
     * @param receiver
     *            the receiver
     * @return the intent
     */
    public static Intent locationQueryAllMaker(final ResultReceiver receiver)
    {
        Intent intent = new Intent(Command.LOCATION_SEARCH.toString());
        // set target explicitly
        intent.setComponent(new ComponentName(
            PKGNAME,
            PKGNAME + ".DbHandler"));
        // package receiver
        intent.putExtra(DBHANDLER_REQUEST_CALLBACK, receiver);

        // create DB4O stuff
        Predicate<Location> predicate = new Predicate<Location>() {

            @Override
            public boolean match(Location arg0)
            {
                return true;
            }
        };

        QueryComparator<Location> comparator = new QueryComparator<Location>() {

            @Override
            public int compare(Location arg0, Location arg1)
            {
                // and let's sort chronologically, for fun
                return (int)(arg0.getTime() - arg1.getTime());
            }
        };
        // package DB4O stuff
        intent.putExtra(DBHANDLER_LOCATION_QUERY, predicate);
        intent.putExtra(DBHANDLER_LOCATION_QUERY_COMPARATOR, comparator);
        return intent;
    }

//    /**
//     * Attempt at retrieving a list of locations.
//     *
//     * @return an object set of locations.
//     */
//    public static ArrayList<Location> getLocationList()
//    {
//        ObjectContainer tempDB =
//            Db4oEmbedded.openFile(
//                Db4oEmbedded.newConfiguration(),
//                MainUIScreen.DB4OFILENAME);
//        ArrayList<Location> locationList =
//            new ArrayList<Location>(tempDB.query(Location.class));
//        tempDB.close();
//        return locationList;
//    }

    /**
     * Record count snippet from teh internets.
     *
     * @param db
     *            the db
     * @return the int
     */
    public static int records(ObjectContainer db)
    {
        int records = 0;
        for (StoredClass storedClass : db.ext().storedClasses())
        {
            // Filter out db4o internal objects
            // and filter out object which have a parent-class, because these
            // are in the count of the parent
            if (!storedClass.getName().startsWith("com.db4o")
                && null == storedClass.getParentStoredClass())
            {
                records += storedClass.instanceCount();
            }
        }
        return records;
    }

}
