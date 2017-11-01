package com.joker.pacific.network;

/**
 * Created by joker on 17-10-31.
 */

public interface IHttpVisitor<T, R>{
    void doAysncGet(String url, R requestBuilder, HttpRecponseCallback callback);
    void doSyncGet(String url, R requestBuilder, HttpRecponseCallback callback);
    void doSyncPost(String url,R requestBuilder, T requestBody,  HttpRecponseCallback callback);
    void doAsyncPost(String url,R requestBuilder, T requestBody,   HttpRecponseCallback callback);
    void addHeader(R r, String key, String value);
}
