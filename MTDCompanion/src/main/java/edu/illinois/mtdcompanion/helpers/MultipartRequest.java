package edu.illinois.mtdcompanion.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

public class MultipartRequest extends Request<String> {

	private MultipartEntityBuilder builder;
	private final HttpEntity entity;

	private static final String FILE_PART_NAME = "file";

	private final Response.Listener<String> mListener;
	private final File mFilePart;

	public MultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, File file)
	{
		super(Method.POST, url, errorListener);

		builder = MultipartEntityBuilder.create();
	    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		mListener = listener;
		mFilePart = file;
		builder.addPart(FILE_PART_NAME, new FileBody(mFilePart));
		entity = builder.build();
	}

	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody()	throws AuthFailureError{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try	{
			entity.writeTo(bos);
		}
		catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected void deliverResponse(String response)	{
		mListener.onResponse(response);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		return Response.success(new String(response.data), getCacheEntry());
	}
}