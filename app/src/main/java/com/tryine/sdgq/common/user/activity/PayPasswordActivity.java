package com.tryine.sdgq.common.user.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.presenter.RegisterPresenter;
import com.tryine.sdgq.common.user.view.RegisterView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.Utils;
import com.tryine.sdgq.view.CountDownTimerView;
import com.tryine.sdgq.view.dialog.VerifyPayPwdDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置支付密码
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 11:33
 */
public class PayPasswordActivity extends BaseActivity implements RegisterView {
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.et_userPhone)
    EditText et_userPhone;
    @BindView(R.id.et_verifityCode)
    EditText et_verifityCode;
    @BindView(R.id.et_payPassword)
    EditText et_payPassword;
    @BindView(R.id.et_payPassword1)
    EditText et_payPassword1;
    @BindView(R.id.tv_verifityCode)
    TextView tv_verifityCode;

    RegisterPresenter registerPresenter;

    CountDownTimerView countDownTimerView;

    UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PayPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_paypassword;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("设置支付密码");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);

        registerPresenter = new RegisterPresenter(this);
        registerPresenter.attachView(this);


        et_userPhone.setText("" + userBean.getMobile());

    }


    @OnClick({R.id.iv_black, R.id.tv_submit, R.id.tv_verifityCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                submit();
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

    private void submit() {
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
        if ("".equals(getTextStr(et_payPassword))) {
            ToastUtil.toastLongMessage("请输入支付密码");
            return;
        }
        if (getTextStr(et_payPassword).length() != 6) {
            ToastUtil.toastLongMessage("密码需为6位");
            return;
        }
        if (!et_payPassword.getText().toString().equals(et_payPassword1.getText().toString())) {
            ToastUtil.toastLongMessage("密码不一致");
            return;
        }

        registerPresenter.updatepaypassword(getTextStr(et_userPhone),getTextStr(et_verifityCode),getTextStr(et_payPassword));
    }


    @Override
    public void onLoginSuccess(UserBean bean) {

    }

    @Override
    public void onRegisterSuccess() {
        ToastUtil.toastLongMessage("修改成功");
        finish();

    }

    @Override
    public void onCodeSuccess() {
        countDownTimerView = new CountDownTimerView(tv_verifityCode, "#FFFFFF", 60000, 1000);
        countDownTimerView.start();
    }

    @Override
    public void onUpdatepasswordSuccess() {
        ToastUtil.toastLongMessage("修改成功");
        UserBean userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        userBean.setPayPassword("1");
        SPUtils.saveString(Parameter.USER_INFO, new Gson().toJson(userBean));
        finish();
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
