package com.hello.ms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OCRHelper {

	public static final String PACKAGE_NAME = "com.hello.ms";
	public static final String DATA_PATH = "/path/that/I/dont/know";
	public static final String lang = "eng";

	TessBaseAPI baseApi;

    OCRHelper() {
    	TessBaseAPI baseApi = new TessBaseAPI();
		// DATA_PATH = Path to the storage
		// lang = for which the language data exists, usually "eng"
		// Eg. baseApi.init("/mnt/sdcard/tesseract/tessdata/eng.traineddata", "eng");
		baseApi.init(DATA_PATH, lang);
    }

    public String getText(Bitmap frame) {
    	frame = frame.copy(Bitmap.Config.ARGB_8888, true);
    	frame = cropToArea(frame, 0, frame.getWidth(), 0, frame.getHeight());
		baseApi.setImage(frame);
		return baseApi.getUTF8Text();
	}

	private Bitmap cropToArea(Bitmap frame, int x1, int x2, int y1, int y2) {
		return frame;
	}

	public void close() {
		baseApi.end();
	}
}