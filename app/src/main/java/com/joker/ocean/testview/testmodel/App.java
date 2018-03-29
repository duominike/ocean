package com.joker.ocean.testview.testmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.joker.ocean.BR;

/**
 * Created by joker on 2018/3/29.
 */

public class App extends BaseObservable {
    public String name = "";
    public String packagename = "com.joker.ocean";

    public App(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
        notifyPropertyChanged(BR.name);
    }
}
