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

public class ScanActivity extends Activity implements ScannerSession.Listener {

	private int ScanOptions = Result.Type.IMAGE;
	private int ScanExtras = Result.Extra.CORNERS;

	private ScannerSession session;
	private TextView resultTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan);

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
			resultTextView.setText(String.format("Scan result: %s\n(%f, %f)\n(%f, %f)\n(%f, %f)\n(%f, %f)", result.getValue(), c[0], c[1], c[2], c[3], c[4], c[5], c[6], c[7]));
		}
		else {
			resultTextView.setText("Scan result: N/A");
		}

	}

	@Override
	public void onScanFailed(MoodstocksError error) {
		resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	}

}