package com.joker.ocean;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by joker on 17-10-10.
 */

public class SimpleIntentService extends IntentService{
    public SimpleIntentService(String name){
        super(name);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
