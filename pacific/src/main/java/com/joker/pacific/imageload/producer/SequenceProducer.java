package com.joker.pacific.imageload.producer;

import android.graphics.Bitmap;

import com.joker.pacific.imageload.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by joker on 18-1-10.
 */

public class SequenceProducer implements Producer<Bitmap>{
    private Producer<Bitmap> inputProducer;
    private Scheduler rxSubscribeScheduler;
    private Scheduler rxObservableScheduler;
    public SequenceProducer(Producer<Bitmap> inputProducer, Scheduler subscribeScheduler,
                            Scheduler observableScheduler){
        this.inputProducer = inputProducer;
        this.rxSubscribeScheduler = subscribeScheduler;
        this.rxObservableScheduler = observableScheduler;

    }
    @Override
    public Observable<Bitmap> produce(ImageLoader.ImageRequest imgRequest) {
        return inputProducer.produce(imgRequest).subscribeOn(rxSubscribeScheduler)
                .observeOn(rxObservableScheduler);
    }
}
