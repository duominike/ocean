package com.joker.pacific.network;

/**
 * Created by joker on 17-10-31.
 */

public interface HttpRecponseCallback {
    void onResponse(int statusCode, String url, byte[] data);
    void onError(int errorCode, String url, byte[]errorMsg, Throwable throwable);
}
