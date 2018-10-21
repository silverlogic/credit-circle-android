package com.tsl.creditcircle.utils.pushnotifications;

import rx.Subscriber;

/**
 * Created by kevinlavi on 11/17/17.
 */

public abstract class PromiseSubscriber<T> extends Subscriber<T> {

    protected void preAlways() {
    }

    protected void postAlways() {
    }

    protected abstract void then(T value);
    protected abstract void error(Throwable e);

    @Override
    public final void onCompleted() {

    }

    @Override
    public final void onError(Throwable e) {
        preAlways();
        error(e);
        postAlways();
    }

    @Override
    public final void onNext(T t) {
        preAlways();
        then(t);
        postAlways();
    }
}