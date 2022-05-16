package com.tryine.sdgq.common.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.view.CampusView;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class CampusPresenter extends BasePresenter {

    public CampusPresenter(Context context) {
        super(context);
    }

    CampusView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (CampusView) view;
    }

    /**
     * 获取校区
     */
    public void getFicationList(int pageNum, String lonLat, String city, String searchStr) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("lonLat", lonLat);
            if (city.equals("北京")) {
                obj.put("city", "110000");
            } else if (city.equals("长沙")) {
                obj.put("city", "430100");
            } else if (city.equals("武汉")) {
                obj.put("city", "420100");
            }
            obj.put("name", searchStr);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getFicationList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CampusBean>>() {
                        }.getType();
                        List<CampusBean> campusBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCampusBeanListSuccess(campusBeanList, pages);
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
