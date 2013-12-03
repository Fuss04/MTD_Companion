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
	private String DATA_PATH;
	public static final String lang = "eng";

	TessBaseAPI baseApi;

    OCRHelper() {
    	TessBaseAPI baseApi = new TessBaseAPI();
		// DATA_PATH = Path to the storage
		// lang = for which the language data exists, usually "eng"
		// Eg. baseApi.init("/mnt/sdcard/tesseract/tessdata/eng.traineddata", "eng");
        DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tessdata/eng.traineddata";
		baseApi.init(DATA_PATH, lang);
    }

    public String getText(Bitmap frame) {
    	frame = frame.copy(Bitmap.Config.ARGB_8888, true);
    	frame = cropToArea(frame, 350, 45, 64, 30);
		baseApi.setImage(frame);
        String recognizedText = baseApi.getUTF8Text();
        // do analysis on recognized text before returning
		return recognizedText;
	}

	public static Bitmap cropToArea(Bitmap frame, int x, int y, int width, int height) {
        return Bitmap.createBitmap(frame, x, y, width, height);
	}

	public void close() {
		baseApi.end();
	}
}