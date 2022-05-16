package com.tryine.sdgq.common.circle.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.view.BannerView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.view.HomeView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class BannerPresenter extends BasePresenter {

    public BannerPresenter(Context context) {
        super(context);
    }

    BannerView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (BannerView) view;
    }

    /**
     * 获取Banner
     *
     * @param type 模块0、首页、1、圣典视频、2直播课 3、商城4、话题
     */
    public void getBannerList(int type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", type);
            ApiHelper.generalApi(Constant.getBannerList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<BannerBean>>() {
                    }.getType();
                    List<BannerBean> bannerBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetBannerBeanListSuccess(bannerBeanList);
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
