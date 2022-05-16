package com.tryine.sdgq.common.live.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.view.LiveView;
import com.tryine.sdgq.common.live.view.ProtocolView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class ProtocolPresenter extends BasePresenter {

    public ProtocolPresenter(Context context) {
        super(context);
    }

    ProtocolView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (ProtocolView) view;
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
