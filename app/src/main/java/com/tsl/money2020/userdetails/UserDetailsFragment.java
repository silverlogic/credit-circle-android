package com.tsl.money2020.userdetails;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.orhanobut.hawk.Hawk;
import com.rey.material.widget.Button;
import com.rey.material.widget.SnackBar;
import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseApplication;
import com.tsl.money2020.base.BaseViewStateFragment;
import com.tsl.money2020.model.event.BaseEvent;
import com.tsl.money2020.model.objects.user.UpdateUser;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.model.objects.user.UserFinder;
import com.tsl.money2020.utils.Base64;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class UserDetailsFragment extends BaseViewStateFragment<UserDetailsView, UserDetailsPresenter>
        implements UserDetailsView {

    @BindView(R.id.edit_image)
    CircleImageView mEditImage;
    @BindView(R.id.edit_first_name)
    EditText mEditFirstName;
    @BindView(R.id.edit_last_name)
    EditText mEditLastName;
    @BindView(R.id.confirm_changes_button)
    Button mConfirmChangesButton;
    private Context mContext;
    private UserDetailsViewState vs;
    private UserDetailsComponant mUserDetailsComponant;
    public static final String USER_ID = "user";
    public static final String IS_CURRENT_USER = "isCurrentUser";
    private ProgressDialog mProgressBar;
    private User mUser;
    private boolean isCurrentUser;

    private static final int TAKE_PICTURE_REQUEST = 0;
    private static final int PICK_IMAGE_REQUEST = 1;
    String[] permissions = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    private static final int PERMISSIONS_ID = 3;
    SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyyMMdd_HHmmss");
    String dt = sdf.format(new Date());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_user_details;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        Intent intent = getActivity().getIntent();
        int userId = intent.getIntExtra(USER_ID, 0);
        mUser = UserFinder.find(userId);
        isCurrentUser = intent.getBooleanExtra(IS_CURRENT_USER, false);
        String name = mUser.getFirst_name() + " " + mUser.getLast_name();
        if (!isCurrentUser) {
            setEnabled(false);
            if (!name.equals(" ")) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(name);
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.action_profile));
            }
        }
    }

    @Override
    public ViewState createViewState() {
        return new UserDetailsViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (UserDetailsViewState) viewState;
        if (isCurrentUser && Utils.isNetworkAvailable(mContext)){
            // fetch any updates for current user
            presenter.fetchUser(mContext);
        }
        else {
            showForm();
        }
    }

    @Override
    public UserDetailsPresenter createPresenter() {
        return mUserDetailsComponant.presenter();
    }

    @Override
    public void showForm() {
        vs.setShowForm();
        Glide.with(mContext)
                .load(mUser.getUserImages().getFull_size())
                .into(mEditImage);
        mEditFirstName.setText(mUser.getFirst_name());
        mEditLastName.setText(mUser.getLast_name());
    }

    @Override
    public void showError(String error) {
        vs.setShowError();
        mProgressBar.hide();
        setEnabled(true);
        SnackBar.make(mContext)
                .applyStyle(R.style.SnackBarSingleLine)
                .text(error)
                .singleLine(true)
                .duration(3000)
                .show(getActivity());
    }

    @Override
    public void showLoading(String message) {
        vs.setShowLoading();
        mProgressBar = new ProgressDialog(mContext);
        mProgressBar.setMessage(message);
        mProgressBar.show();
        setEnabled(false);
    }

    @Override
    public void updateUserSuccess() {
        vs.setUpdateUserSuccess();
        mProgressBar.hide();
        setEnabled(true);
        SnackBar.make(mContext)
                .applyStyle(R.style.SnackBarSingleLine)
                .text(mContext.getString(R.string.successful_update))
                .singleLine(true)
                .duration(3000)
                .show(getActivity());
    }

    @Override
    public void fetchUserSuccess() {
        vs.setFetchUserSuccess();
        mProgressBar.hide();
        setEnabled(true);
        int userId = Hawk.get(Constants.USER_ID);
        mUser = UserFinder.find(userId);
        showForm();
    }

    @Override
    protected void injectDependencies() {
        mUserDetailsComponant = DaggerUserDetailsComponant.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(BaseEvent event) {
        // Do stuff with Eventbus event
    }

    private void setEnabled(boolean enabled) {
        mEditImage.setEnabled(enabled);
        mEditFirstName.setEnabled(enabled);
        mEditLastName.setEnabled(enabled);
        if (enabled) {
            mConfirmChangesButton.setVisibility(View.VISIBLE);
        } else {
            mConfirmChangesButton.setVisibility(View.GONE);
        }
    }

    private void validate(){
        boolean valid = true;

        String first_name = mEditFirstName.getText().toString();
        String last_name = mEditLastName.getText().toString();
        final Resources r = mContext.getResources();

        if (first_name.isEmpty()) {
            mEditFirstName.setError(r.getString(R.string.enter_first_name));
            valid = false;
        } else {
            mEditFirstName.setError(null);
        }

        if (last_name.isEmpty()) {
            mEditLastName.setError(r.getString(R.string.enter_last_name));
            valid = false;
        } else {
            mEditLastName.setError(null);
        }
        if (valid){
            Drawable drawable = mEditImage.getDrawable();
            if (drawable != null ){
                Bitmap bitmap = ((BitmapDrawable)drawable.getCurrent()).getBitmap();
                String image = bitmapToBase64(bitmap);
                UpdateUser user = new UpdateUser(mUser.getId(), first_name, last_name, image);
                presenter.updateUser(mContext, user);
            }
            else {
                UpdateUser user = new UpdateUser(mUser.getId(), first_name, last_name);
                presenter.updateUser(mContext, user);
            }
        }
    }


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String s = Base64.encode(byteArray);
        return "data:image/png;base64," + s;
    }

    private void photoOption() {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(mContext);
        adapter.add(new MaterialSimpleListItem.Builder(mContext)
                .content(getString(R.string.take_picture))
                .icon(R.drawable.ic_camera)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(mContext)
                .content(getString(R.string.choose_picture))
                .icon(R.drawable.ic_photo)
                .backgroundColor(Color.WHITE)
                .build());

        new MaterialDialog.Builder(mContext)
                .title(R.string.picture_options)
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        MaterialSimpleListItem item = adapter.getItem(which);
                        if (which == TAKE_PICTURE_REQUEST) {
                            getCamera();
                        } else {
                            Intent intent = new Intent();
                            // Show only images, no videos or anything else
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture)), PICK_IMAGE_REQUEST);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void getCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if permissions have been granted
            if (mContext.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                // If they have not been granted before, show a Toast to tell the user they need to accept permission
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(mContext, getString(R.string.camera_permission), Toast.LENGTH_LONG)
                            .show();
                }

                requestPermissions(permissions, PERMISSIONS_ID);
            } else {
                takePicture();
            }
        } else {
            takePicture();
        }
    }

    private void takePicture() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        dt = sdf.format(new Date());
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "BaseApp_Camera_" + dt + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    @OnClick({R.id.edit_image, R.id.confirm_changes_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_image:
                photoOption();
                break;
            case R.id.confirm_changes_button:
                if (Utils.isNetworkAvailable(mContext)) validate();
                else Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                takePicture();
            } else {
                // Permission request was denied.
                Toast.makeText(mContext, "Permission request was denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "BaseApp_Camera_" + dt + ".jpg");
            Uri imageUri = Uri.fromFile(file);
            Glide.with(mContext)
                    .load(imageUri)
                    .into(mEditImage);

        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            Glide.with(mContext)
                    .load(imageUri)
                    .into(mEditImage);

        }
    }
}
