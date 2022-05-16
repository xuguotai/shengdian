package com.tryine.sdgq.common.user.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tryine.sdgq.Application;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.live.activity.ProtocolActivity;
import com.tryine.sdgq.common.live.tencent.liteav.basic.GenerateTestUserSig;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.presenter.LoginPresenter;
import com.tryine.sdgq.common.user.view.LoginView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.config.RequestCode;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.AgreementDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 *
 * @author: zhangshuaijun
 * @time: 2021-11-17 13:34
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.cb_checkbox)
    CheckBox cb_checkbox;

    LoginPresenter loginPresenter;

    /***QQ登录授权需要***/
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    /******/


    /***微信登录授权需要***/
    private IWXAPI api;
    private String nickname;
    private String headimgurl;
    private String openid;
    /******/

    private String TAG = "LoginActivity";

    private Boolean selectProtocol = false;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        ImmersionBar.with(this).statusBarColor(R.color.transparent).
                statusBarDarkFont(false).
                navigationBarColor(R.color.white).navigationBarDarkIcon(true).init();

        loginPresenter = new LoginPresenter(this);
        loginPresenter.attachView(this);

        mTencent = Tencent.createInstance(Parameter.QQ_APP_ID, LoginActivity.this.getApplicationContext());

        api = WXAPIFactory.createWXAPI(this, Parameter.WX_APP_ID, false);
        api.registerApp(Parameter.WX_APP_ID);

        //显示用户协议弹窗
        boolean isFirst = SPUtils.getBoolean(this, "isAgreement", true);
        if (isFirst) {
            uaDelay();
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.tv_register, R.id.tv_login, R.id.iv_weChat_login, R.id.tv_wjmm, R.id.iv_qq_login, R.id.tv_Service_agreement, R.id.tv_Privacy_policy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                RegisterActivity.start(LoginActivity.this);
                break;
            case R.id.iv_weChat_login:
                if (!cb_checkbox.isChecked()) {
                    ToastUtil.toastLongMessage("请阅读服务协议及隐私政策协议并勾选");
                    return;
                }
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(LoginActivity.this, "您的设备未安装微信客户端", Toast.LENGTH_SHORT).show();
                } else {
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                }
                break;
            case R.id.iv_qq_login:
                if (!cb_checkbox.isChecked()) {
                    ToastUtil.toastLongMessage("请阅读服务协议及隐私政策协议并勾选");
                    return;
                }
                mIUiListener = new BaseUiListener();
                //all表示获取所有权限
                mTencent.login(LoginActivity.this, "all", mIUiListener);
                break;
            case R.id.tv_wjmm:
                RetrievePasswordActivity.start(mContext, "忘记密码");
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_Service_agreement:
                ProtocolActivity.start(mContext, 1);
                break;
            case R.id.tv_Privacy_policy:
                ProtocolActivity.start(mContext, 2);
                break;
        }
    }


    private void login() {
        if ("".equals(getTextStr(et_phone))) {
            ToastUtil.toastLongMessage("请输入手机号码");
            return;
        }
        if (getTextStr(et_phone).length() != 11) {
            ToastUtil.toastLongMessage("请输入正确的手机号码");
            return;
        }
        if ("".equals(getTextStr(et_password))) {
            ToastUtil.toastLongMessage("请输入密码");
            return;
        }
        if (!cb_checkbox.isChecked()) {
            ToastUtil.toastLongMessage("请阅读服务协议及隐私政策协议并勾选");
            closeInput();
            return;
        }
        progressDialog.show();
        loginPresenter.loginByPassword(et_phone.getText().toString(), et_password.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REGISTER_CODE:
                    et_phone.setText(data.getStringExtra("phone"));
                    break;
                case Constants.REQUEST_LOGIN:
                    if (data != null) {
                        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
                        String bean = data.getExtras().get("key_response").toString();
                        try {
                            openid = new JSONObject(bean).get("openid").toString();
                            loginPresenter.otherLogin("2", openid, "", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }


    }

    @Override
    public void onCodeSuccess() {
        progressDialog.show();
    }

    @Override
    public void onLoginSuccess(UserBean bean) {
        progressDialog.dismiss();
        SPUtils.setBoolean(this, Parameter.IS_LOGIN, true);
        SPUtils.saveString(Parameter.USER_ID, bean.getId());
        SPUtils.saveString(Parameter.USER_INFO, new Gson().toJson(bean));
//        final UserModel userModel = new UserModel();
//        userModel.userId = bean.getId();
//        userModel.userName = bean.getUserName();
//        userModel.userAvatar = bean.getAvatar();
//        userModel.userSig = GenerateTestUserSig.genTestUserSig(bean.getId());
//        final UserModelManager manager = UserModelManager.getInstance();
//        manager.setUserModel(userModel);
        loginPresenter.setRegistrationId();
        MainActivity.start(mContext);
        finish();
    }

    @Override
    public void onBind(String type, String openId, String nickName, String headimgurl) {
        BindAccountActivity.start(mContext, type, openId, nickName, headimgurl);
    }

    @Override
    public void onGetUsersign(String userSign) {

    }

    @Override
    public void onFailed(String message) {
        progressDialog.dismiss();
        ToastUtil.toastLongMessage(message);

    }


    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }

                    @Override
                    public void onWarning(int i) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onWarning(int i) {

        }

    }

    /**
     * 微信登录回调返回
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String responseInfo = sp.getString("responseInfo", "");

        if (!responseInfo.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(responseInfo);
                nickname = jsonObject.getString("nickname");
                headimgurl = jsonObject.getString("headimgurl");
                openid = jsonObject.getString("openid");
                loginPresenter.otherLogin("1", openid, nickname, headimgurl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }
    }


    /**
     * 用户协议延时弹窗
     */
    private void uaDelay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showAgreementDialog();
                    break;
            }
        }
    };

    //用户协议弹窗
    private void showAgreementDialog() {
        AgreementDialog mAgreementDialog = new AgreementDialog(mContext);
        mAgreementDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAgreementDialog.dismiss();
                //标记不是第一次
                SPUtils.setBoolean(LoginActivity.this, "isAgreement", false);
                Application.initSDK(LoginActivity.this);
            }
        });
        mAgreementDialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAgreementDialog.show();
    }


}
