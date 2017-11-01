package com.joker.ocean;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joker.ocean.base.BaseActivity;
import com.joker.ocean.net.ProtoMaster;
import com.joker.ocean.rxjava.RxJavaUtils;
import com.joker.ocean.rxjava.rxjava2.Rxjava2Combination;
import com.joker.ocean.rxjava.rxjava2.Rxjava2FilterOperation;
import com.joker.ocean.rxjava.rxjava2.Rxjava2Test;
import com.joker.ocean.rxjava.rxjava2.Rxjava2TestFlow;
import com.joker.ocean.rxjava.rxjava2.RxjavaMapOperation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private TextView m_tvTestView;
    private static final String TAG = "MainActivity";
    private Rxjava2Test mRxjava2Test;
    private Rxjava2FilterOperation mFilterOperation;
    private RxjavaMapOperation mRxjavaMapOperation;
    private Rxjava2Combination mRxjava2Combination;
    private Rxjava2TestFlow mRxjava2TestFlow;
    private RxJavaUtils mRxJavaUtils = new RxJavaUtils();
    private ProtoMaster mProtoMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_tvTestView = (TextView)findViewById(R.id.tv_customview);
        m_tvTestView.setOnClickListener(this);
        mRxjava2Test = new Rxjava2Test();
        mRxjava2Test.init();
        mFilterOperation = new Rxjava2FilterOperation();
        mRxjava2TestFlow = new Rxjava2TestFlow();
        mRxjava2Combination = new Rxjava2Combination();
        mRxjavaMapOperation = new RxjavaMapOperation();
        mProtoMaster = new ProtoMaster(this);
    }

    @Override
    public void onClick(View v) {
        mProtoMaster.getConf();
    }

    private void connect(){
        //        startActivity(new Intent(this, TestVerticalSeekbarActiivty.class));
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
        //使用publish操作符将普通Observable转换为可连接的Observable
        ConnectableObservable<Long> connectableObservable = observable.publish();
        //开始发射数据,如果不调用connect方法，connectableObservable则不会发射数据
//        Disposable subscription = connectableObservable.connect();
        connectableObservable.observeOn(Schedulers.newThread())
                .subscribe(new Subject<Long>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super Long> observer) {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        mLogger.info("onNext: " + Thread.currentThread().getName());
                        mLogger.info("onNext1: " + aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        connectableObservable.observeOn(Schedulers.newThread())
                .subscribe(new Subject<Long>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super Long> observer) {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        mLogger.info("onNext: " + Thread.currentThread().getName());
                        mLogger.info("onNext2: " + aLong);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        connectableObservable.connect();
    }

    private void testFilter(){
        mFilterOperation.testFilter();
        mFilterOperation.testSmpling();
        mFilterOperation.testElementAt();
        mFilterOperation.testDistinct();
        mFilterOperation.testDistinctUntilsChanged();
        mFilterOperation.testTimeOut();
        mFilterOperation.testSkip();
        mFilterOperation.testSkipLast();
    }

    private void testMap(){
        mRxjavaMapOperation.testmap();
        mRxjavaMapOperation.testFlatMap();
        mRxjavaMapOperation.testConcatMap();
        mRxjavaMapOperation.testFlatMapIterable();
        mRxjavaMapOperation.testBuffer();
        mRxjavaMapOperation.testWindow();
    }

    public void testCombination(){
        mRxjava2Combination.testZip();
        mRxjava2Combination.testMerge();
        mRxjava2Combination.join();
    }

    private void testFlowable(){
        mRxjava2TestFlow.testError();
        mRxjava2TestFlow.testDrop();
        mRxjava2TestFlow.testLatest();
        mRxjava2TestFlow.testFlowableBuffer();
        mRxjava2TestFlow.testFlowWithOutMiss();

    }

    private void testRxjavaUtils(){
        mRxJavaUtils.testReqOrder();
        mRxJavaUtils.testSeriReqDepend();
        mRxJavaUtils.testConditionReq();
        mRxJavaUtils.testMergeReq();
    }

    private void testObservable(){
        mRxjava2Test.testObservableFrom();
        mRxjava2Test.testObservableRange();
        mRxjava2Test.testObservableInterval();
        mRxjava2Test.testObservableTimer();
    }


}
