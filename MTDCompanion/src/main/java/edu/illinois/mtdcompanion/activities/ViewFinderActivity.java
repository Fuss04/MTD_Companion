package edu.illinois.mtdcompanion.activities;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import edu.illinois.mtdcompanion.R;
import edu.illinois.mtdcompanion.models.MTDBus;
import edu.illinois.mtdcompanion.models.MTDBusLatLon;
import edu.illinois.mtdcompanion.models.MTDDepartures;
import edu.illinois.mtdcompanion.services.GPSTracker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.ScannerSession;

public class ViewFinderActivity extends Activity implements ScannerSession.Listener, SensorEventListener {

	private GPSTracker gps;
	private ScannerSession session;
	private TextView resultTextView;
	private RequestQueue mRequestQueue;
	private MTDDepartures mtdDepartures;

	private float currentDegree = 999999;
	private SensorManager mSensorManager;
	private float degreeChange = 999999;

	int tempCount = 0;

	float lat = 0;
	float lon = 0;
	String targetStop = "No stop found.";
	float latitude;
	float longitude;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		mRequestQueue = Volley.newRequestQueue(this);
		mtdDepartures = new MTDDepartures();
		resultTextView = (TextView) findViewById(R.id.scan_result);


		// get the camera preview surface & result text view
		SurfaceView preview = (SurfaceView) findViewById(R.id.preview);

		// Create a scanner session
		try {
			session = new ScannerSession(this, this, preview);
		} catch (MoodstocksError e) {
			e.log();
		}
		//GPS stuff
		gps = new GPSTracker(ViewFinderActivity.this);
		if(gps.canGetLocation()){
			latitude = (float) gps.getLatitude();
			longitude = (float) gps.getLongitude();
		}else{
			//dummy data for presentation
			latitude = (float) 40.109290;
			longitude = (float) -88.239948;
			gps.showSettingsAlert();

		}
		//Compass time yo
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


		getNearestFourBuses(latitude, longitude);

		// set session options//




	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		session.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// pause the scanner session
		session.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// close the scanner session
		session.close();
	}

	@Override
	public void onApiSearchStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onApiSearchComplete(Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onApiSearchFailed(MoodstocksError e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanComplete(Result result) {


	}

	@Override
	public void onScanFailed(MoodstocksError error) {
		//resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	}

	private void getNextBus(String stopId) {
		String url = Constants.MTD_BASE_URL + Constants.MTD_VERSION + Constants.MTD_FORMAT + Constants.MTD_METHOD_GET_DEPARTURES_BY_STOP + Constants.MTD_KEY + Constants.STOP_ID_PARAMETER + stopId;
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				parseJsonBusObject(response);
			}
		},
		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO
			}
		});

		mRequestQueue.add(stringRequest);
	}

	private void parseJsonBusObjectBearing(String stringResponse, double lat, double lon) {
		Gson gson = new GsonBuilder().create();
		mtdDepartures = gson.fromJson(stringResponse, MTDDepartures.class);
		if (mtdDepartures.getStops().isEmpty()) {
			resultTextView.setText("ERROR");
		} else {
			findTargetStop();
		}
	}

	private void findTargetStop() {
		List<MTDBus> newList = mtdDepartures.getStops();
		Iterator<MTDBus> iterator = newList.iterator();
		targetStop = "No Stop Available";
		double diff = 9999999;

		while (iterator.hasNext()) {
			MTDBus tempBus = iterator.next();
			Iterator<MTDBusLatLon> iterator2 = tempBus.getStop_points().iterator();
			while(iterator2.hasNext())
			{
				MTDBusLatLon tempLatLon = iterator2.next();
				float calc = (float) bearing(latitude, longitude, tempLatLon.getLat(), tempLatLon.getLon());
				if(degreeDistance(calc, currentDegree) < diff)
				{
					targetStop = tempLatLon.getStop_name();
					diff = degreeDistance(calc, currentDegree);
				}

			}
		}
	}

	private void getNearestFourBuses(final double lat, final double lon) {
		tempCount++;
		String url = Constants.MTD_BASE_URL + Constants.MTD_VERSION + Constants.MTD_FORMAT + Constants.MTD_PROXIMITY_PARAMTETER + Constants.MTD_KEY + Constants.LON_PARAMETER + lon + Constants.LAT_PARAMETER + lat + Constants.COUNT_PARAMETER + "4";
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				parseJsonBusObjectBearing(response, lat, lon);
			}
		},
		new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.print(error);
			}
		});

		mRequestQueue.add(stringRequest);

	}

	static private double bearing(double lat1, double long1, double lat2, double long2)
	{
		double degToRad = Math.PI / 180.0;
		double phi1 = lat1 * degToRad;
		double phi2 = lat2 * degToRad;
		double lam1 = long1 * degToRad;
		double lam2 = long2 * degToRad;

		return (Math.atan2(Math.sin(lam2-lam1)*Math.cos(phi2),
				(Math.cos(phi1)*Math.sin(phi2)) - (Math.sin(phi1)*Math.cos(phi2)*Math.cos(lam2-lam1))
				) * 180)/Math.PI;
	}

	private void parseJsonBusObject(String stringResponse) {
		Gson gson = new GsonBuilder().create();
		mtdDepartures = gson.fromJson(stringResponse, MTDDepartures.class);
		if (mtdDepartures.getDepartures().isEmpty()) {
			resultTextView.setText("No busses in this direction!");
		} else {
			MTDBus nextBus = mtdDepartures.getDepartures().get(0);
			String recognized = nextBus.getHeadsign() + " expected\nin " + nextBus.getExpected_mins() + "minutes";
			resultTextView.setText(recognized);
		}
	}


	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);

		//resultTextView.setText("Heading: " + Float.toString(degree) + " degrees " + tempCount + "\n");
		resultTextView.setText(targetStop + "\n" + currentDegree);
		float diff = degreeDistance(degree, degreeChange);
		currentDegree = degree;
		if((diff) > 5) {
			degreeChange = degree;
			findTargetStop();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}

	public float degreeDistance(float first, float second) {
		float rawdiff = Math.abs(first - second);
		rawdiff = rawdiff % 360;
		if(rawdiff > 180)
		{
			rawdiff = 360 - rawdiff;
		}
		return rawdiff;
	}

}
