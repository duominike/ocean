package com.joker.pacific.imageload.producer;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by joker on 18-1-9.
 */

public class NetWorkProducer implements Producer<InputStream>{
    private Executor mExecutor;
    private Producer mProducer;
    public NetWorkProducer(Executor executor){
        this.mExecutor = executor;
    }
    @Override
    public void setProducer(Producer producer) {
        this.mProducer = producer;
    }

    @Override
    public Observable<InputStream> produce(final ImageRequest imgRequest) {
        return Observable.create(new ObservableOnSubscribe<InputStream>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<InputStream> e) throws Exception {
                URL url = new URL(imgRequest.url);
                URLConnection connection = url.openConnection();
                e.onNext(connection.getInputStream());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.from(mExecutor));
    }
}
