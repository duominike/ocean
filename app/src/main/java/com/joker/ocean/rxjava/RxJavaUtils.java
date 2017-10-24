package com.joker.ocean.rxjava;


import com.joker.pacific.log.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 17-10-24.
 */

public class RxJavaUtils {
    private Logger mLogger = Logger.getLogger(RxJavaUtils.class);

    private boolean isMemCache(){
        return false;
    }

    private boolean isDiskCache(){
        return true;
    }

    private boolean isNetWorkCache(){
        return true;
    }
    /**
     * 假设一个页面有多个请求，希望请求结果按顺序给出
     */
    public void demoReqDenpend() {
        Observable<Object> observable1 = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                Thread.sleep(300);
                e.onNext("hello");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<Object> observable2 = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                Thread.sleep(150);
                e.onNext("7");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        Observable.concat(observable1, observable2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        mLogger.info("demoReqDenpend -- onNext: " + o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("demoReqDenpend -- onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("demoReqDenpend -- onComplete");
                    }
                });
    }

    /**
     * 假设请求２依赖请求１的请求结果。。
     */
    public void testSeriReqDepend() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                mLogger.info("subscribe1: -->> " + Thread.currentThread().getName());
                Thread.sleep(500);
                e.onNext("hello");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                                mLogger.info("subscribe2: -->> " + Thread.currentThread().getName());
                                Thread.sleep(500);
                                e.onNext(11);
                                e.onComplete();
                            }
                        });
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        mLogger.info("onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 多个条件，有一个条件为真就返回
     */
    public void testConditionReq(){
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if(isMemCache()){
                    e.onNext("memCache");
                }else{
                    e.onComplete();
                }
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if(isDiskCache()){
                    e.onNext("diskCache");
                }else{
                    e.onComplete();
                }
            }
        });

        Observable<String> observable3 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if(isNetWorkCache()){
                    e.onNext("newworkCache");
                }else{
                    e.onComplete();
                }
            }
        });

        Observable.concat(observable1, observable2, observable3)
                .first("null")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mLogger.info("testConditionReq accept: " + s);
                    }
                });
    }

    /**
     * 一个页面有多个请求，想等请求的结果都返回的时候再刷新页面, merge不保证顺序，　可用于对于请求结果顺序无要求的情况，
     */
    public void testSameTimeNotify(){
        Observable<Object> observable1 = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                Thread.sleep(300);
                mLogger.info("testSameTimeNotify observable1 public data");
                e.onNext("hello");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<Object> observable2 = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                Thread.sleep(150);
                mLogger.info("testSameTimeNotify observable2 public data");
                e.onNext("7");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        Observable.merge(observable1, observable2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        mLogger.info("testSameTimeNotify -- onNext: " + o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("testSameTimeNotify -- onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testSameTimeNotify -- onComplete");
                    }
                });
    }
}
