package com.joker.ocean.temp;

import android.app.Activity;
import android.content.pm.ApplicationInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 17-11-8.
 */

public class LoadApkUtils {
    public static Observable<ApplicationInfo> getInstalledApks(Activity activity) {
        return Observable.fromIterable(activity.getPackageManager().getInstalledApplications(0))
                .filter(new Predicate<ApplicationInfo>() {
                    @Override
                    public boolean test(@NonNull ApplicationInfo applicationInfo) throws Exception {
                        return !((ApplicationInfo.FLAG_SYSTEM & applicationInfo.flags) != 0);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
