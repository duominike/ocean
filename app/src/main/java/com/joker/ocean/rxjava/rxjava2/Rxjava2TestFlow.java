package com.joker.ocean.rxjava.rxjava2;

import com.joker.pacific.log.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 17-10-23.
 */

public class Rxjava2TestFlow {
    private Logger mLogger = Logger.getLogger(Rxjava2TestFlow.class);

    public void showBackPress() {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mLogger.info("showBackPress -- accept");
                        Thread.sleep(1000);
                    }
                });

    }


    //在此策略下，如果放入Flowable的异步缓存池中的数据超限了，则会抛出MissingBackpressureException异常。
    public void testError() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                for (int i = 1; i <= 130; i++) {
                    mLogger.info("onError: " + e.requested());
                    e.onNext(i);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mLogger.info("onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("onNext");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });
    }


    /**
     * 在此策略下，如果Flowable的异步缓存池满了，会丢掉将要放入缓存池中的数据。
     */
    public void testDrop() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                mLogger.info("testDrop begin fire data");
                for (int i = 1; i <= 500; i++) {
                    mLogger.info("testDrop: requested -->> " + e.requested());
                    mLogger.info("testDrop fire: " + i);
                    e.onNext(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException E) {

                    }
                }
                mLogger.info("testDrop end fire data");
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mLogger.info("testDrop onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mLogger.info("testDrop onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testDrop onComplete");
                    }
                });
    }

    /**
     * 与Drop策略一样，如果缓存池满了，会丢掉将要放入缓存池中的数据，
     * 不同的是，不管缓存池的状态如何，LATEST都会将最后一条数据强行放入缓存池中
     */
    public void testLatest() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                mLogger.info("testFlowableDrop begin fire data");
                for (int i = 0; i <= 500; i++) {
                    mLogger.info("testFlowableDrop fire: " + i);
                    e.onNext(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException E) {

                    }
                }
                mLogger.info("testFlowableDrop end fire data");
                e.onComplete();
            }
        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mLogger.info("testFlowableDrop onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mLogger.info("testFlowableDrop onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });
    }


    /**
     * 此策略下，Flowable的异步缓存池同Observable的一样，没有固定大小，可以无限制向里添加数据，不会抛出MissingBackpressureException异常，但会导致OOM。
     */
    public void testFlowableBuffer() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                int i = 0;
                while (true) {
                    e.onNext(i++);
                }
//                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mLogger.info("testFlowableDrop onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mLogger.info("testFlowableDrop onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });
    }


    /**
     * 我们发现Flowable照常发送数据，而Subsriber不再接收数据。
     * 这是因为Flowable在设计的时候，采用了一种新的思路——响应式拉取方式，来设置下游对数据的请求数量，上游可以根据下游的需求量，按需发送数据。
     * 如果不显示调用request则默认下游的需求量为零，所以运行上面的代码后，上游Flowable发射的数据不会交给下游Subscriber处理。
     */
    public void testFlowableNoneRequest() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
//                mLogger.info("testFlowableNoneRequest subscribe 1");
//                e.onNext(1);
//                mLogger.info("testFlowableNoneRequest subscribe 2");
//                e.onNext(2);
//                mLogger.info("testFlowableNoneRequest subscribe 3");
//                e.onNext(3);
//                mLogger.info("testFlowableNoneRequest onFinished");
//                e.onComplete();
                for(int i=1; i<= 10; i++){
                    mLogger.info("testFlowableNoneRequest fire:  " + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
//                        s.request(Long.MAX_VALUE);
//                        s.request(2);
                        s.request(3);
                        s.request(4);
                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("testFlowableDrop onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });
    }

    public void testFlowableData() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                for (int i = 1; i < 130; i++) {
                    mLogger.info("fire data: " + i);
                    e.onNext(i);
                }
//                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("testFlowableDrop onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });
    }


    /**
     * 其实不论下游通过s.request();设置多少请求量，我们在上游获取到的初始未完成请求数量都是128。
     * 这是为啥呢？
     * 还记得之前我们说过，Flowable有一个异步缓存池，上游发射的数据，先放到异步缓存池中，再由异步缓存池交给下游。
     * 所以上游在发射数据时，首先需要考虑的不是下游的数据请求量，而是缓存池中能不能放得下，否则在缓存池满的情况下依然会导致数据遗失或者背压异常。
     * 如果缓存池可以放得下，那就发送，至于是否超出了下游的数据需求量，可以在缓存池向下游传递数据时，再作判断，
     * 如果未超出，则将缓存池中的数据传递给下游，如果超出了，则不传递。
     * 如果下游对数据的需求量超过缓存池的大小，而上游能获取到的最大需求量是128，上游对超出128的需求量是怎么获取到的呢？
     */

    public void testFlowableReqOut(){
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                for (int i = 1; i <= 5; i++) {
                    mLogger.info("testFlowableReqOut unFinished Count: " + e.requested());
                    mLogger.info("testFlowableReqOut fire Data: " + i);
                    e.onNext(i);
                }
                mLogger.info("testFlowableReqOut fire data end");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(150);
                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("testFlowableReqOut onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("testFlowableReqOut onComplete");
                    }
                });
    }

    /**
     * 如果下游对数据的需求量超过缓存池的大小，而上游能获取到的最大需求量是128，上游对超出128的需求量是怎么获取到的呢？
     */
    public void testFlowableRequested() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                for (int i = 1; i <= 150; i++) {
                    mLogger.info("testFlowableRequested unFinished Count: " + e.requested());
                    mLogger.info("testFlowableRequested fire Data: " + i);
                    e.onNext(i);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(150);
                    }

                    @Override
                    public void onNext(Object o) {
                        mLogger.info("testFlowableDrop onNext: " + o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLogger.info("onComplete");
                    }
                });
    }


    private Subscription subscription;

    public void testFlowWithOutMiss() {
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                int i = 0;
                while (true) {
                    if (e.requested() == 0) {
                        continue;
                    }
                    mLogger.info("testFlowWithOutMiss send: " + (++i));
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void testTranspoline(){
        Scheduler.Worker worker = Schedulers.trampoline().createWorker();
        Runnable leafAction = () -> System.out.println("----leafAction.");
        Runnable innerAction = () ->
        {
            System.out.println("--innerAction start.");
            worker.schedule(leafAction);
            System.out.println("--innerAction end.");
        };


        Runnable outerAction = () ->
        {
            System.out.println("outer start.");
            worker.schedule(innerAction);
            System.out.println("outer end.");
        };

        worker.schedule(outerAction);
    }


}
