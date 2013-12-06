package edu.illinois.mtdcompanion.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(edu.illinois.mtdcompanion.R.layout.activity_splash);

		Thread background = new Thread() {
			@Override
			public void run() {

				try {
					sleep(5*1000);
					Intent i=new Intent(getBaseContext(), MainActivity.class);
					startActivity(i);
					finish();

				} catch (Exception e) {
				}
			}
		};

		background.start();
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();

	}
}
