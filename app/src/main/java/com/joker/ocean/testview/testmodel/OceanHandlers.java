package com.joker.ocean.testview.testmodel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by joker on 2018/3/29.
 */

public class OceanHandlers {
    public void onClickTitle(View view){
        Log.i("OceanHandlers", "onClickTitle");
        Toast.makeText(view.getContext(), "click title", Toast.LENGTH_SHORT).show();
    }
}
