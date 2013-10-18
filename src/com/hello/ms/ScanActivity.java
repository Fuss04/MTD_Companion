package com.hello.ms;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.view.SurfaceView;
import android.util.Log;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.ScannerSession;
import com.moodstocks.android.Result;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class ScanActivity extends Activity implements ScannerSession.Listener {

	private int ScanOptions = Result.Type.IMAGE;
	private int ScanExtras = Result.Extra.CORNERS;
	
	private RequestQueue mRequestQueue;

	private MTDBusObject mtdBus;

	private ScannerSession session;
	private TextView resultTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		mRequestQueue =  Volley.newRequestQueue(this);

		mtdBus = new MTDBusObject();

    	// get the camera preview surface & result text view
		SurfaceView preview = (SurfaceView) findViewById(R.id.preview);

   		// Create a scanner session
		try {
			session = new ScannerSession(this, this, preview);
		} catch (MoodstocksError e) {
			e.log();
		}

    	// set session options
		session.setOptions(ScanOptions);
		session.setExtras(ScanExtras);

		resultTextView = (TextView) findViewById(R.id.scan_result);
		// resultTextView.setText("Scan result: N/A");

	}

	@Override
	protected void onResume() {
		super.onResume();

    	// start the scanner session
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
		if (result != null) {
			float[] c = result.getCorners();
			// resultTextView.setText(String.format("Scan result: %s\n(%f, %f)\n(%f, %f)\n(%f, %f)\n(%f, %f)", result.getValue(), c[0], c[1], c[2], c[3], c[4], c[5], c[6], c[7]));

			String url = Constants.MTD_BASE_URL + Constants.MTD_VERSION + Constants.MTD_FORMAT + Constants.MTD_METHOD_GET_DEPARTURES_BY_STOP + Constants.MTD_KEY + Constants.STOP_ID_PARAMETER + "GWNMN";

			JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            	@Override
	            public void onResponse(JSONObject response) {
	            	parseJsonObject(response);
	            }
	        }, new Response.ErrorListener() {
	 
	            @Override
	            public void onErrorResponse(VolleyError error) {
	 
	            }
	        });
	 
	        mRequestQueue.add(jsObjRequest);

		}
		else {
			
		}

	}

	@Override
	public void onScanFailed(MoodstocksError error) {
		resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	}


	private void parseJsonObject(JSONObject jsonObject) {
		int expectedTimeInMinutes = -1;
		String expectedTime = "NONE";
		String stopID = "NONE";
		String headSign = "NONE";
		try {
			// expectedTimeInMinutes = jsonObject.getJSONArray("departures").getJSONObject(0).getInt("expected_mins");
			// expectedTime = jsonObject.getJSONArray("departures").getJSONObject(0).getString("expected");
			stopID = jsonObject.getJSONArray("departures").getJSONObject(0).getString("stop_id");
			// headSign = jsonObject.getJSONArray("departures").getJSONObject(0).getString("headsign");

			resultTextView.setText("StopID: " + stopID);
		} catch (JSONException error) {
			error.printStackTrace();
			resultTextView.setText("ERROR");
		}
	}

}