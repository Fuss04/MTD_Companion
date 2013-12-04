package edu.illinois.mtdcompanion.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Model for storing bus stops in database
 * @author Tom Olson
 */
@DatabaseTable(tableName = "BusStop")
public class BusStop {

	/**
	 * SLF4J Logger object for logging in BusStop class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(BusStop.class);

	// TODO

	/**
	 * Default constructor
	 */
	public BusStop() {
		// Used by ORMLite for reflection
	}

	// TODO

}
