package com.joker.ocean.rxjava;

import com.joker.pacific.log.Logger;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by joker on 17-9-27.
 */

public class RxJavaCombination {
    private Logger mLogger = Logger.getLogger(RxJavaCombination.class);
    /**
     * RxJava的 merge() 方法将帮助你把两个甚至更多的Observables合并到他们发射的数据项里。
     * 注意如果你同步的合并Observable,它们将连接在一起并且不会交叉。
     */
    public void testMerge() {
        Observable observable1 = Observable.just(1, 2, 3, 4, 5);
        Observable observable2 = Observable.just(6, 7, 8, 9, 10);
        Observable.merge(observable1, observable2)
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

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

    private String zipIntAndStr(Integer int1, String str1){
        return int1+ str1;
    }

    private String zipStrAndInt(String str1, Integer int1){
        return int1+ str1;
    }

    /**
     * 在一种新的可能场景中处理多个数据来源时会带来:多从个Observables接收数
     据,处理它们,然后将它们合并成一个新的可观测序列来使用。RxJava有一个特殊
     的方法可以完成: zip() 合并两个或者多个Observables发射出的数据项,根据指
     定的函数 Func* 变换它们,并发射一个新值
     */
    public void testZip() {
        Observable observable1 = Observable.just(1, 2, 3, 4, 5);
        Observable observable2 = Observable.just("a", "b", "c", "d", "e");
        Observable.zip(observable1, observable2, new Func2() {
            @Override
            public Object call(Object o, Object o2) {
                return (Integer)o + (String)o2;
            }
        })
                    .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

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

    public void testJoin() {
       Observable<Integer> observable1 = Observable.interval(200, TimeUnit.MILLISECONDS)
               .map(new Func1<Long, Integer>() {
                   @Override
                   public Integer call(Long aLong) {
                       return Integer.valueOf((int)(aLong % 100));
                   }
               });
        Observable<String> observable2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return String.valueOf(aLong %100);
                    }
                });

        Observable<String> observable = observable1.join(observable2,
                integer -> Observable.timer(300, TimeUnit.MILLISECONDS),
                str -> Observable.timer(150, TimeUnit.MILLISECONDS),
                new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return integer+ s;
            }
        });
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mLogger.info("testJoin onNext: " +s);
            }
        });
    }

    public void testConbineLatest() {
        Observable<Integer> observable1 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return Integer.valueOf((int)(aLong % 100));
                    }
                });
        Observable<String> observable2 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return String.valueOf(aLong %100);
                    }
                });
        Observable.combineLatest(observable1, observable2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return integer+ s;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String o) {
                mLogger.info("testCombineLatest onNext: " + o);
            }
        });
    }

}
