package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.mine.view.TeacherView;
import com.tryine.sdgq.common.mine.view.UploadVideoView;
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

public class TeacherPresenter extends BasePresenter {

    public TeacherPresenter(Context context) {
        super(context);
    }

    TeacherView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (TeacherView) view;
    }

    /**
     * 获取视频首页类型列表
     */
    public void addTeacher(String name, String educationLayer, String graduateSchool, String phone, String remake) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("educationLayer", educationLayer);
            obj.put("graduateSchool", graduateSchool);
            obj.put("phone", phone);
            obj.put("remake", remake);
            ApiHelper.generalApi(Constant.addTeacher, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onAddTeacherSuccess();
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
