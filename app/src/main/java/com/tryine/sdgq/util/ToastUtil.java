package com.tryine.sdgq.util;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tryine.sdgq.Application;

/**
 * UI通用方法类
 */
public class ToastUtil {

    @Nullable
    private static Toast mToast;

    public static final void toastLongMessage(final String message) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(Application.getApplication(), null, Toast.LENGTH_LONG);
                mToast.setText(message);
                mToast.show();
            }
        });
    }


    public static final void toastShortMessage(final String message) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(Application.getApplication(), null, Toast.LENGTH_SHORT);
                mToast.setText(message);
                mToast.show();
            }
        });
    }
}
