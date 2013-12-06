package edu.illinois.mtdcompanion.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

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

import edu.illinois.mtdcompanion.R;
import edu.illinois.mtdcompanion.helpers.FileUploadCallback;
import edu.illinois.mtdcompanion.helpers.FileUploadFacade;
import edu.illinois.mtdcompanion.helpers.ImageUploadUtility;
import edu.illinois.mtdcompanion.models.MTDBus;
import edu.illinois.mtdcompanion.models.MTDDepartures;
import edu.illinois.mtdcompanion.models.MTDOCRData;

public class ScanActivity extends Activity implements ScannerSession.Listener {

	private int ScanOptions = Result.Type.IMAGE;
	private int ScanExtras = Result.Extra.CORNERS | Result.Extra.WARPED_IMAGE;

	private ScannerSession session;
	private TextView resultTextView;

	private Boolean done;
	private String recognized;

	private RequestQueue mRequestQueue;
	private MTDDepartures mtdDepartures;
	private MTDOCRData mtdOCRData;

	private int flag;

	private ProgressDialog simpleWaitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

		mRequestQueue = Volley.newRequestQueue(this);
		mtdDepartures = new MTDDepartures();

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

		done = false;
		recognized = "";
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
		recognized = "";
		// pause the scanner session
		session.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		done = false;
		recognized = "";

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
			flag++;

			// Waits 10 frames for camera to focus
			if (flag >= 10) {
				Bitmap frame = result.getWarped();

				// 1. crop frame
				// 2. save as png
				// 3. send png to server
				// 4. get code on response
				// 5. check if code is in database
				//     -No : return (will get repeat 1-5 on next frame)
				//     -Yes: done = true; goto 6
				// 6. get next bus time - make sure to check for no busses
				//     - set resultTextView.setText(recognized);

				frame = frame.copy(Bitmap.Config.ARGB_8888, true);
				frame = cropToArea(frame, Constants.CROP_X, Constants.CROP_Y, Constants.CROP_W, Constants.CROP_H);
				resultTextView.setText("Cropped");

				File png = saveToPng(frame); // TODO null check
				resultTextView.setText("Saved");

				sendPng(png,
						new FileUploadCallback() {
				    		@Override
				    		public void onSuccess(int code, String response) {
								String stopCode = parseJsonTextObject(response);
								if (stopCode == null) {
									resultTextView.setText("stopCode == null");
									return;
								}
								String stopId = lookUpIdFromCode(stopCode);
								if (stopId == null) {
									resultTextView.setText("stopId == null");
									return;
								}

								done = true;
								getNextBus(stopId);
				    		}

				    		@Override
				    		public void onFailure(int code, String response, Throwable e) {
				    			resultTextView.setText(e.getCause().toString());
				    		}
						});
			}
		}
		else {
			//resultTextView.setText("Scan result: N/A");
		}
	}

	@Override
	public void onScanFailed(MoodstocksError error) {
		resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	}

	private Bitmap cropToArea(Bitmap frame, int x, int y, int width, int height) {
		return Bitmap.createBitmap(frame, x, y, width, height);
	}

	private File saveToPng(Bitmap frame) {
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File file = new File(path, "TextFrame.png");
		try {
			path.mkdirs();
			OutputStream os = new FileOutputStream(file);
			frame.compress(Bitmap.CompressFormat.PNG, 90, os);
			os.close();

			MediaScannerConnection.scanFile(this,
					new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
				@Override
				public void onScanCompleted(String path, Uri uri) {
					Log.i("ExternalStorage", "Scanned " + path + ":");
					Log.i("ExternalStorage", "-> uri=" + uri);
				}
			});
		} catch (IOException e) {
			Log.w("ExternalStorage", "Error writing " + file, e);
			return null;
		}

		return file;
	}

	private class ImageUploaderTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected void onPreExecute(){
			simpleWaitDialog = ProgressDialog.show(ScanActivity.this, "Wait", "Uploading Image");
		}

		@Override
		protected Void doInBackground(String... params) {
			new ImageUploadUtility().uploadSingleImage(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result){
			simpleWaitDialog.dismiss();
		}
	}

	private void sendPng(File png, FileUploadCallback callback) {
//		String url = Constants.OCR_SERVER;
//		MultipartRequest fileRequest = new MultipartRequest(url, listener, errorListener, png);
//		mRequestQueue.add(fileRequest);
//		resultTextView.setText("Sent");

		FileUploadFacade fileUploadFacade = new FileUploadFacade();
		String url = Constants.OCR_SERVER;
		fileUploadFacade.post(url, png, callback);
	}

	private String parseJsonTextObject(String stringResponse) {
		Gson gson = new GsonBuilder().create();
		mtdOCRData = gson.fromJson(stringResponse, MTDOCRData.class);

		if (mtdOCRData.isValid()) {
			resultTextView.setText(mtdOCRData.getStopCode());
			return mtdOCRData.getStopCode();
		}
		else {
			return null;
		}
	}

	private String lookUpIdFromCode(String code) {
		boolean inDatabase = (code == "6051");
		String id = "GWNMN";

		if (inDatabase) {
			return id;
		}
		else {
			return null;
		}
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

	private void parseJsonBusObject(String stringResponse) {
		Gson gson = new GsonBuilder().create();
		mtdDepartures = gson.fromJson(stringResponse, MTDDepartures.class);
		if (mtdDepartures.getDepartures().isEmpty()) {
			resultTextView.setText("ERROR");
		} else {
			MTDBus nextBus = mtdDepartures.getDepartures().get(0);
			recognized = nextBus.getHeadsign() + " expected\nin " + nextBus.getExpected_mins() + "minutes";
			resultTextView.setText(recognized);
		}
	}
}
