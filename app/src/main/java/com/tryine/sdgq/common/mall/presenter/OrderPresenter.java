package com.tryine.sdgq.common.mall.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.common.mall.view.OrderView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderPresenter extends BasePresenter {

    public OrderPresenter(Context context) {
        super(context);
    }

    OrderView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (OrderView) view;
    }

    /**
     * 订单列表
     */
    public void getOrderList(int pageNum, String status,String goodsName) {
        try {
            JSONObject obj = new JSONObject();
            if (!"-1".equals(status)) {
                obj.put("status", status);//单状态 (0:订单关闭；1：待付款；2:订单支付超时；3:已取消 4：待评价；5:已完成 ；6:退款中；7:退款完成 8待自提)
            }
            obj.put("goodsName", goodsName);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getOrderList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<OrderListBean>>() {
                        }.getType();

                        List<OrderListBean> orderListBeanList1 = new ArrayList<>();
                        List<OrderListBean> orderListBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        for (int i = 0; i < orderListBeanList.size(); i++) {
                            OrderListBean orderListBean = orderListBeanList.get(i);
                            orderListBean.setStatus(orderListBean.getPiratesOrderDetailListVoList().get(0).getStatus());
                            orderListBean.setStatusVal(orderListBean.getPiratesOrderDetailListVoList().get(0).getStatusVal());
                            orderListBean.setCreateTime(orderListBean.getPiratesOrderDetailListVoList().get(0).getCreateTime());
                            orderListBean.setAutoCancelTime(orderListBean.getPiratesOrderDetailListVoList().get(0).getAutoCancelTime());
                            orderListBean.setLongAutoCancelTime(orderListBean.getPiratesOrderDetailListVoList().get(0).getLongAutoCancelTime());
                            orderListBean.setDetailOrderNo(orderListBean.getPiratesOrderDetailListVoList().get(0).getDetailOrderNo());
                            orderListBean.setPickAddress(orderListBean.getPiratesOrderDetailListVoList().get(0).getPickAddress());
                            orderListBean.setReceiptId(orderListBean.getPiratesOrderDetailListVoList().get(0).getReceiptId());
                            orderListBean.setReceiptAddress(orderListBean.getPiratesOrderDetailListVoList().get(0).getReceiptAddress());
                            orderListBeanList1.add(orderListBean);
                        }
                        int pages = result.optInt("pages");
                        mView.onGetOrderBeanListSuccess(orderListBeanList1, pages);
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


    public void getOrderDetail(int pageNum, String status) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("status", status);//单状态 (0:订单关闭；1：待付款；2:订单支付超时；3:已取消 4：待评价；5:已完成 ；6:退款中；7:退款完成 8待自提)
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getOrderList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<OrderListBean>>() {
                        }.getType();
                        List<OrderListBean> orderListBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetOrderBeanListSuccess(orderListBeanList, pages);
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
     * 确认收货
     * @param orderNo
     */
    public void determine(String orderNo) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderNo", orderNo);
            ApiHelper.generalApi(Constant.determine, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onDetermineSuccess();
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
     * 取消订单
     * @param orderNo
     */
    public void cancelorder(String orderNo) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderNo", orderNo);
            ApiHelper.generalApi(Constant.cancelorder, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onCancelorderSuccess();
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
     * 支付
     * @param orderNo
     */
    public void payOrder(String orderNo,String payPassword) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderNo", orderNo);
            obj.put("payPassword", payPassword);
            ApiHelper.generalApi(Constant.payOrder, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            mView.onPayOrderSuccess();
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
     * 获取校区
     */
    public void getFicationList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getFicationList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CampusBean>>() {
                        }.getType();
                        List<CampusBean> campusBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetCampusBeanListSuccess(campusBeanList);
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

    public void uploadFile(String file, int type) {
        String suffix = file.substring(file.lastIndexOf(".") + 1);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), new File(file));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "temp." + suffix, fileBody)
                .addFormDataPart("type", "1")
                .build();
        Request request = new Request.Builder()
                .url(Constant.uploadFile)
                .addHeader("Authorization", SPUtils.getToken())
                .addHeader("Terminaltype", "Android")
                .addHeader("platform", "app")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(60000, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mView.onFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    //{"code":"200","message":"操作成功!","result":{"url":"https://sdgq-1308358049.cos.ap-beijing.myqcloud.com
                    // /images/65d94752-d35e-48a9-8fb5-c39476e70529.jpg"}}
                    String str = response.body().string();
                    JSONObject data = new JSONObject(str);
                    JSONObject resultData = new JSONObject(data.optString("result"));
                    if ("200".equals(data.optString("code"))) {
                        mView.onUploadFileSuccess(resultData.optString("url"), type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 申请退款
     * @param obj
     */
    public void refund(JSONObject obj) {
        try {
            ApiHelper.generalApi(Constant.refund, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onRefundSuccess();
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
     * 评价
     * @param obj
     */
    public void comment(JSONObject obj, int i) {
        try {
            ApiHelper.generalApi(Constant.comment, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onCommentSuccess(i);
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
