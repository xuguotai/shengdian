package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mine.bean.TecheCasBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoRecordBean;
import com.tryine.sdgq.common.mine.view.CollectView;
import com.tryine.sdgq.common.mine.view.MineCourseView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class MineCoursePresenter extends BasePresenter {

    public MineCoursePresenter(Context context) {
        super(context);
    }

    MineCourseView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (MineCourseView) view;
    }


    public void gettecheCaslist( ) {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.gettecheCaslist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        String[] ids = result.optString("couresCatsId").split(",");
                        String[] names = result.optString("couresCatsName").split(",");
                        List<TecheCasBean> techeCasBeans = new ArrayList<>();
                        for (int i = 0; i < ids.length; i++) {
                            TecheCasBean techeCasBean = new TecheCasBean();
                            techeCasBean.setId(ids[i]);
                            techeCasBean.setCouresName(names[i]);
                            techeCasBeans.add(techeCasBean);
                        }
                        mView.onGetTecheCasBeanListSuccess(techeCasBeans, 0);
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
     * 获取学生
     * @param pageNum
     * @param catsId
     */
    public void gettecheCasinfolist(int pageNum,String catsId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("catsId", catsId);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.gettecheCasinfolist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<TecheCasinfoBean>>() {
                        }.getType();
                        List<TecheCasinfoBean> techeCasBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetTecheCasinfoBeanListSuccess(techeCasBeans, pages);
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
     * 上课记录
     * @param pageNum
     * @param userId
     */
    public void gettecheCasinfodetial(int pageNum,String userId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userId", userId);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.gettecheCasinfodetial, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<TecheCasinfoRecordBean>>() {
                        }.getType();
                        List<TecheCasinfoRecordBean> techeCasBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetRecordListSuccess(techeCasBeans, pages);
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
