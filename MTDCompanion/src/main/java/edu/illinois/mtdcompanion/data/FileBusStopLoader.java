package edu.illinois.mtdcompanion.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

import edu.illinois.mtdcompanion.models.BusStop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		JSONObject jsonObject = null;

		try {
			BufferedReader bufferedReader = null;

			bufferedReader = new BufferedReader(new InputStreamReader(is));
			StringBuffer content = new StringBuffer();
			char[] buffer = new char[1024];
			int number;

			while ((number = bufferedReader.read(buffer)) > 0) {
				content.append(buffer, 0 , number);
			}

			jsonObject = new JSONObject(content.toString());
		} catch (IOException e) {
			logger.error("IO Error Creating JSONObject");
			e.printStackTrace();
		} catch (JSONException e) {
			logger.error("JSON Error creating JSONObject");
			e.printStackTrace();
		}

		JSONArray jsonArray = null;

		database.open(context);

		try {
			jsonArray = jsonObject.getJSONArray("stops");

			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject object = jsonArray.getJSONObject(index);

				String stopID = object.getString("stop_id");
				String stopCode = object.getString("code");
				String stopName = object.getString("stop_name");

				String latitude = object.getJSONArray("stop_points").getJSONObject(0).getString("stop_lat");
				String longitude = object.getJSONArray("stop_points").getJSONObject(0).getString("stop_lon");

				BusStop newStop = new BusStop();
				newStop.setStopID(stopID);
				newStop.setStopCode(stopCode);
				newStop.setStopName(stopName);
				newStop.setLatitude(Double.parseDouble(latitude));
				newStop.setLongitude(Double.parseDouble(longitude));

				database.create(newStop);
			}
		} catch (JSONException e) {
			logger.error("JSON Error parsing JSONObject");
			e.printStackTrace();
		}

		database.close();

		/*
		InputStreamReader reader = new InputStreamReader(is, Charset.defaultCharset());
		BufferedReader buffer = new BufferedReader(reader);

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
		 */
	}

}
