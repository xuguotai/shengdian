package com.tryine.sdgq.common.mall.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.common.mall.view.SearchView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class SearchPresenter extends BasePresenter {

    public SearchPresenter(Context context) {
        super(context);
    }

    SearchView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (SearchView) view;
    }


    /**
     * 搜索热词
     * @param type
     */
    public void searchHot(int type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type",type);//模块0、琴谱搜索、1、视频搜索、2商品搜索 3、直播搜索
            ApiHelper.generalApi(Constant.searchHot, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<LabelBean>>() {
                    }.getType();
                    List<LabelBean> labelBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetLabelBeanSuccess(labelBeanList);
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
