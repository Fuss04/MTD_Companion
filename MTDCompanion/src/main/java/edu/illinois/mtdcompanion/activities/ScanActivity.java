package edu.illinois.mtdcompanion.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import edu.illinois.mtdcompanion.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.ScannerSession;

public class ScanActivity extends Activity implements ScannerSession.Listener {

	private int ScanOptions = Result.Type.IMAGE;
	private int ScanExtras = Result.Extra.CORNERS | Result.Extra.WARPED_IMAGE;

	private ScannerSession session;
	private TextView resultTextView;

	private OCRHelper ocr;
	private Boolean done;
	private Boolean found_code;
	private String recognized;
	private String stop_id;

	private RequestQueue mRequestQueue;
	private MTDBusObject mtdBus;

	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		mRequestQueue = Volley.newRequestQueue(this);
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
		resultTextView.setText("Scan result: N/A");

		flag = 0;

		ocr = new OCRHelper();
		done = false;
		found_code = false;
		recognized = "";
		stop_id = "NA";
	}

	@Override
	protected void onResume() {
		super.onResume();

		flag = 0;
		// start the scanner session
		session.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		done = false;
		found_code = false;
		recognized = "";
		stop_id = "NA";
		// pause the scanner session
		session.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		done = false;
		found_code = false;
		recognized = "";
		stop_id = "NA";
		ocr.close();

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
		if (!done && (result != null)) {
			//flag++;
			//float[] c = result.getCorners();
			//resultTextView.setText(String.format("Scan result: %s\n(%f, %f)\n(%f, %f)\n(%f, %f)\n(%f, %f)", result.getValue(), c[0], c[1], c[2], c[3], c[4], c[5], c[6], c[7]));

			Bitmap frame = result.getWarped();
			recognized = ocr.getText(frame);
			resultTextView.setText(recognized);

			if (recognized.equals("mtd1327")) {
				done = true;

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

			// gets 10th frame
			// if (flag == 10) {
			// Bitmap croppedFrame = OCRHelper.cropToArea(frame, 350, 45, 64, 30);
			// File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			// File file = new File(path, "DemoPicture.png");
			// try {
			// 	path.mkdirs();
			// 	OutputStream os = new FileOutputStream(file);
			// 	croppedFrame.compress(Bitmap.CompressFormat.PNG, 90, os);
			// 	os.close();

			// 	MediaScannerConnection.scanFile(this,
			// 			new String[] { file.toString() }, null,
			// 			new MediaScannerConnection.OnScanCompletedListener() {
			// 		public void onScanCompleted(String path, Uri uri) {
			// 			Log.i("ExternalStorage", "Scanned " + path + ":");
			// 			Log.i("ExternalStorage", "-> uri=" + uri);
			// 		}
			// 	});
			// } catch (IOException e) {
			// 	Log.w("ExternalStorage", "Error writing " + file, e);
			// }
			// }
		}
		else {
			//resultTextView.setText("Scan result: N/A");
		}
		/*
		if (!done && result != null) {
			if (!found_code) {
				Bitmap frame = result.getWarped();
				recognized = ocr.getText(frame);
				if (found_code = db.exists(recognized)) {
					stop_id = db.lookup(recognized);
				}
			}
			resultTextView.setText(String.format("OCR: %s\nID: %s", recognized, stop_id));
		}
		 */
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
			expectedTimeInMinutes = jsonObject.getJSONArray("departures").getJSONObject(0).getInt("expected_mins");
			// expectedTime = jsonObject.getJSONArray("departures").getJSONObject(0).getString("expected");
			// stopID = jsonObject.getJSONArray("departures").getJSONObject(0).getString("stop_id");
			headSign = jsonObject.getJSONArray("departures").getJSONObject(0).getString("headsign");

			resultTextView.setText(headSign + " expected\nin " + expectedTimeInMinutes + "minutes");
		} catch (JSONException error) {
			error.printStackTrace();
			resultTextView.setText("ERROR");
		}
	}

}