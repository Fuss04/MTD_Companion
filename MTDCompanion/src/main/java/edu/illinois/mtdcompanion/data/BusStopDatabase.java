package edu.illinois.mtdcompanion.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import edu.illinois.mtdcompanion.models.BusStop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Manages creation and upgrading of database for storing bus stops <br>
 * Used internally by {@link BusStopDatabaseManager}
 * @author Tom Olson
 */
public class BusStopDatabase extends OrmLiteSqliteOpenHelper {

	/**
	 * SLF4J Logger object for logging in BusStopDatabase class
	 */
	private static final Logger logger = LoggerFactory.getLogger(BusStopDatabase.class);

	/**
	 * Current singleton instance of BusStopDatabase
	 */
	private static BusStopDatabase instance = null;

	/**
	 * Name of database used to store bus stops
	 */
	private static final String DATABASE_NAME = "BusStops.db";

	/**
	 * Version number of database schema used to store bus stops
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * Database Access Object (DAO) for BusStop
	 */
	private Dao<BusStop, Integer> dao = null;

	/**
	 * Default constructor for singleton
	 * @param context Current context of Android application
	 */
	private BusStopDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Get the current singleton instance of BusStopDatabase
	 * @return {@link #instance}
	 */
	public static synchronized BusStopDatabase getInstance(Context context) {
		if (instance == null) {
			instance = new BusStopDatabase(context);

			logger.debug("New instance of BusStopDatabase successfully created");
		}

		return instance;
	}

	/**
	 * Called when database is first created <br>
	 * Creates all tables found in database schema
	 */
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			// Create database tables
			TableUtils.createTable(connectionSource, BusStop.class);

			logger.debug("All tables successfully created onCreate");
		} catch (SQLException e) {
			logger.error("Failed to drop one or more table onCreate");
			e.printStackTrace();
		}
	}

	/**
	 * Called when database schema is modified <br>
	 * All existing tables are dropped <br>
	 * {@link #onCreate(SQLiteDatabase, ConnectionSource)} is called using new database schema
	 */
	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			// Drop old version of database
			TableUtils.dropTable(connectionSource, BusStop.class, true);

			logger.debug("All tables successfully dropped onUpgrade");
		} catch (SQLException e) {
			logger.error("Failed to drop one or more table onUpgrade");
			e.printStackTrace();
		}

		// Recreate database using new version
		onCreate(database, connectionSource);
	}

	/**
	 * Returns Database Access Object (DAO) for BusStop <br>
	 * DAO is created once and then cached
	 * @return {@link #dao}
	 */
	public Dao<BusStop, Integer> getBusStopDAO() {
		if (dao == null) {
			try {
				// Get DAO that represents BusStop
				dao = getDao(BusStop.class);
			} catch (SQLException e) {
				logger.error("Unable to get DAO for BusStop");
				e.printStackTrace();
			}
		}

		return dao;
	}

	/**
	 * Close database connection <br>
	 * Clears {@link #dao}
	 */
	@Override
	public void close() {
		super.close();

		dao = null;
		instance = null;
	}

}
