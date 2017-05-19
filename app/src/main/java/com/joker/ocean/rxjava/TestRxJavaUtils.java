package com.joker.ocean.rxjava;

import com.joker.pacific.log.Logger;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by joker on 17-5-19.
 */

public class TestRxJavaUtils {
    private Logger mLogger = Logger.getLogger(TestRxJavaUtils.class);
    private static TestRxJavaUtils sInstance;
    private TestRxJavaUtils(){

    }

    public static synchronized TestRxJavaUtils getInstance(){
        if(sInstance == null){
            sInstance = new TestRxJavaUtils();
        }
        return sInstance;
    }

    public void testRxJava1(){
        Observable.create(new Observable.OnSubscribe<Integer>(){
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i =0; i< 5; i++){
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                mLogger.info("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                mLogger.info("onError");
            }

            @Override
            public void onNext(Integer integer) {
                mLogger.info("onNext item is: " + integer);
            }
        });
    }


}
