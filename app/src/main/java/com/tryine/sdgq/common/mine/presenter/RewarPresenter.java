package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.mine.view.RewarView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class RewarPresenter extends BasePresenter {

    public RewarPresenter(Context context) {
        super(context);
    }

    RewarView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (RewarView) view;
    }


    /**
     * 我的打赏/我的礼物
     *
     * @param pageNum
     * @param presentType
     * @param incomeType
     */
    public void getGiftRecordList(int pageNum, String presentType, String incomeType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("presentType", presentType);//豆子类型 0:金豆礼物 1:银豆礼物
            obj.put("incomeType", incomeType);//类型  0:我的打赏 1:我的礼物
            ApiHelper.generalApi(Constant.getGiftRecordList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<RewarBean>>() {
                        }.getType();
                        List<RewarBean> rewarBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetRewarBeanListSuccess(rewarBeans, pages);
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
     * 支付记录
     *
     * @param pageNum
     * @param searchDate
     */
    public void getPayList(int pageNum, String searchDate) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("searchDate", searchDate);//日期 yyyy-MM-dd
            ApiHelper.generalApi(Constant.getPayList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<PayRecordBean>>() {
                        }.getType();
                        List<PayRecordBean> payRecordBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetPayRecordBeanListSuccess(payRecordBeans, pages);
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
