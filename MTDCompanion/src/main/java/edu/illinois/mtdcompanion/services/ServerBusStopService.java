package edu.illinois.mtdcompanion.services;

import android.app.IntentService;
import android.content.Intent;

import edu.illinois.mtdcompanion.data.ServerBusStopLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for updating database with bus stops from server <br>
 * Uses {@link ServerBusStopLoader} to load bus stops from server into database
 * @author Tom Olson
 */
public class ServerBusStopService extends IntentService {

	/**
	 * SLF4J Logger object for logging in ServerBusStopService class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ServerBusStopService.class);

	/**
	 * QuestionLoader used to get bus stops from server
	 */
	private ServerBusStopLoader busStopLoader;

	/**
	 * Default constructor
	 */
	public ServerBusStopService() {
		super("ServerBusStopService");

		// TODO
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO
	}

}
