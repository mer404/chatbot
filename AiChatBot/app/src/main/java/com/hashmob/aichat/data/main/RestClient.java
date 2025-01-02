package com.hashmob.aichat.data.main;import androidx.annotation.NonNull;import java.io.File;import okhttp3.MediaType;import okhttp3.RequestBody;public class RestClient {    /**     * MULTIPLE IMAGE UPLOAD WITH MULTIPART     */    private static final String MULTIPART_FORM_DATA = "multipart/form-data";    public static RequestBody createRequestBody(@NonNull String s) {        return RequestBody.create(s, MediaType.parse(MULTIPART_FORM_DATA));    }    public static RequestBody createRequestBody(@NonNull File file) {        return RequestBody.create(file, MediaType.parse(MULTIPART_FORM_DATA));    }    public static String USER_AGENT = "container1102";    public static String APP_SECRET = "boss#1407";    public static String VERSION_NAME = "v1";    public static String DEVICE_TYPE_ANDROID = "android";    public static String CONTENT_TYPE = "application/json";}