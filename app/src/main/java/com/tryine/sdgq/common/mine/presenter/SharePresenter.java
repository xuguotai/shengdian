package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.mine.view.ShareView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.ShareInfoBean;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * 作者：qujingfeng
 * 创建时间：2020.08.17 14:28
 */

public class SharePresenter extends BasePresenter {
    public SharePresenter(Context context) {
        super(context);
    }

    ShareView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (ShareView) view;
    }

    public void getShareInfo(String objId, String moduleType, int shareType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("objId", objId);
            obj.put("type", moduleType);
            ApiHelper.generalApi(Constant.sharing, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try{
                        JSONObject data = response.optJSONObject("result");
                        ShareInfoBean bean = new Gson().fromJson(data.toString(), ShareInfoBean.class);
                        mView.onSuccess(shareType, bean);
                    }catch (Exception e){
                        e.getMessage();
                    }

                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("msg"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
