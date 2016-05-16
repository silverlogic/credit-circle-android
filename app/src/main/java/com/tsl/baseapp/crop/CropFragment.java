package com.tsl.baseapp.crop;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.Utilities.Constants;
import com.tsl.baseapp.model.event.CropEvent;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public class CropFragment extends BaseViewStateFragment<CropView, CropPresenter> implements CropView {


    @Bind(R.id.crop_view)
    com.lyft.android.scissors.CropView mCropView;
    @Bind(R.id.crop_instructions)
    AppCompatTextView mCropInstructions;
    @Bind(R.id.crop_fab)
    FloatingActionButton mCropFab;

    private Context mContext;
    private CropViewState vs;
    private CropComponant cropComponant;
    private URI uri;
    private static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";
    CompositeSubscription subscriptions = new CompositeSubscription();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_crop;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        setImage();
    }

    @Override
    public ViewState createViewState() {
        return new CropViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (CropViewState) viewState;
    }

    @Override
    public CropPresenter createPresenter() {
        return cropComponant.presenter();
    }

    @Override
    public void showForm() {
        vs.setShowForm();
    }

    @Override
    protected void injectDependencies() {
        cropComponant = DaggerCropComponant.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(CropEvent event){
        File croppedFile = event.getCroppedFile();
        //do something with file
    }

    private void setImage(){
        String picture_type = getActivity().getIntent().getStringExtra(Constants.PICTURE_TYPE);
        Uri filename = Uri.parse(getActivity().getIntent().getStringExtra(Constants.FILENAME));
        Bitmap bitmap = null;
        if (picture_type.equals(Constants.PICTURE_CAPTURE)){
            try {
                uri = new URI(filename.toString());
                Glide.with(this).load(new File(uri)).fitCenter().centerCrop().into(mCropView);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else if (picture_type.equals(Constants.PICTURE_PICK)){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), filename);
                mCropView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.crop_fab)
    public void onCropClicked() {
        presenter.crop(mCropView, mContext);
    }

}
