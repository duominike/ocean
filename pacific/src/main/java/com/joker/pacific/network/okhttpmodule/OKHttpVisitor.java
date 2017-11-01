package com.joker.pacific.network.okhttpmodule;

import com.joker.pacific.network.HttpRecponseCallback;
import com.joker.pacific.network.IHttpVisitor;
import com.joker.pacific.util.Md5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by joker on 17-10-31.
 */

public class OKHttpVisitor implements IHttpVisitor<RequestBody, Request.Builder> {
    private OkHttpClient sOKHttpClient = getOKHttpClient();
    private static OkHttpClient getOKHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        return builder.build();
    }
    public OKHttpVisitor(){

    }

    @Override
    public void addHeader(Request.Builder builder, String key, String value) {
        builder.addHeader(key, value);
    }

    public byte [] getBody() throws IOException{
        if(mResponseBody != null){
            return mResponseBody.bytes();
        }
        return null;
    }
    @Override
    public void doAysncGet(final String url, Request.Builder builder, final HttpRecponseCallback callback) {
        sOKHttpClient.newCall(builder.url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, url, null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    callback.onResponse(response.code(), url,  processResponse(response));
                }else{
                    callback.onError(response.code(), url, processResponse(response), null);
                }
            }
        });
    }

    private ResponseBody mResponseBody;
    private byte[] processResponse(Response response){
        byte data[] = null;
        try {
            data = response.body().bytes();
            mResponseBody = response.body();
        } catch (IOException e) {
//            response.close();
            return null;
        }

        if (response.header("X-GZIP") != null && response.header("X-GZIP").equalsIgnoreCase("true")) {
            data = decompress(data);
            if (data == null) {
                return null;
            }
        }
        if (response.header("X-CRC") != null && response.header("X-CRC").length() == 32) {
            if (!Md5.getMd5(data).equalsIgnoreCase(response.header("X-CRC"))) {
                return null;
            }
        }
//        response.close();
        return data;
    }

    private byte[] decompress(byte[] data) {
        byte[] output = null;

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        decompresser.end();
        return output;
    }

    class Rc4Encrypt {

        private int[] box = new int[256];
        private String key = null;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            byte[] k = key.getBytes();
            int i = 0, x = 0, t = 0, l = k.length;

            for (i = 0; i < 256; i++) {
                box[i] = i;
            }

            for (i = 0; i < 256; i++) {
                x = (x + box[i] + k[i % l]) % 256;

                t = box[x];
                box[x] = box[i];
                box[i] = t;
            }
            this.key = key;
        }

        private byte[] make(byte[] data) {
            int t, o, i = 0, j = 0, l = data.length;
            byte[] out = new byte[l];
            int[] ibox = new int[256];
            System.arraycopy(box, 0, ibox, 0, 256);

            for (int c = 0; c < l; c++) {
                i = (i + 1) % 256;
                j = (j + ibox[i]) % 256;

                t = ibox[j];
                ibox[j] = ibox[i];
                ibox[i] = t;

                o = ibox[(ibox[i] + ibox[j]) % 256];
                out[c] = (byte) (data[c] ^ o);
            }
            return out;
        }

        public byte[] encrypt(byte data[]) {
            return make(data);
        }

        public byte[] decrypt(byte data[]) {
            return make(data);
        }

    }

    @Override
    public void doSyncGet(String url, Request.Builder builder, HttpRecponseCallback callback) {
        try{
            Response response = sOKHttpClient.newCall(builder.url(url).build()).execute();
            if(response.code() == 200){
                callback.onResponse(response.code(), url, processResponse(response));
            }else{
                callback.onError(response.code(), url, processResponse(response), null);
            }
        }catch (IOException e){
            callback.onError(0, url, null, e);
        }
    }

    @Override
    public void doSyncPost(final String url, Request.Builder builder,
                           RequestBody requestBody, final HttpRecponseCallback callback) {
        Request request = builder.url(url).post(requestBody).build();
        try{
            Response response = sOKHttpClient.newCall(request).execute();
            if(response.code() == 200){
                callback.onResponse(response.code(), url, processResponse(response));
            }else{
                callback.onError(response.code(), url, processResponse(response), null);
            }
        }catch (IOException e){
            callback.onError(0, url, null, e);
        }
    }

    @Override
    public void doAsyncPost(final String url, Request.Builder builder, RequestBody requestBody,
                            final HttpRecponseCallback callback) {
        Request request = builder.url(url).post(requestBody).build();
        sOKHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, url, null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    callback.onResponse(response.code(), url, processResponse(response));
                }else{
                    callback.onError(response.code(), url, processResponse(response), null);
                }
            }
        });
    }
}
