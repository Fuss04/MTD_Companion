package edu.illinois.mtdcompanion.data;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import edu.illinois.mtdcompanion.interfaces.DatabaseManager;
import edu.illinois.mtdcompanion.models.BusStop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.stmt.QueryBuilder;

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

	/**
	 * Create a new entry in database table for storing bus stops
	 * @param busStop New BusStop to be added to database table
	 */
	public void create(BusStop busStop) {
		try {
			database.getBusStopDAO().create(busStop);
		} catch (SQLException e) {
			logger.error("Failed to create new entry in BusStopDatabase");
			e.printStackTrace();
		}
	}

	/**
	 * Update an existing entry in database table for storing bus stops
	 * @param busStop BusStop to be updated in database table
	 */
	public void update(BusStop busStop) {
		try {
			database.getBusStopDAO().update(busStop);
		} catch (SQLException e) {
			logger.error("Failed to update entry in BusStopDatabase");
			e.printStackTrace();
		}
	}

	/**
	 * Check if table exists for storing bus stops
	 * @return True or False depending if table exists
	 */
	public boolean tableExists() {
		boolean exists = false;

		try {
			exists = database.getBusStopDAO().isTableExists();
		} catch (SQLException e) {
			logger.error("Failed to check if table exists in BusStopDatabase");
			e.printStackTrace();
		}

		return exists;
	}

	/**
	 * Get the number of rows in database table for storing bus stops
	 * @return Number of rows in database table for storing bus stops
	 */
	public long rowCount() {
		long rowCount = 0;

		try {
			rowCount = database.getBusStopDAO().countOf();
		} catch (SQLException e) {
			logger.error("Failed to get number of rows in BusStopDatabase");
			e.printStackTrace();
		}

		return rowCount;
	}

	/**
	 * Get bus stop associated with stop code
	 * @param stopCode Stop code associated with bus stop
	 * @return Bus stop associated with stop code
	 */
	public BusStop getStopByCode(String stopCode) {
		BusStop busStop = null;

		try {
			QueryBuilder<BusStop, Integer> busStopQuery = database.getBusStopDAO().queryBuilder();

			// Get bus stop associated with stopCode
			busStopQuery.where().eq(BusStop.STOP_CODE, stopCode);
			List<BusStop> busQuery = busStop.query();
			if (!busQuery.isEmpty()) {
				busStop = busStopQuery.query().get(0);
			}	
		} catch (SQLException e) {
			logger.error("Failed to get entry in BusStopDatabase where stopCode={}", stopCode);
			e.printStackTrace();
		}

		return busStop;
	}

	/**
	 * Get bus stop associated with stop ID
	 * @param stopID Stop ID associated with bus stop
	 * @return Bus stop associated with stop ID
	 */
	public BusStop getStopByID(String stopID) {
		BusStop busStop = null;

		try {
			QueryBuilder<BusStop, Integer> busStopQuery = database.getBusStopDAO().queryBuilder();

			// Get bus stop associated with stopID
			busStopQuery.where().eq(BusStop.STOP_ID, stopID);
			List<BusStop> busQuery = busStop.query();
			if (!busQuery.isEmpty()) {
				busStop = busStopQuery.query().get(0);
			}
		} catch (SQLException e) {
			logger.error("Failed to get entry in BusStopDatabase where stopID={}", stopID);
			e.printStackTrace();
		}

		return busStop;
	}

	/**
	 * Get list of every entry in database table for storing bus stops
	 * @return List containing every entry in database table for storing bus stops
	 */
	public List<BusStop> getAll() {
		List<BusStop> busStopList = null;

		try {
			busStopList = database.getBusStopDAO().queryForAll();
		} catch (SQLException e) {
			logger.error("Failed to get list of rows in BusStopDatabase");
			e.printStackTrace();
		}

		return busStopList;
	}

}
