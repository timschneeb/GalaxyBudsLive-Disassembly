package com.samsung.accessory.neobeanmgr.module.home.drawer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.samsung.accessory.neobeanmgr.R;
import com.samsung.accessory.neobeanmgr.common.ui.OnSingleClickListener;
import com.samsung.accessory.neobeanmgr.common.util.ResponseCallback;
import com.samsung.accessory.neobeanmgr.common.util.SamsungAccountUtil;
import com.samsung.accessory.neobeanmgr.common.util.SeMobileServiceUtil;
import com.samsung.accessory.neobeanmgr.common.util.Util;
import com.samsung.android.sdk.mobileservice.profile.ProfileApi;
import com.samsung.android.sdk.mobileservice.profile.result.ProfileResult;

public class ProfileImageSupport {
    private static final String TAG = "NeoBean_ProfileImageSupport";
    /* access modifiers changed from: private */
    public final Activity mActivity;
    private final OnSingleClickListener mClickListener = new OnSingleClickListener() {
        public void onSingleClick(View view) {
            Log.d(ProfileImageSupport.TAG, "onClick()");
            SamsungAccountUtil.startSettingActivity(ProfileImageSupport.this.mActivity);
        }
    };
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public final ImageView mImageView;

    ProfileImageSupport(Activity activity) {
        this.mActivity = activity;
        this.mImageView = (ImageView) this.mActivity.findViewById(R.id.image_account);
        this.mImageView.setOnClickListener(this.mClickListener);
        clipImageViewToCircle();
        updateUI();
    }

    public void updateUI() {
        Log.d(TAG, "updateUI()");
        boolean isSupport = isSupport();
        this.mImageView.setVisibility(isSupport ? 0 : 4);
        this.mImageView.setEnabled(isSupport);
        if (!isSupport) {
            return;
        }
        if (SamsungAccountUtil.isSignedIn()) {
            SeMobileServiceUtil.getProfileApi(new ResponseCallback() {
                public void onResponse(String str) {
                    ProfileResult profile;
                    byte[] photo;
                    if (str == null) {
                        Log.d(ProfileImageSupport.TAG, "onResponse() : " + getExtraObject());
                        ProfileApi profileApi = (ProfileApi) getExtraObject();
                        if (profileApi != null && (profile = profileApi.getProfile()) != null && profile.getStatus() != null && profile.getStatus().getCode() == 1 && (photo = profile.getResult().getPhotoInstance().getPhoto()) != null) {
                            final Bitmap decodeByteArray = BitmapFactory.decodeByteArray(photo, 0, photo.length);
                            ProfileImageSupport.this.mHandler.post(new Runnable() {
                                public void run() {
                                    ProfileImageSupport.this.mImageView.setImageBitmap(decodeByteArray);
                                    ProfileImageSupport.this.clipImageViewToCircle();
                                }
                            });
                            return;
                        }
                        return;
                    }
                    Log.e(ProfileImageSupport.TAG, "failureReason : " + str);
                    ProfileImageSupport.this.mHandler.post(new Runnable() {
                        public void run() {
                            ProfileImageSupport.this.setImageViewToDefault();
                        }
                    });
                }
            });
        } else {
            setImageViewToDefault();
        }
    }

    /* access modifiers changed from: private */
    public void setImageViewToDefault() {
        this.mImageView.setImageResource(R.drawable.drawer_ic_account_default);
        clipImageViewToCircle();
    }

    /* access modifiers changed from: private */
    public void clipImageViewToCircle() {
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            this.mImageView.setClipToOutline(true);
        }
    }

    static boolean isSupport() {
        boolean z = Build.VERSION.SDK_INT >= 23 && Util.isSamsungDevice() && SamsungAccountUtil.isSettingSupport();
        Log.d(TAG, "isSupport() : " + z);
        return z;
    }
}
