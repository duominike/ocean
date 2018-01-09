package com.joker.pacific.imageload.producer;

import io.reactivex.Observable;

/**
 * Created by joker on 18-1-9.
 */

public interface Producer<T> {
    void setProducer(Producer producer);

    <T> Observable<T> produce(ImageRequest imgRequest);
}
