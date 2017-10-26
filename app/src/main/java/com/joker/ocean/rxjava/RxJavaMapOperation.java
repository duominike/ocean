package com.joker.ocean.rxjava;


/**
 * Created by joker on 17-9-27.
 */

public class RxJavaMapOperation {
//    private Logger mLogger = Logger.getLogger(RxJavaMapOperation.class);
//
//    /**
//     * RxJava的 map 函数接收一个指定的 Func 对象然后将它应用到每一个由Observable发射的值上。
//     */
//    public void testmap(){
//        Observable.range(0, 10)
//                .map(new Func1<Integer, Integer>() {
//                    @Override
//                    public Integer call(Integer integer) {
//                        return integer * integer;
//                    }
//                }).subscribe(new Observer<Integer>() {
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
//            public void onNext(Integer integer) {
//                mLogger.info("testmap onNext: " + integer);
//            }
//        });
//    }
//
//    /**
//     * 在复杂的场景中,我们有一个这样的Observable:它发射一个数据序列,这些数据
//     本身也可以发射Observable。RxJava的 flatMap() 函数提供一种铺平序列的方
//     式,然后合并这些Observables发射的数据,最后将合并后的结果作为最终的
//     Observable。
//     */
//    public void testFlatMap(){
//        Observable.range(1, 10)
//                .flatMap(new Func1<Integer, Observable<List<String>>>() {
//                    @Override
//                    public Observable<List<String>> call(Integer integer) {
//                        List<String> lst = new ArrayList<String>();
//                        for(int i = 0; i < integer; i++){
//                            lst.add(String.valueOf(i) + String.valueOf(integer));
//                        }
//                        return Observable.just(lst);
//                    }
//                }).
//            subscribe(new Subscriber<List<String>>() {
//            @Override
//            public void onCompleted() {
//                mLogger.info("testFlatMap: onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mLogger.info("testFlatMap: onError");
//            }
//
//            @Override
//            public void onNext(List<String> s) {
//                StringBuilder stringBuilder = new StringBuilder();
//                for(String ss: s){
//                    stringBuilder.append(ss);
//                    stringBuilder.append(",");
//                }
//                mLogger.info("testFlatMap onNext: " + stringBuilder.toString());
//
//            }
//        });
//    }
//
//    /**
//     * oncatMap()解决了flatMap()的交叉问题，它能够把发射的值连续在一起
//     */
//    public void testConcatMap(){
//        Observable.range(0, 10)
//                .concatMap(new Func1<Integer, Observable<List<String>>>() {
//                    @Override
//                    public Observable<List<String>> call(Integer integer) {
//                        List<String> lst = new ArrayList<String>();
//                        for(int i = 0; i< integer; i++){
//                            lst.add(String.valueOf(i) + String.valueOf(integer));
//                        }
//                        return Observable.just(lst);
//                    }
//                }).subscribe(new Subscriber<List<String>>() {
//            @Override
//            public void onCompleted() {
//                mLogger.info("testFlatMap: onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mLogger.info("testFlatMap: onError");
//            }
//
//            @Override
//            public void onNext(List<String> s) {
//                mLogger.info("testFlatMap onNext: " + s.toString());
//
//            }
//        });
//    }
//
//    /**
//     * flatMapIterable latMapInterable() 和 flatMap() 很像。仅有的本质
//     不同是它将源数据两两结成对并生成Iterable
//     */
//    public void testFlatMapIterable(){
//        Observable<Integer> observable1 = Observable.just(1, 2, 3);
//        observable1.flatMapIterable(new Func1<Integer, Iterable<String>>() {
//            @Override
//            public Iterable<String> call(Integer integer) {
//                List<String> lst = new ArrayList<String>();
//                for(int i = 0; i< integer; i++){
//                    lst.add(String.valueOf(i) + String.valueOf(integer));
//                }
//                return lst;
//            }
//        }).subscribe(new Subscriber<String>() {
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
//                mLogger.info("testFlatMapIterable onNext: " + s);
//
//            }
//        });
//    }
//
//    /**
//     * switchMap() 和 flatMap() 很像,除了一点:每当源Observable
//     发射一个新的数据项(Observable)时,它将取消订阅并停止监视之前那个数据项
//     产生的Observable,并开始监视当前发射的这一个。
//     */
//    public void testSwitchMap(){
//        Observable.
//                interval(200, TimeUnit.MILLISECONDS).
//                observeOn(Schedulers.computation())
//                .switchMap(new Func1<Long, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(Long aLong) {
//                        return Observable.just(String.valueOf(aLong)).subscribeOn(Schedulers.newThread());
//                    }
//                }).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        mLogger.info("testSwitchMap onNext: " + s);
//                    }
//                });
//    }
//
//    /**
//     * RxJava的 scan() 函数可以看做是一个累积函数。 scan() 函数对原始
//     Observable发射的每一项数据都应用一个函数,计算出函数的结果值,并将该值填
//     充回可观测序列,等待和下一次发射的数据一起使用
//     */
//    public void testScan(){
//        Observable.just(1, 2, 3, 4, 5).scan((sum, x) -> sum + x)
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        mLogger.info("testScan onNext: " + integer);
//                    }
//                });
//    }
//
//
//    public void testGroupBy(){
//        List<String> datas = new ArrayList<String>();
//        datas.add("one");
//        datas.add("two");
//        datas.add("three");
//        datas.add("four");
//        datas.add("three");
//        datas.add("five");
//        datas.add("six");
//        datas.add("seven");
//        datas.add("three");
//        datas.add("five");
//        datas.add("seven");
//        Observable observable = Observable.from(datas).
//                groupBy(new Func1<String, Character>() {
//                    @Override
//                    public Character call(String s) {
//                        return s.charAt(0);
//                    }
//                });
//        Observable.concat(observable).subscribe(new Observer<String>() {
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
//            public void onNext(String o) {
//                mLogger.info("testGroupBy onNext: " + o);
//            }
//        });
//    }
//
//
//    /**
//     * RxJava中的 buffer() 函数将源Observable变换一个新的Observable,
//     * 这个新的Observable每次发射一组列表值而不是一个一个发射。
//     */
//    private int i=0;
//    public void testBuffer(){
////        List<Integer> lst = new ArrayList<Integer>();
////        int i=0 ;
//        Observable.interval(100, TimeUnit.MILLISECONDS).map(new Func1<Long, Integer >() {
//            @Override
//            public Integer call(Long aLong) {
//                return i++;
//            }
//        }).buffer(350, TimeUnit.MILLISECONDS, 3)
//                .subscribe(new Subscriber<List<Integer>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Integer> lists) {
//                        StringBuilder sb = new StringBuilder();
//                        for(Integer i: lists){
//                            sb.append(i+"");
//                            sb.append(",");
//                        }
//                        mLogger.info("testBuffer onNext: " + sb.toString());
//                    }
//                });
//    }
//
//    /**
//     * RxJava的 window() 函数和 buffer() 很像,但是它发射的是Observable而不是列表。
//     *
//     */
//    public void testWindow(){
//        i = 0;
//        Observable.interval(50, TimeUnit.MILLISECONDS).
//                map(new Func1<Long, Integer>() {
//                    @Override
//                    public Integer call(Long aLong) {
//                        return i++;
//                    }
//                }).window(200, TimeUnit.MILLISECONDS, 3)
//                .subscribe(new Subscriber<Observable<Integer>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Observable<Integer> integerObservable) {
//                        mLogger.info("testWindow onNext: " + integerObservable);
//                        integerObservable.subscribe(new Subscriber<Integer>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(Integer integer) {
//                                mLogger.info("testWindow -->> onNext -->> integerObservable: ---->> " + integer);
//                            }
//                        });
//                    }
//                });
//    }
//
//    public void testCast(){
//        Observable.range(0, 15).cast(String.class)
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        mLogger.info("testCast onNext: " + s);
//                    }
//                });
//    }


}
