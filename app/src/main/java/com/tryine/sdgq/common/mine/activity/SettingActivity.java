package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.ActivityManager;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.live.activity.ProtocolActivity;
import com.tryine.sdgq.common.user.activity.LoginActivity;
import com.tryine.sdgq.common.user.activity.PayPasswordActivity;
import com.tryine.sdgq.common.user.activity.RetrievePasswordActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideCacheUtil;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统设置
 *
 * @author: zhangshuaijun
 * @time: 2021-11-19 15:09
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_date)
    TextView tv_date;
    UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("系统设置");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        tv_size.setText(GlideCacheUtil.getInstance().getCacheSize(this));
        tv_date.setText(userBean.getCreateTime());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }


    @OnClick({R.id.iv_black, R.id.ll_btn_out, R.id.ll_pwd, R.id.ll_phone, R.id.rl_sysm, R.id.rl_hc,R.id.ll_paypwd,R.id.rl_yszc,R.id.rl_fwxy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_yszc:
                //跳转隐私政策
                ProtocolActivity.start(mContext,2);
                break;
            case R.id.rl_fwxy:
                //跳转用户协议
                ProtocolActivity.start(mContext,1);
                break;
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_btn_out:
                PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                        "是否退出当前账号", "确认", "取消");
                promptDialog.show();
                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public void insure() {
                        SPUtils.setBoolean(mContext, Parameter.IS_LOGIN, false);
                        LoginActivity.start(mContext);
                        ActivityManager.getScreenManager().popActivity(MainActivity.class);
                        finish();
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.ll_pwd:
                RetrievePasswordActivity.start(mContext,"设置密码");
                break;
            case R.id.ll_phone:
                SetPhoneActivity.start(mContext);
                break;
            case R.id.rl_sysm:
                ProtocolActivity.start(mContext, 5);
                break;
            case R.id.ll_paypwd:
                PayPasswordActivity.start(mContext);
                break;
            case R.id.rl_hc:
                GlideCacheUtil.getInstance().clearImageAllCache(this);
                ToastUtil.toastLongMessage("清除成功");
                tv_size.setText("0MB");
                break;
        }
    }

}
