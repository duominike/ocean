package com.joker.ocean.rxjava.rxjava2;


import android.os.Handler;

import com.joker.pacific.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by joker on 17-10-23.
 */

public class Rxjava2Test {
    private Logger mLogger = Logger.getLogger(Rxjava2Test.class);
    private List<String> mDatas = new ArrayList<String>();
    private Handler mHandler = new Handler();

    public void init(){
        mDatas.add("one");
        mDatas.add("two");
        mDatas.add("three");
        mDatas.add("four");
        mDatas.add("five");
        mDatas.add("six");
    }
    public void testObservableJust(){
        Observable.just("hello world").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mLogger.info("onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                mLogger.info("onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mLogger.info("onError");
            }

            @Override
            public void onComplete() {
                mLogger.info("onComplete");
            }
        });
    }

    public void testObservableFrom(){
        Observable.fromIterable(mDatas).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mLogger.info("onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                mLogger.info("onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mLogger.info("onError");
            }

            @Override
            public void onComplete() {
                mLogger.info("onComplete");
            }
        });
    }


    public void testObservableRange(){
        Observable.range(3, 10).subscribe(new Observer<Integer>() {
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
                mLogger.info("onError");
            }

            @Override
            public void onComplete() {
                mLogger.info("onComplete");
            }
        });
    }

    public void testObservableInterval(){
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testObservableInterval onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        mLogger.info("testObservableInterval currentThread: " + Thread.currentThread().getName());
                        mLogger.info("testObservableInterval onNext: " + mDatas.get(0));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("testObservableInterval onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testObservableInterval onComplete");
                    }
                });


    }

    public void testObservableTimer(){
        mLogger.info("testObservableTimer");
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testObservableTimer onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        mLogger.info("testObservableTimer currentThread: " + Thread.currentThread().getName());
                        mLogger.info("testObservableTimer onNext: " + mDatas.get(0));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("testObservableTimer onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testObservableTimer onComplete");
                    }
                });
    }

    public void singleDemo(){
        Single.just(5).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void maybeDemo(){
        Maybe.just(10%2 ==0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if(aBoolean){

                        }else{

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void completeDemo(){
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                Thread.sleep(1000);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("completeDemo -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("completeDemo -- onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("completeDemo -- onError");
                    }
                });
    }

    public void collectSubscription(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Flowable.range(1, 10).subscribeWith(new ResourceSubscriber(){
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }


}
