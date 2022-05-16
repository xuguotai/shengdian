package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.mine.view.PersonalView;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalPresenter extends BasePresenter {

    public PersonalPresenter(Context context) {
        super(context);
    }

    PersonalView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (PersonalView) view;
    }

    /**
     * 修改头像
     *
     * @param avatar
     */
    public void updateAvatar(String avatar) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("avatar", avatar);
            ApiHelper.generalApi(Constant.updateuserinfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onUpdateSuccess();
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


    public void uploadFile(String file) {
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
                    String str = response.body().string();
                    JSONObject data = new JSONObject(str);
                    JSONObject resultData = new JSONObject(data.optString("result"));
                    if ("200".equals(data.optString("code"))) {
                        mView.onUpdateAvatarSuccess(resultData.optString("url"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 获取个人用户信息
     */
    public void userdetail(){
        try{
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.userdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if("200".equals(response.optString("code"))){
                        UserBean bean = new Gson().fromJson(response.optString("result"), UserBean.class);
                        mView.onGetUserdetailSuccess(bean);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void usercampusid(){
        try{
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.usercampusid, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onCampusNameSuccess(result.optString("campusName"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateName(JSONObject obj) {
        try {
            ApiHelper.generalApi(Constant.updateuserinfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onUpdateSuccess();
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
