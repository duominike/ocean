package com.joker.ocean.net;

import com.google.gson.JsonElement;
import com.joker.pacific.network.okhttpmodule.ProtoRsp;

/**
 * Created by joker on 17-10-31.
 */

public class GetConfRsp extends ProtoRsp{
    private JsonElement configure;

    public JsonElement getConfigure() {
        return configure;
    }

    public void setConfigure(JsonElement configure) {
        this.configure = configure;
    }
}
