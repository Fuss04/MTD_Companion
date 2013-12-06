package edu.illinois.mtdcompanion.activities;

import android.graphics.Bitmap;

public class OCRHelper {

	public static final String PACKAGE_NAME = "com.hello.ms";
	public static final String lang = "eng";

	public String getText(Bitmap frame) {
		frame = frame.copy(Bitmap.Config.ARGB_8888, true);
		frame = cropToArea(frame, 350, 45, 64, 30);

		// Send bitmap to server and get recognized frame
		String recognizedText = "Temp";
		return recognizedText;
	}

	public static Bitmap cropToArea(Bitmap frame, int x, int y, int width, int height) {
		return Bitmap.createBitmap(frame, x, y, width, height);
	}
}