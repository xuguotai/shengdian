package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class RechargPresenter extends BasePresenter {

    public RechargPresenter(Context context) {
        super(context);
    }

    RechargeView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (RechargeView) view;
    }


    /**
     * 充值
     */
    public void recharge(String unitPrice, String goldenBean) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("unitPrice", unitPrice); //商品金额
            obj.put("goldenBean", goldenBean); //金豆数量
            ApiHelper.generalApi(Constant.recharge, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = new JSONObject(response.optString("result"));
                        mView.onCreateOrderSuccess(obj.optString("orderNo"));
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
     * 充值
     */
    public void recharge1(String orderNo, String paymentType, String userId, int payType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderNo", orderNo);
            obj.put("paymentType", paymentType); //支付类型(APP:微信APP支付 ALI:支付宝 IOS:苹果 JSAPI:微信小程序支付)
            obj.put("userId", userId);
            ApiHelper.generalApi(Constant.recharge1, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onRechargeSuccess(response.toString(), payType);
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
     * 查询用户金豆，银豆
     */
    public void getUserbean() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getUserbean, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetUserbeanSuccess(result.optInt("goldenBean"), result.getInt("silverBean"));

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
     * 我的钱包
     */
    public void getUserWallet() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getUserWallet, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            JSONObject silverBean = new JSONObject(result.optString("silverBean"));
                            mView.onGetUserWalletSuccess(silverBean.optInt("goldenBean"),
                                    silverBean.optInt("goldenBeanobtain"),
                                    silverBean.optInt("goldenBeanconp"),
                                    silverBean.optInt("silverBean"),
                                    silverBean.optInt("silverBeanbtain"),
                                    silverBean.optInt("silverBeanconp"));

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
     * 钱包明细
     *
     * @param beanType
     * @param type
     */
    public void getWalletList(String beanType, String type, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("beanType", beanType);//类型：0银豆，1金豆
            if (!type.equals("全部")) {
                if (type.equals("获得")) {
                    obj.put("type", "0");//类型 0-获得 1-消耗 2-转赠
                } else if (type.equals("消耗")) {
                    obj.put("type", "1");//类型 0-获得 1-消耗 2-转赠
                } else if (type.equals("转赠")) {
                    obj.put("type", "2");//类型 0-获得 1-消耗 2-转赠
                }
            }
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");

            ApiHelper.generalApi(Constant.getWalletList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<PayRecordBean>>() {
                        }.getType();
                        List<PayRecordBean> payRecordBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetWalletListSuccess(payRecordBeans, pages);

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
     * 转增
     *
     * @param beanType
     * @param beanCount
     * @param mobile
     */
    public void turnadd(int beanType, String beanCount, String mobile, String remake) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("beanType", beanType);// 豆子类型 0-金豆 1-银豆
            obj.put("beanCount", beanCount);// 豆子数量
            obj.put("mobile", mobile);//手机号码
            obj.put("remake", remake);//原因
            ApiHelper.generalApi(Constant.turnadd, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onRechargeSuccess("", 0);
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
     * 获取提现比例
     */
    public void proportion() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.proportion, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetProportionSuccess(
                                    result.optString("teaBean"),
                                    result.optString("realStatus"),
                                    result.optString("miniWithdraw"),
                                    result.optString("serviceCharge"),
                                    result.optString("proportion"));
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
     * 提现
     *
     * @param paymentType
     * @param withdrawAmount
     * @param serviceCharge
     * @param openid
     */
    public void withdraw(String paymentType, String withdrawAmount, String serviceCharge, String openid, String payPassword) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("paymentType", paymentType);//  支付类型(WXAPP:微信APP支付 ALI:支付宝 JSAPI:微信小程序支付)
            obj.put("withdrawAmount", withdrawAmount);// 提现金额
            obj.put("serviceCharge", serviceCharge);//提现手续费率
            obj.put("openid", openid);//openid
            obj.put("code", "");
            obj.put("payPassword", payPassword);
            obj.put("alipayAccount", "");//     支付宝账号（支付类型为ALI必填）
            obj.put("alipayRealName", "");//     支付宝账号姓名（支付类型为ALI必填）
            ApiHelper.generalApi(Constant.withdraw, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onWithdrawSuccess();
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

    public void withdrawALI(String paymentType, String withdrawAmount, String serviceCharge,String payPassword, String alipayAccount, String alipayRealName) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("paymentType", paymentType);//  支付类型(WXAPP:微信APP支付 ALI:支付宝 JSAPI:微信小程序支付)
            obj.put("withdrawAmount", withdrawAmount);// 提现金额
            obj.put("serviceCharge", serviceCharge);//提现手续费率
            obj.put("openid", "");//openid
            obj.put("code", "");
            obj.put("payPassword", payPassword);
            obj.put("alipayAccount", alipayAccount);//     支付宝账号（支付类型为ALI必填）
            obj.put("alipayRealName", alipayRealName);//     支付宝账号姓名（支付类型为ALI必填）
            ApiHelper.generalApi(Constant.withdraw, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onWithdrawSuccess();
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
     * 查询协议
     * @param type
     */
    public void getAgreement(int type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", type);
            ApiHelper.generalApi(Constant.getAgreement, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetProtocolSuccess(result.optString("agreement"));
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
