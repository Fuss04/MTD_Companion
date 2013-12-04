package edu.illinois.mtdcompanion.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Model for storing bus routes in database
 * @author Tom Olson
 */
@DatabaseTable(tableName = "BusRoute")
public class BusRoute {

	/**
	 * SLF4J Logger object for logging in BusRoute class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(BusRoute.class);

	// TODO

	/**
	 * Default constructor
	 */
	public BusRoute() {
		// Used by ORMLite for reflection
	}

	// TODO

}
