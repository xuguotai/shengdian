package com.tryine.sdgq.common.user.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.view.RegisterView;
import com.tryine.sdgq.mvp.BasePresenter;

import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 作者：qujingfeng
 * 创建时间：2020.08.17 14:37
 */

public class RegisterPresenter extends BasePresenter {
    public RegisterPresenter(Context context) {
        super(context);
    }

    RegisterView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (RegisterView) view;
    }

    /**
     * 注册
     *
     * @param userPhone
     * @param userPassword
     * @param verifityCode
     */
    public void register(String userPhone, String userPassword, String verifityCode) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userPhone", userPhone);
            obj.put("userPassword", userPassword);
            obj.put("verifityCode", verifityCode);
            ApiHelper.generalApi(Constant.register, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onRegisterSuccess();
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
     * 获取验证码
     *
     * @param phone
     */
    public void getCode(String phone) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phone", phone);
            ApiHelper.generalApi(Constant.getCode, obj, new JsonCallBack() {
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
     * 绑定手机
     *
     * @param userPhone
     * @param openId
     * @param verifityCode
     */
    public void bindLogin(String userPhone, String openId, String verifityCode,String nickName,String headimgurl) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phone", userPhone);
            obj.put("openId", openId);
            obj.put("code", verifityCode);
            obj.put("nickName", nickName);
            obj.put("headimgurl", headimgurl);
            ApiHelper.generalApi(Constant.bindLogin, obj, new JsonCallBack() {
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
     * 找回密码
     * @param password
     * @param phone
     * @param code
     */
    public void updatepassword(String password, String phone,String code) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("password", password);
            obj.put("phone", phone);
            obj.put("code", code);
            ApiHelper.generalApi(Constant.updatepassword, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUpdatepasswordSuccess();
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
     * 设置支付密码
     * @param phone
     * @param code
     * @param payPassword
     */
    public void updatepaypassword(String phone, String code,String payPassword) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phone", phone);
            obj.put("code", code);
            obj.put("payPassword", payPassword);
            ApiHelper.generalApi(Constant.updatepaypassword, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUpdatepasswordSuccess();
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

}
