package com.tryine.sdgq.common.user.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.view.LoginView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import okhttp3.Call;


public class LoginPresenter extends BasePresenter {
    public LoginPresenter(Context context) {
        super(context);
    }

    LoginView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (LoginView) view;
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param type
     */
    public void getCode(String phone, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phoneNo", phone);
            obj.put("type", type);
            ApiHelper.generalApi(Constant.register, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    mView.onCodeSuccess();
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 验证码登录
     *
     * @param phoneNo
     * @param vcCode
     */

    public void loginByVcCode(String phoneNo, String vcCode) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phoneNo", phoneNo);
            obj.put("vcCode", vcCode);
            ApiHelper.generalApi(Constant.register, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    JSONObject data = response.optJSONObject("data");
                    UserBean bean = new Gson().fromJson(data.toString(), UserBean.class);
                    mView.onLoginSuccess(bean);
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 密码登录
     *
     * @param phone
     * @param password
     */
    public void loginByPassword(String phone, String password) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phone", phone);
            obj.put("password", password);
            obj.put("loginType", "2");
            ApiHelper.generalApi(Constant.loginByPassword, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        UserBean bean = new Gson().fromJson(response.optString("result"), UserBean.class);
                        mView.onLoginSuccess(bean);
                    }
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方登录
     *
     * @param type
     */
    public void otherLogin(String type, String openId, String nickName, String headimgurl) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", type); //1微信 2qq
            obj.put("openId", openId);
            ApiHelper.generalApi(Constant.otherLogin, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            if (TextUtils.isEmpty(result.optString("isBand"))) {
                                UserBean bean = new Gson().fromJson(response.optString("result"), UserBean.class);
                                mView.onLoginSuccess(bean);
                            } else {
                                mView.onBind(type, openId, nickName, headimgurl);
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取用户UserSign(IM)
     */
    public void getUsersign() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getUsersign, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            if (!TextUtils.isEmpty(result.optString("userSign"))) {
                                mView.onGetUsersign(result.optString("userSign"));
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置极光推送ID
     */
    public void setRegistrationId() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", SPUtils.getUserId());
            obj.put("registrationId", Parameter.JPushRegistrationID);
            ApiHelper.generalApi(Constant.setRegistrationId, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
