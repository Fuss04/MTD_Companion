package edu.illinois.mtdcompanion.data;

import android.content.Context;

import edu.illinois.mtdcompanion.interfaces.DatabaseManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages connection to database for storing bus stops <br>
 * Uses {@link BusStopDatabase} to access database
 * @author Tom Olson
 */
public class BusStopDatabaseManager implements DatabaseManager {

	/**
	 * SLF4J Logger object for logging in BusStopDatabaseManager class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BusStopDatabaseManager.class);

	/**
	 * Current singleton instance of BusStopDatabaseManager
	 */
	private static BusStopDatabaseManager instance = null;

	/**
	 * Current instance of database helper class BusStopDatabase
	 */
	private BusStopDatabase database = null;

	/**
	 * Default constructor for singleton
	 */
	private BusStopDatabaseManager() {}

	/**
	 * Get the current singleton instance of BusStopDatabaseManager
	 * @return {@link #instance}
	 */
	public static synchronized BusStopDatabaseManager getInstance() {
		if (instance == null) {
			instance = new BusStopDatabaseManager();

			logger.debug("New instance of BusStopDatabaseManager successfully created");
		}

		return instance;
	}

	/**
	 * Create and open new connection to database
	 * @param context Current application context
	 */
	@Override
	public void open(Context context) {
		if (database == null) {
			database = BusStopDatabase.getInstance(context);

			logger.debug("New connection to BusStopDatabase opened");
		}
	}

	/**
	 * Close and destroy existing connection to database
	 */
	@Override
	public void close() {
		if (database != null) {
			database.close();
			database = null;

			logger.debug("Connection to BusStopDatabase closed");
		}
	}

	// TODO

}
