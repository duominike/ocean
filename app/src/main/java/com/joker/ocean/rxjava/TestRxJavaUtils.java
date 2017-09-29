package com.joker.ocean.rxjava;


import com.joker.pacific.log.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

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

    public void test1(){
        Observable<Integer> stringObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i =0; i< 5; i++){
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
        Subscription subscription = stringObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                mLogger.info("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                mLogger.error(e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                mLogger.info("onNext: " + integer);
            }
        });
    }

    /**
     * from() 创建符可以从一个列表/数组来创建Observable,并一个接一个的从列表/数
     组中发射出来每一个对象,或者也可以从Java Future 类来创建Observable,并
     发射Future对象的 .get() 方法返回的结果值。传入 Future 作为参数时,我们
     可以指定一个超时的值。Observable将等待来自 Future 的结果;如果在超时之前
     仍然没有结果返回,Observable将会触发 onError() 方法通知观察者有错误发生
     了。
     */
    public void testFrom(){
        List<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(2);
        items.add(3);
        Observable<Integer> stringObservable = Observable.from(items);
        Subscription subscription = stringObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                mLogger.info("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                mLogger.error(e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                mLogger.info("onNext: " + integer);
            }
        });
    }

    private String helloWorld(){
        return "hello world";
    }

    /**
     * just() 方法可以传入一到九个参数,它们会按照传入的参数的顺序来发射它
     们。 just() 方法也可以接受列表或数组,就像 from() 方法,但是它不会迭代
     列表发射每个值,它将会发射整个列表。通常,当我们想发射一组已经定义好的值时
     会用到它。但是如果我们的函数不是时变性的,我们可以用just来创建一个更有组
     织性和可测性的代码库。
     */
    public void testJust(){
        Observable<String> stringObservable = Observable.just(helloWorld());

        Subscription subscription = stringObservable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                mLogger.info("onComplete");
            }

            @Override
            public void onError(Throwable e) {
                mLogger.error(e.getMessage());
            }

            @Override
            public void onNext(String integer) {
                mLogger.info("onNext: " + integer);
            }
        });
    }

    public void testPublishSubject(){
        final PublishSubject<Boolean> subject = PublishSubject.create();
        // subject　作为一个observable
        subject.subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                 mLogger.info("observable complete: " + aBoolean);
            }
        });

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext(i);
                }
                mLogger.info("subscriber onCompleted");
                subscriber.onCompleted();
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                subject.onNext(true);
            }
        }).subscribe();// 仅仅为了开启Observable
    }

}
