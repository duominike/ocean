package com.joker.ocean.rxjava.rxjava2;

import com.joker.ocean.rxjava.RxJavaCombination;
import com.joker.pacific.log.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 17-10-23.
 */

public class Rxjava2Combination {
    private Logger mLogger = Logger.getLogger(RxJavaCombination.class);

    /**
     * RxJava的 merge() 方法将帮助你把两个甚至更多的Observables合并到他们发射的数据项里。
     * 注意如果你同步的合并Observable,它们将连接在一起并且不会交叉。
     */
    public void testMerge() {
        Observable observable1 = Observable.just(1, 2, 3, 4, 5);
        Observable observable2 = Observable.just(6, 7, 8, 9, 10);
        Observable.merge(observable1, observable2)
                .subscribe(new Observer() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("testMerge onNext: " + o);
                    }
                });
    }

    private String zipIntAndStr(Integer int1, String str1) {
        return int1 + str1;
    }

    private String zipStrAndInt(String str1, Integer int1) {
        return int1 + str1;
    }

    /**
     * 在一种新的可能场景中处理多个数据来源时会带来:多从个Observables接收数
     * 据,处理它们,然后将它们合并成一个新的可观测序列来使用。RxJava有一个特殊
     * 的方法可以完成: zip() 合并两个或者多个Observables发射出的数据项,根据指
     * 定的函数 Func* 变换它们,并发射一个新值
     */
    public void testZip() {
        Observable observable1 = Observable.just(1, 2, 3, 4, 5);
        Observable observable2 = Observable.just("a", "b", "c", "d", "e");
        Observable.zip(observable1, observable2, new BiFunction() {
            @Override
            public Object apply(Object o, Object o2) {
                return (Integer) o + (String) o2;
            }
        })
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("testZip onNext: " + o);
                    }
                });
    }

//    private int i = 1;
//    private int j = 0;
//    private String content = "abcdefghijklmnopqrstuvwxyz";
//
//    public void testJoin() {
//        mLogger.info("testJoin");
//        Observable<Integer> observable1 = Observable.interval(3000, TimeUnit.MILLISECONDS).
//                map(new Function<Long, Integer>() {
//            @Override
//            public Integer apply(Long aLong) {
//                mLogger.info("testJoin call integer");
//                return Integer.valueOf(i++);
//            }
//        });
//
//        Observable<String> observable2 = Observable.interval(1500, TimeUnit.MILLISECONDS).
//                map(new Function<Long, String>() {
//            @Override
//            public String apply(Long aLong) {
//                mLogger.info("testJoin call string");
//                return content.substring(j, ++j);
//            }
//        });
//
//        observable1.join(observable2, integer -> Observable.timer(2, TimeUnit.SECONDS),
//                str -> Observable.timer(1, TimeUnit.SECONDS),
//                new Func<Integer, String, String>() {
//                    @Override
//                    public String call(Integer integer, String s) {
//                        return integer + s;
//                    }
//                }).subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                mLogger.info("testJoin onNext: " + s);
//            }
//        });
//    }


    private Observable<String> createJoinObserver() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                for (int i = 1; i < 5; i++) {
                    e.onNext("Right-" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }).subscribeOn(Schedulers.newThread());
    }

    public void join() {
        mLogger.info("join");
        Observable.just("Left-").join(createJoinObserver(),
                integer -> Observable.timer(3000, TimeUnit.MILLISECONDS),
                integer -> Observable.timer(2000, TimeUnit.MILLISECONDS),
                (i, j) -> i + j
        ).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                mLogger.info("join:" + s + "\n");
            }
        });
    }
}
