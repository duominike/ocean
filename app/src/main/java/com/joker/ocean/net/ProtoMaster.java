package com.joker.ocean.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joker.pacific.log.Logger;
import com.joker.pacific.network.HttpCallback;
import com.joker.pacific.network.HttpRecponseCallback;
import com.joker.pacific.network.IHttpVisitor;
import com.joker.pacific.network.okhttpmodule.OKHttpVisitor;
import com.joker.pacific.network.okhttpmodule.ProtoRsp;
import com.joker.pacific.util.SystemInfoUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by joker on 17-10-31.
 */

public class ProtoMaster {
    private Logger mLogger = Logger.getLogger(ProtoMaster.class);
    private IHttpVisitor<RequestBody, Request.Builder> mHttpVisitor;
    private String domain = "http://www.baidu.com";
    private String loadConfUrl = "/api/client/getConfig.htm?parameter=%s";
    private Context mContext;
    public ProtoMaster(Context context){
        this.mContext = context;
        mHttpVisitor = new OKHttpVisitor();
    }


    private void prepareBuilder(Request.Builder builder){
        builder.addHeader("X-CID", SystemInfoUtil.getMid(mContext));
        builder.addHeader("X-PRODUCT", "ocean");
        builder.addHeader("X-VER", "3.11.0.0");
        builder.addHeader("X-OS", SystemInfoUtil.getOSVersion());
        builder.addHeader("X-CHANNEL", "9999");

        builder.addHeader("X-MODEL", SystemInfoUtil.getMobileModel());

        builder.addHeader("X-PLATFORM", "Android");
        builder.addHeader("Accept-Encoding", "compress,gzip");
    }
    public void getConf(){
        GetConfParam param = new GetConfParam();
        param.setAppVer(SystemInfoUtil.getVersionName(mContext));
        param.setChannelID("9999");
        param.setMobileModel(SystemInfoUtil.getMobileModel());
        param.setMobileOSVer(SystemInfoUtil.getOSVersion());
        param.setMobileType("Android");
        try{
            String url =  getLoadConfUrl(param);
            Request.Builder builder = new Request.Builder();
            prepareBuilder(builder);
            doAysncGet(url, builder, new HttpCallback() {
                @Override
                public void onRsp(ProtoRsp rsp) {
                    try{
                        byte[] data = ((OKHttpVisitor)mHttpVisitor).getBody();
                        String string = new String(data, "utf-8");
                    }catch (IOException e){

                    }
                }

                @Override
                public void onError(int errorCode, Throwable throwable) {

                }

                @Override
                public boolean IsCallable() {
                    return false;
                }
            });
        }catch (Exception e){

        }
    }


    private void doAysncGet(String url, Request.Builder builder, HttpCallback callback){
        mHttpVisitor.doAysncGet(url, builder, new HttpRecponseCallback() {
            @Override
            public void onResponse(int statusCode, String url, byte[] data) {
                String JSON = null;
                try {
                    JSON = new String(data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                ProtoRsp rsp = null;
                try{
                    rsp = gson.fromJson(JSON, new TypeToken<GetConfRsp>(){}.getType());
                }catch (Exception e){

                }
                callback.onRsp(rsp);
            }

            @Override
            public void onError(int errorCode, String url, byte[] errorMsg, Throwable throwable) {
                mLogger.info("onError: " + errorCode);
                callback.onError(errorCode, throwable);
            }
        });
    }

    private String getLoadConfUrl(GetConfParam param) throws UnsupportedEncodingException{
        return domain + String.format(loadConfUrl, URLEncoder.encode(new Gson().toJson(param), "UTF-8"));
    }


}
