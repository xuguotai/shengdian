package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-03-07 13:56
 */
public class HdActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hd;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("活动中心");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
    }

    @OnClick({R.id.iv_black, R.id.iv_hd, R.id.iv_yx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.iv_hd:
                H5WebViewJsActivity.start(mContext, "http://ht.2007shengdian.cn/#/activitys");
                break;
            case R.id.iv_yx:
                H5WebViewJsActivity.start(mContext, "http://ht.2007shengdian.cn/#/lottery");
                break;
        }
    }


}
