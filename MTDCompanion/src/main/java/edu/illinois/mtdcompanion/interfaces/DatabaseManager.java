package edu.illinois.mtdcompanion.interfaces;

import android.content.Context;

/**
 * Interface for DatabaseManager classes <br>
 * DatabaseManager allows application to connect to a particular database
 * @author Tom Olson
 */
public interface DatabaseManager {

	/**
	 * Create and open new connection to database
	 * @param context Current application context
	 */
	public void open(Context context);

	/**
	 * Close and destroy existing connection to database
	 */
	public void close();

}
