package edu.illinois.mtdcompanion.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import android.os.Environment;
import android.util.Log;

public class ImageUploadUtility {


	/**
	 * Simple Utility Method gets called from other class to start uploading the image
	 * @param fileNameToUpload name of the file to upload
	 */
	public void uploadSingleImage(String fileNameToUpload){

    	try {
			doUploadinBackground(getBytesFromFile(new File(Environment.getExternalStorageDirectory(),fileNameToUpload)), fileNameToUpload);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Method uploads the image using http multipart form data.
	 * We are not using the default httpclient coming with android we are using the new from apache
	 * they are placed in libs folder of the application
	 *
	 * @param imageData
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	static boolean doUploadinBackground(final byte[] imageData, String filename) throws Exception{
        String responseString = null;
        PostMethod method;

        method = new PostMethod("http://vadtaldham.siyatechnologies.com/Geotagg/upload_image_email.php");

                org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
                client.getHttpConnectionManager().getParams().setConnectionTimeout(
                                100000);

                FilePart photo = new FilePart("userfile", new ByteArrayPartSource(
                        filename, imageData));

                photo.setContentType("image/jpeg");
                photo.setCharSet(null);
                String s    =   new String(imageData);
               Part[] parts = {
                                new StringPart("latitude", "123456"),
                                new StringPart("longitude","12.123567"),
                                new StringPart("imei","1234567899"),
                                new StringPart("to_email","gargidas19188@gmail.com"),
                                photo
                                };

                method.setRequestEntity(new MultipartRequestEntity(parts, method
                                .getParams()));

                client.executeMethod(method);
                responseString = method.getResponseBodyAsString();
                method.releaseConnection();

                Log.e("httpPost", "Response status: " + responseString);

        if (responseString.equals("SUCCESS")) {
                return true;
        } else {
                return false;
        }
    }


	/**
	 * Simple Reads the image file and converts them to Bytes
	 *
	 * @param file name of the file
	 * @return byte array which is converted from the image
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }

	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while ((offset < bytes.length)
	           && ((numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)) {
	        offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}


}