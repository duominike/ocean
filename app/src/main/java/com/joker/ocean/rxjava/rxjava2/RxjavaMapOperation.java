package com.joker.ocean.rxjava.rxjava2;

import com.joker.ocean.rxjava.RxJavaMapOperation;
import com.joker.pacific.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
 * Created by joker on 17-10-23.
 */

public class RxjavaMapOperation {
    private Logger mLogger = Logger.getLogger(RxJavaMapOperation.class);

    /**
     * RxJava的 map 函数接收一个指定的 Func 对象然后将它应用到每一个由Observable发射的值上。
     */
    public void testmap() {
        Observable.range(0, 10)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer o) throws Exception {
                        return o * o;
                    }

                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mLogger.info("testmap -- onSubscribe");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                mLogger.info("testmap -- onNext" + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mLogger.info("testmap -- onError");
            }

            @Override
            public void onComplete() {
                mLogger.info("testmap -- onComplete");
            }
        });
    }

    /**
     * 在复杂的场景中,我们有一个这样的Observable:它发射一个数据序列,这些数据
     * 本身也可以发射Observable。RxJava的 flatMap() 函数提供一种铺平序列的方
     * 式,然后合并这些Observables发射的数据,最后将合并后的结果作为最终的
     * Observable。
     */
    private String mStr = "abcdefghijklmnopqrstuvwxyz";

    public void testFlatMap() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i <= 25; i++) {
                    mLogger.info("testFlatMap　emit int: -->> " + i + "-->> " + Thread.currentThread().getName());
                    e.onNext(i);
                    Thread.sleep(100);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        observable1.flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        mLogger.info("testFlatMap convert: -->> " + integer + "-->> " +
                                Thread.currentThread().getName());
                        for (int i = 0; i <= integer; i++) {
                            mLogger.info("testFlatMap　emit string: -->> " + String.valueOf(mStr.charAt(integer)) + "-->> " +
                                    Thread.currentThread().getName());
                            e.onNext(String.valueOf(mStr.charAt(integer)));
                            Thread.sleep(50);
                        }
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread());
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mLogger.info("testFlatMap accept: -->> " + s);
                    }
                });
    }

    /**
     * oncatMap()解决了flatMap()的交叉问题，它能够把发射的值连续在一起
     */
    public void testConcatMap() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i <= 25; i++) {
                    mLogger.info("testFlatMap　emit int: -->> " + i + "-->> " + Thread.currentThread().getName());
                    e.onNext(i);
                    Thread.sleep(100);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        observable1.concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        mLogger.info("testConcatMap convert: -->> " + integer + "-->> " +
                                Thread.currentThread().getName());
                        for (int i = 0; i <= integer; i++) {
                            mLogger.info("testConcatMap　emit string: -->> " + String.valueOf(mStr.charAt(integer)) + "-->> " +
                                    Thread.currentThread().getName());
                            e.onNext(String.valueOf(mStr.charAt(integer)));
                            Thread.sleep(50);
                        }
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io());
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mLogger.info("testConcatMap accept: -->> " + s);
                    }
                });
    }

    /**
     * flatMapIterable latMapInterable() 和 flatMap() 很像。仅有的本质
     * 不同是它将源数据两两结成对并生成Iterable
     */
    public void testFlatMapIterable() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        observable1.flatMapIterable(new Function<Integer, Iterable<?>>() {
            @Override
            public Iterable<String> apply(Integer integer) {
                List<String> lst = new ArrayList<String>();
                for (int i = 0; i < integer; i++) {
                    lst.add(String.valueOf(i) + String.valueOf(integer));
                }
                return lst;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                mLogger.info("testFlatMapIterable -- " + o.toString());
            }
        });
    }

    /**
     * switchMap() 和 flatMap() 很像,除了一点:每当源Observable
     * 发射一个新的数据项(Observable)时,它将取消订阅并停止监视之前那个数据项
     * 产生的Observable,并开始监视当前发射的这一个。
     */
    public void testSwitchMap() {
        Observable.
                interval(200, TimeUnit.MILLISECONDS).
                observeOn(Schedulers.computation())
                .switchMap(new Function<Long, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Long aLong) {
                        return Observable.just(String.valueOf(aLong)).subscribeOn(Schedulers.newThread());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
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
                    public void onNext(String s) {
                        mLogger.info("testSwitchMap onNext: " + s);
                    }
                });
    }

    /**
     * RxJava的 scan() 函数可以看做是一个累积函数。 scan() 函数对原始
     * Observable发射的每一项数据都应用一个函数,计算出函数的结果值,并将该值填
     * 充回可观测序列,等待和下一次发射的数据一起使用
     */
    public void testScan() {
        Observable.just(1, 2, 3, 4, 5).scan((sum, x) -> sum + x)
                .subscribe(new Observer<Integer>() {
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
                    public void onNext(Integer integer) {
                        mLogger.info("testScan onNext: " + integer);
                    }
                });
    }


    public void testGroupBy() {
        List<String> datas = new ArrayList<String>();
        datas.add("one");
        datas.add("two");
        datas.add("three");
        datas.add("four");
        datas.add("three");
        datas.add("five");
        datas.add("six");
        datas.add("seven");
        datas.add("three");
        datas.add("five");
        datas.add("seven");
        Observable.fromIterable(datas).
                groupBy(new Function<String, Character>() {
                    @Override
                    public Character apply(String s) {
                        return s.charAt(0);
                    }
                }).subscribe(group -> {
            group.subscribe(str ->{
                mLogger.info("testGroupBy key: -->> " + group.getKey() + "value: " + str);
            });
        });

    }


    /**
     * RxJava中的 buffer() 函数将源Observable变换一个新的Observable,
     * 这个新的Observable每次发射一组列表值而不是一个一个发射。
     */
    private int i = 0;

    public void testBuffer() {
//        List<Integer> lst = new ArrayList<Integer>();
//        int i=0 ;
        Observable.interval(100, TimeUnit.MILLISECONDS).map(new Function<Long, Integer>() {
            @Override
            public Integer apply(Long aLong) {
                return i++;
            }
        }).buffer(350, TimeUnit.MILLISECONDS, 3)
                .subscribe(new Observer<List<Integer>>() {
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
                    public void onNext(List<Integer> lists) {
                        StringBuilder sb = new StringBuilder();
                        for (Integer i : lists) {
                            sb.append(i + "");
                            sb.append(",");
                        }
                        mLogger.info("testBuffer onNext: " + sb.toString());
                    }
                });
    }

    /**
     * RxJava的 window() 函数和 buffer() 很像,但是它发射的是Observable而不是列表。
     */
    public void testWindow() {
        i = 0;
        Observable.interval(50, TimeUnit.MILLISECONDS).
                map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) {
                        return i++;
                    }
                }).window(200, TimeUnit.MILLISECONDS, 3)
                .subscribe(new Observer<Observable<Integer>>() {
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
                    public void onNext(Observable<Integer> integerObservable) {
                        mLogger.info("testWindow onNext: " + integerObservable);
                        integerObservable.subscribe(new Observer<Integer>() {
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
                            public void onNext(Integer integer) {
                                mLogger.info("testWindow -->> onNext -->> integerObservable: ---->> " + integer);
                            }
                        });
                    }
                });
    }

    public void testCast() {
        Observable.range(0, 15).cast(String.class)
                .subscribe(new Observer<String>() {
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
                    public void onNext(String s) {
                        mLogger.info("testCast onNext: " + s);
                    }
                });
    }
}
