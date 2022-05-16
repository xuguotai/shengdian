package com.tryine.sdgq.common.user.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 10:20
 */
public class UpdatePasswordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UpdatePasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_updatepassword;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("修改密码");

    }


    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }


}
