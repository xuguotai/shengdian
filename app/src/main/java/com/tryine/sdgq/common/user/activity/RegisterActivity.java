package com.tryine.sdgq.common.user.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.live.activity.ProtocolActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.presenter.RegisterPresenter;
import com.tryine.sdgq.common.user.view.RegisterView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.Utils;
import com.tryine.sdgq.view.CountDownTimerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 *
 * @author: zhangshuaijun
 * @time: 2021-11-17 13:34
 */
public class RegisterActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.et_userPhone)
    EditText et_userPhone;
    @BindView(R.id.et_verifityCode)
    EditText et_verifityCode;
    @BindView(R.id.et_userPassword)
    EditText et_userPassword;
    @BindView(R.id.tv_verifityCode)
    TextView tv_verifityCode;

    @BindView(R.id.cb_agree)
    CheckBox cb_agree;

    RegisterPresenter registerPresenter;

    CountDownTimerView countDownTimerView;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, RegisterActivity.class);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        ImmersionBar.with(this).statusBarColor(R.color.transparent).
                statusBarDarkFont(false).
                navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();

        registerPresenter = new RegisterPresenter(this);
        registerPresenter.attachView(this);

    }


    @OnClick({R.id.tv_submit, R.id.tv_verifityCode,R.id.tv_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                register();
                break;
            case R.id.tv_agreement:
                ProtocolActivity.start(mContext,1);
                break;
            case R.id.tv_verifityCode:
                if ("".equals(getTextStr(et_userPhone))) {
                    ToastUtil.toastLongMessage("请输入手机号码");
                    return;
                }
                if (getTextStr(et_userPhone).length() != 11) {
                    ToastUtil.toastLongMessage("请输入正确的手机号码");
                    return;
                }
                if (Utils.isFastClick()) {
                    registerPresenter.getCode(getTextStr(et_userPhone));
                }


                break;
        }
    }


    private void register() {

        if ("".equals(getTextStr(et_userPhone))) {
            ToastUtil.toastLongMessage("请输入手机号码");
            return;
        }
        if (getTextStr(et_userPhone).length() != 11) {
            ToastUtil.toastLongMessage("请输入正确的手机号码");
            return;
        }
        if ("".equals(getTextStr(et_verifityCode))) {
            ToastUtil.toastLongMessage("请输入验证码");
            return;
        }
        if ("".equals(getTextStr(et_userPassword))) {
            ToastUtil.toastLongMessage("请输入密码");
            return;
        }
        if (getTextStr(et_userPassword).length() < 6) {
            ToastUtil.toastLongMessage("密码不能小于6位");
            return;
        }

        if (!cb_agree.isChecked()) {
            ToastUtil.toastLongMessage("请阅读并同意服务协议");
            return;
        }
        progressDialog.show();
        registerPresenter.register(et_userPhone.getText().toString(), et_userPassword.getText().toString(), getTextStr(et_verifityCode));

    }


    @Override
    public void onLoginSuccess(UserBean bean) {

    }

    @Override
    public void onRegisterSuccess() {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage("注册成功");
        Intent intent = new Intent();
        intent.putExtra("phone", et_userPhone.getText().toString());
        setResult(1, intent);
        finish();
    }

    @Override
    public void onCodeSuccess() {
        countDownTimerView = new CountDownTimerView(tv_verifityCode, "#FFFFFF", 60000, 1000);
        countDownTimerView.start();
    }

    @Override
    public void onUpdatepasswordSuccess() {

    }

    @Override
    public void onFailed(String message) {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);
    }
}
