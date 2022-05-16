package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;

import butterknife.BindView;

/**
 * 查看自提地址
 * @author: zhangshuaijun
 * @time: 2021-12-03 14:15
 */
public class LookAddressActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LookAddressActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lookaddress;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("查看地址");


    }
}
