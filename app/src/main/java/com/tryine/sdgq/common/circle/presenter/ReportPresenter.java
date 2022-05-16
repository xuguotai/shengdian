package com.tryine.sdgq.common.circle.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.ReportTypeBean;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.circle.view.ReportView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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

public class ReportPresenter extends BasePresenter {

    public ReportPresenter(Context context) {
        super(context);
    }

    ReportView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (ReportView) view;
    }

    /**
     * 举报类型
     */
    public void getReportType() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.selectreporttypelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<ReportTypeBean>>() {
                    }.getType();
                    List<ReportTypeBean> bannerBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetReportTypeListSuccess(bannerBeanList);
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
     * 举报
     */
    public void report(JSONObject obj) {
        try {
            ApiHelper.generalApi(Constant.report, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onReportSuccess();
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
     * 投诉建议
     * @param content
     * @param phone
     */
    public void usercomplain(String content, String phone,String campusId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("content", content);// 内容
            obj.put("phone", phone);//    联系电话
            obj.put("campusId", campusId);//    校区
            ApiHelper.generalApi(Constant.usercomplain, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUserComplainSuccess();
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



}
