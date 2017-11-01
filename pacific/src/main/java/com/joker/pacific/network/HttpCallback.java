package com.joker.pacific.network;

import com.joker.pacific.network.okhttpmodule.ProtoRsp;

/**
 * Created by joker on 17-10-31.
 */

public interface HttpCallback {
   void onRsp(ProtoRsp rsp);
   void onError(int errorCode, Throwable throwable);
   boolean IsCallable();
}
