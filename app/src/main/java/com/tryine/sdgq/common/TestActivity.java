package com.tryine.sdgq.common;

import android.content.Context;
import android.content.Intent;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.user.activity.LoginActivity;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-21 16:38
 */
public class TestActivity extends BaseActivity {


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_homepage1;
    }

    @Override
    protected void init() {

    }
}
