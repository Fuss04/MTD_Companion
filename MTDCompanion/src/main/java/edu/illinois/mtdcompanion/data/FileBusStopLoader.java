package edu.illinois.mtdcompanion.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import android.content.Context;
import android.content.res.AssetManager;

import edu.illinois.mtdcompanion.models.BusStop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Loads questions in database from jSON file <br>
 * jSON file is stored in assets folder <br>
 * Used to initialize bus stop database for new user
 * @author Tom Olson
 */
public class FileBusStopLoader {

	/**
	 * SLF4J Logger object for logging in FileBusStopLoader class
	 */
	private static final Logger logger = LoggerFactory.getLogger(FileBusStopLoader.class);

	/**
	 * File path of jSON file storing bus stops
	 */
	private static final String BUS_STOPS_FILE = "/json/Bus_Stops.json";

	/**
	 * AssetManager to open file included in assets folder
	 */
	private AssetManager assetManager;

	/**
	 * Current context of Android application
	 */
	private Context context;

	/**
	 * DatbaseManager used to connect to database for storing bus stops
	 */
	private BusStopDatabaseManager database;

	/**
	 * Default constructor
	 * @param context Current context of application
	 */
	public FileBusStopLoader(Context context) {
		database = BusStopDatabaseManager.getInstance();

		assetManager = context.getAssets();

		this.context = context;
	}

	/**
	 * Populate database with stops using jSON file in assets folder
	 */
	public void populateDatabase() {
		InputStream is = null;

		try {
			is = assetManager.open(BUS_STOPS_FILE);
		} catch (IOException e) {
			logger.error("Unable to open jSON file storing bus stops");
			e.printStackTrace();
		}

		InputStreamReader reader = new InputStreamReader(is, Charset.defaultCharset());

		JsonParser parser = new JsonParser();

		JsonObject rootObject = parser.parse(reader).getAsJsonObject();
		JsonArray busStopList = rootObject.getAsJsonArray("stops");

		database.open(context);

		for (JsonElement busStop : busStopList) {
			String stopID = busStop.getAsJsonObject().getAsJsonObject("stop_id").getAsString();
			String stopCode = busStop.getAsJsonObject().getAsJsonObject("code").getAsString();
			String stopName = busStop.getAsJsonObject().getAsJsonObject("stop_name").getAsString();

			JsonArray stopPointList = busStop.getAsJsonObject().getAsJsonArray("stop_points");

			JsonElement stopPoint = stopPointList.get(0);
			String latitude = stopPoint.getAsJsonObject().getAsJsonObject("stop_lat").getAsString();
			String longitude = stopPoint.getAsJsonObject().getAsJsonObject("stop_lon").getAsString();

			BusStop newStop = new BusStop();
			newStop.setStopID(stopID);
			newStop.setStopCode(stopCode);
			newStop.setStopName(stopName);
			newStop.setLatitude(Double.parseDouble(latitude));
			newStop.setLongitude(Double.parseDouble(longitude));

			database.create(newStop);
		}

		database.close();
	}

}
