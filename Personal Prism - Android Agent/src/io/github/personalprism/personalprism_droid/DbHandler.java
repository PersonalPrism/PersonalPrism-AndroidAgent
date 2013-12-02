package io.github.personalprism.personalprism_droid;

import com.db4o.ext.StoredClass;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.query.Predicate;
import com.db4o.query.QueryComparator;
import com.google.android.gms.location.LocationClient;

import org.apache.commons.lang3.SerializationUtils;

/**
 * This class manages the local DB4O database that we keep records in. It is an
 * IntentService, so it uses Intents for activation and message passing. You
 * feed it a command in an intent and, depending on the command, a PendingIntent
 * to send a return message.
 * 
 * @author Hunter Morgan <kp1108>
 * @version Dec, 1, 2013
 */
public class DbHandler extends IntentService {

	/**
	 * The Command Enum. This specifies the action requested in an intent.
	 */
	public enum Command {
		LOCATION_LOG, LOCATION_SEARCH, NONE
	}

	/** The Constant DBHANDLER_REQUEST_CALLBACK for a bundle extra key. */
	public static final String DBHANDLER_REQUEST_CALLBACK = "io.github.personalprism.personalprism_droid.DbHandler."
			+ "DBHANDLER_REQUEST_CALLBACK";

	/** The Constant DBHANDLER_LOCATION_RESULTS. */
	public static final String DBHANDLER_LOCATION_RESULTS = "io.github.personalprism.personalprism_droid.DbHandler."
			+ "DBHANDLER_LOCATION_RESULTS";

	/** The Constant DBHANDLER_LOCATION_QUERY. */
	public static final String DBHANDLER_LOCATION_QUERY = "io.github.personalprism.personalprism_droid.DbHandler."
			+ "DBHANDLER_LOCATION_QUERY";

	/** The Constant DBHANDLER_LOCATION_QUERY_COMPARATOR. */
	public static final String DBHANDLER_LOCATION_QUERY_COMPARATOR = "io.github.personalprism.personalprism_droid.DbHandler."
			+ "DBHANDLER_LOCATION_QUERY_COMPARATOR";;

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

	/** Instantiates a new DbHandler. */
	public DbHandler() // or here.
	{
		super("DbHandler");
	}

	private ObjectContainer db; // DB4O db handle

	/**
	 * This method parses the sent Intent to get a command and optional callback
	 * PendingIntent, and respond to the command.
	 * 
	 * @param intent
	 *            the intent
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Command command = Command.NONE;
		if (intent.getAction() != null)  command = Command.valueOf(intent.getAction());

		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "handling command: " + command
					+ ", intent: " + intent.toString());

		switch (command) {
			case LOCATION_LOG:
				logLocationHelper(intent);
				break;
			case LOCATION_SEARCH:
				searchLocationHelper(intent);
				break;
			default:
				//this is a hack because background updates don't have a command
				if (intent.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED) != null)
					logLocationHelper(intent);
				
//				StringBuilder debug = new StringBuilder();
//				debug.append("toString(): " + intent.toString() + "\n");
//				debug.append("getAction(): " + intent.getAction() + "\n");
//				debug.append("getData(): " + intent.getData() + "\n");
//				debug.append("getExtras().keySet().toString(): " + intent.getExtras().keySet().toString() + "\n");
//				debug.append("getExtras().keySet().toString(): " + intent.getExtras(). + "\n");
//				Toast.makeText(this, debug, Toast.LENGTH_LONG).show();
				break;
		}
	}

	/**
	 * Search location command helper. Ok - this isn't going to be super simple.
	 * We need to pass in a DB4O native query Predicate and optional Comparator
	 * to run the query. You will get back an array of Locations.
	 * 
	 * @param intent
	 *            the intent
	 */
	@SuppressWarnings("unchecked")
	// it happens
	private void searchLocationHelper(Intent intent) {
		// These DB4O classes aren't Parcelable, but they are Serializable
		Predicate<Location> predicate = (Predicate<Location>) SerializationUtils
				.deserialize(intent.getByteArrayExtra(DBHANDLER_LOCATION_QUERY));
		QueryComparator<Location> comparator = (QueryComparator<Location>) SerializationUtils
				.deserialize(intent
						.getByteArrayExtra(DBHANDLER_LOCATION_QUERY_COMPARATOR));

		ObjectSet<Location> results;
		Location[] locations = null;
		// Query DB, with or without comparator
		if (intent.getByteArrayExtra(DBHANDLER_LOCATION_QUERY_COMPARATOR) != null)
			results = db.query(predicate, comparator);
		else
			results = db.query(predicate);

		// Set results array
		results.toArray(locations);

		PendingIntent callback = intent // we expect a PI stored here
				.getParcelableExtra(DBHANDLER_REQUEST_CALLBACK);
		Intent response = new Intent(DBHANDLER_LOCATION_RESULTS);
		response.putExtra(DBHANDLER_LOCATION_RESULTS, locations);
		// TODO: wire up MapView/MainUIScreen(for code example) to receive
		// intent reply
		try {
			// I don't know what param2, code, is for
			callback.send(getApplicationContext(), 0, response);
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Log location command helper.
	 * 
	 * @param intent
	 *            the intent
	 */
	private void logLocationHelper(Intent intent) {
		Location location = intent
				.getParcelableExtra(LocationClient.KEY_LOCATION_CHANGED);
		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "logging " + location);
		try {
			db.store(location);
		} catch (DatabaseClosedException e) {
			if (MainUIScreen.DEBUG)
				Log.e(getClass().getSimpleName(),
						"db exception in onHandleIntent", e);
		} catch (DatabaseReadOnlyException e) {
			if (MainUIScreen.DEBUG)
				Log.e(getClass().getSimpleName(),
						"db exception in onHandleIntent", e);
		}
	}

	// ----------------------------------------------------------
	@Override
	public void onCreate() {
		super.onCreate();
		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "try db open");
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				MainUIScreen.DB4OFILENAME);
		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "db open, " + db.toString());
	}

	// ----------------------------------------------------------
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (MainUIScreen.DEBUG)
			Log.d(getClass().getSimpleName(), "db close with " + records(db)
					+ " records");
		db.close();
	}

	/**
	 * Record count snippet from teh internets.
	 *
	 * @param db the db
	 * @return the int
	 */
	public static int records(ObjectContainer db) {
		int records = 0;
		for (StoredClass storedClass : db.ext().storedClasses())
			// Filter out db4o internal objects
			// and filter out object which have a parent-class, because these
			// are in the count of the parent
			if (!storedClass.getName().startsWith("com.db4o")
					&& null == storedClass.getParentStoredClass())
				records += storedClass.instanceCount();
		return records;
	}

}
