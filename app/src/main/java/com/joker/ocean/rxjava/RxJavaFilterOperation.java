package com.joker.ocean.rxjava;


import android.os.Handler;
import android.os.Message;

import com.joker.pacific.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by joker on 17-9-26.
 */

public class RxJavaFilterOperation {
    private Logger mLogger = Logger.getLogger(RxJavaFilterOperation.class);
    private List<String> datas = new ArrayList<String>();
    private List<String> randomTimeDatas = new ArrayList<>();
    private Random mRandom = new Random();
    private Random mTimeRandom = new Random();
    private boolean start = false;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mLogger.info("handleMessage");
//            mObservable.subscribe();
//            mHandler.sendEmptyMessageDelayed(1, 50 * mRandom.nextInt(4));
           return false;
        }
    });

    public RxJavaFilterOperation() {
        datas.add("one");
        datas.add("two");
        datas.add("three");
        datas.add("four");
        datas.add("five");
        datas.add("six");
        datas.add("seven");
        datas.add("eight");
        datas.add("nine");
        datas.add("ten");
        datas.add("eleven");
        datas.add("twenteen");
        datas.add("thirtyeen");
    }


    private void initDataByHandlerRandom() {
        randomTimeDatas.clear();
        randomTimeDatas.add(datas.get(0));
        randomTimeDatas.add(datas.get(1));
        start = true;
        mHandler.sendEmptyMessageDelayed(1, 50);
    }

    public void testFilter() {
        Observable.from(datas).filter((data) -> data.startsWith("t"))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testFilter -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testFilter --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testFilter --onNext -> " + s);
                    }
                });
    }

    public void testTake() {
        Observable.from(datas).take(4)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testTake -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testTake --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testTake --onNext -> " + s);
                    }
                });
    }

    public void testTakeLast() {
        Observable.from(datas).takeLast(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testTakeLast -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testTakeLast --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testTakeLast --onNext -> " + s);
                    }
                });
    }

    public void testDistinct() {
        Observable.from(datas).distinct()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testDistinct -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testDistinct --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testDistinct --onNext -> " + s);
                    }
                });
    }

    public void testDistinctUntilsChanged() {
        Observable.from(datas).distinctUntilChanged()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testDistinctUntilsChanged -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testDistinctUntilsChanged --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testDistinctUntilsChanged --onNext -> " + s);
                    }
                });
    }


    public void testSkip() {
        Observable.from(datas).skip(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testSkip -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testSkip --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testSkip --onNext -> " + s);
                    }
                });
    }

    public void testSkipLast() {
        Observable.from(datas).skipLast(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testSkipLast -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testSkipLast --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testSkipLast --onNext -> " + s);
                    }
                });
    }

    public void testElementAt() {
        Observable.from(datas).elementAt(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testElementAt -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testElementAt --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testElementAt --onNext -> " + s);
                    }
                });
    }

    /**
     * 我们可以使用一个小的发射间隔。在Observable后面加
     * 一个 sample() ,我们将创建一个新的可观测序列,它将在一个指定的时间间隔里
     * 由Observable发射最近一次的数值:
     */
    public void testSmpling() {
        initDataByHandlerRandom();
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mLogger.info("testSmpling call : " + Thread.currentThread().getName());
                while (start){
                    subscriber.onNext(datas.get(mRandom.nextInt(11)));
                }
                subscriber.onCompleted();
            }
        });
        observable.subscribeOn(Schedulers.newThread()).
                sample(200, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testSmpling -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testSmpling --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("currentTread: " + Thread.currentThread().getName());
                        mLogger.info("testSmpling --onNext -> " + s);
                    }
                });
    }

    /**
     * 就是在我们设定的时间间隔内如果没有得到一个值则发射一个错误
     */
    public void testTimeOut() {
        initDataByHandlerRandom();
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if(!start){
                    subscriber.onNext("Data Coming");
                }
            }
        });
        observable.timeout(2, TimeUnit.SECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        mLogger.info("testTimeOut -- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testTimeOut --onError");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testTimeOut --onNext -> " + s);
                    }
                });
    }

    /**
     * debounce() 函数过滤掉由Observable发射的速率过快的数据;如果在一个指定
     * 的时间间隔过去了仍旧没有发射一个,那么它将发射最后的那个。
     */
    private int i = 0;
    private Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            while (start){
                if(i >= 13){
                    i = 0;
                }
                subscriber.onNext(datas.get(i++));

                try{
                    int sleepTime = 50 * mRandom.nextInt(4);
                    mLogger.info("sleepTime: " + sleepTime);
                    Thread.currentThread().sleep(sleepTime);
                }catch (InterruptedException e){

                }

            }
        }
    });
    public void testDebounce() {
//        initDataByHandlerRandom();
        start = true;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                while (start){
                    if(i >= 13){
                        i = 0;
                    }
                    subscriber.onNext(datas.get(i++));

                    try{
                        int sleepTime = 50 * mRandom.nextInt(4);
                        mLogger.info("sleepTime: " + sleepTime);
                        Thread.currentThread().sleep(sleepTime);
                    }catch (InterruptedException e){

                    }

                }
            }
        }).subscribeOn(Schedulers.newThread())
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.info("testDebounce onError: ");
                    }

                    @Override
                    public void onNext(String s) {
                        mLogger.info("testDebounce onNext: " + s);
                    }
                });
    }

}
