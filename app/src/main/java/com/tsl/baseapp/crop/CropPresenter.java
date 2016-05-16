package com.tsl.baseapp.crop;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.model.event.CropEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class CropPresenter extends MvpBasePresenter<CropView> {

    private CompositeSubscription cropSubsciption;
    private EventBus eventBus;

    @Inject
    public CropPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void crop(com.lyft.android.scissors.CropView mCropView, Context mContext) {

        final File croppedFile = new File(mContext.getCacheDir(), "cropped.jpg");

        Observable<Void> onSave = Observable.from(mCropView.extensions()
                .crop()
                .quality(100)
                .format(JPEG)
                .into(croppedFile))
                .subscribeOn(io())
                .observeOn(mainThread());


        cropSubsciption.add(onSave
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void nothing) {
                        eventBus.post(new CropEvent(croppedFile));
                    }
                }));
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (cropSubsciption != null && !cropSubsciption.isUnsubscribed()) {
            cropSubsciption.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(CropView view) {
        super.attachView(view);
    }
}
