package com.tryine.sdgq.common.user.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.ActivityManager;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.live.tencent.liteav.basic.GenerateTestUserSig;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.presenter.RegisterPresenter;
import com.tryine.sdgq.common.user.view.RegisterView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.Utils;
import com.tryine.sdgq.view.CountDownTimerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定账号
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 11:33
 */
public class BindAccountActivity extends BaseActivity implements RegisterView {
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.et_userPhone)
    EditText et_userPhone;
    @BindView(R.id.et_verifityCode)
    EditText et_verifityCode;
    @BindView(R.id.tv_verifityCode)
    TextView tv_verifityCode;

    RegisterPresenter registerPresenter;

    CountDownTimerView countDownTimerView;
    String type;
    String openId;
    String nickName;
    String headimgurl;

    public static void start(Context context, String type, String openId, String nickName, String headimgurl) {
        Intent intent = new Intent();
        intent.setClass(context, BindAccountActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("openId", openId);
        intent.putExtra("nickName", nickName);
        intent.putExtra("headimgurl", headimgurl);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bindaccount;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("绑定账号");

        type = getIntent().getStringExtra("type");
        openId = getIntent().getStringExtra("openId");
        headimgurl = getIntent().getStringExtra("headimgurl");
        nickName = getIntent().getStringExtra("nickName");

        registerPresenter = new RegisterPresenter(this);
        registerPresenter.attachView(this);

    }


    @OnClick({R.id.tv_submit, R.id.tv_verifityCode})
    public void onClick(View view) {
        switch (view.getId()) {
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
        registerPresenter.bindLogin(getTextStr(et_userPhone), openId, getTextStr(et_verifityCode),nickName,headimgurl);
    }


    @Override
    public void onLoginSuccess(UserBean bean) {
        SPUtils.setBoolean(this, Parameter.IS_LOGIN, true);
        SPUtils.saveString(Parameter.USER_ID, bean.getId());
        SPUtils.saveString(Parameter.USER_INFO, new Gson().toJson(bean));

        final UserModel userModel = new UserModel();
        userModel.userId = bean.getId();
        userModel.userName = bean.getUserName();
        userModel.userAvatar = bean.getAvatar();
        userModel.userSig = GenerateTestUserSig.genTestUserSig(bean.getId());
        final UserModelManager manager = UserModelManager.getInstance();
        manager.setUserModel(userModel);
        MainActivity.start(mContext);
        finish();
        ActivityManager.getScreenManager().popActivity(LoginActivity.class);
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

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
