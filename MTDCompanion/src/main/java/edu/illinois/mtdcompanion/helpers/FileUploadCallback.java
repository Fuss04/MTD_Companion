package edu.illinois.mtdcompanion.helpers;

public interface FileUploadCallback {
    void onSuccess(int statusCode, String response);
    void onFailure(int statusCode, String response, Throwable e);
}
