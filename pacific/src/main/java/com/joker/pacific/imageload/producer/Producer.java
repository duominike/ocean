package com.joker.pacific.imageload.producer;

import com.joker.pacific.imageload.ImageLoader;

import io.reactivex.Observable;

/**
 * Created by joker on 18-1-9.
 */

interface Producer<T> {
    Observable<T> produce(ImageLoader.ImageRequest imgRequest);
}
