package com.joker.ocean.rxjava.rxjava2;

import com.joker.ocean.rxjava.RxJavaFilterOperation;
import com.joker.pacific.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 17-10-23.
 */

public class Rxjava2FilterOperation {
    private Logger mLogger = Logger.getLogger(RxJavaFilterOperation.class);
    private List<String> datas = new ArrayList<String>();

    public Rxjava2FilterOperation() {
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


    public void testFilter() {
        Observable.fromIterable(datas).filter((data) -> data.startsWith("t"))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testFilter -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testFilter -- onComplete");
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
        Observable.fromIterable(datas).take(4)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testTake -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testTake -- onComplete");
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
        Observable.fromIterable(datas).takeLast(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testTakeLast -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testTakeLast -- onComplete");
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
        Observable.fromIterable(datas).distinct()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testDistinct -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testDistinct -- onComplete");
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
        Observable.fromIterable(datas).distinctUntilChanged()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testDistinctUntilsChanged -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testDistinctUntilsChanged -- onComplete");
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
        Observable.fromIterable(datas).skip(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testSkip -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testSkip -- onComplete");
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
        Observable.fromIterable(datas).skipLast(3)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testSkip -- onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testSkip -- onComplete");
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
        Observable.fromIterable(datas).elementAt(3)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mLogger.info("testElementAt accept: " + s);
                    }
                });
    }

    /**
     * 我们可以使用一个小的发射间隔。在Observable后面加
     * 一个 sample() ,我们将创建一个新的可观测序列,它将在一个指定的时间间隔里
     * 由Observable发射最近一次的数值:
     */
    public void testSmpling() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .sample(1000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testSmpling -- onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        mLogger.info("testSmpling : " + aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("testSmpling -- onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testSmpling -- onComplete");
                    }
                });
    }

    public void testTimeOut() {
        Observable.interval(2, TimeUnit.SECONDS)
                .timeout(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testTimeOut -- onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        mLogger.info("testTimeOut -- onNext");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("testTimeOut -- onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testTimeOut -- onComplete");
                    }
                });

    }

    private int i = 0;
    private Random mRandom = new Random();
    public void testDebounce() {
        i = 0;
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                while (true){
                    mLogger.info("testDebounce fire: " + i);
                    e.onNext(i);
                    i++;
                    try{
                        Thread.currentThread().sleep(50 * mRandom.nextInt(4));
                    }catch (InterruptedException exception){

                    }
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLogger.info("testDebounce -- onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        mLogger.info("testDebounce -- onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mLogger.info("testDebounce -- onError");
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });

    }

}
