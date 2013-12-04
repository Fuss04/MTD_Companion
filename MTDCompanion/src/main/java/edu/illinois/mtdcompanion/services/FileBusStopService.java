package edu.illinois.mtdcompanion.services;

import android.app.IntentService;
import android.content.Intent;

import edu.illinois.mtdcompanion.data.FileBusStopLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for initializing database with health questions from jSON file <br>
 * Uses {@link FileBusStopLoader} to load questions from jSON file into database
 * @author Tom Olson
 */
public class FileBusStopService extends IntentService {

	/**
	 * SLF4J Logger object for logging in FileBusStopService class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FileBusStopService.class);

	/**
	 * BusStopLoader used to get bus stops from jSON file
	 */
	private FileBusStopLoader busStopLoader;

	/**
	 * Default constructor
	 */
	public FileBusStopService() {
		super("FileBusStopService");

		// TODO
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO
	}

}
