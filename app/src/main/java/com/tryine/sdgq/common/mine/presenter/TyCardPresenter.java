package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.common.mine.view.CollectView;
import com.tryine.sdgq.common.mine.view.TyCardView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class TyCardPresenter extends BasePresenter {

    public TyCardPresenter(Context context) {
        super(context);
    }

    TyCardView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (TyCardView) view;
    }


    public void getMyCardList(int status, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("status", status);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getMyCardList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CardBean>>() {
                        }.getType();
                        List<CardBean> cardBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCardBeanListSuccess(cardBeanList, pages);
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


    public void getMyCardSearchList(String key, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("key", key);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getMyCardList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CardBean>>() {
                        }.getType();
                        List<CardBean> cardBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCardBeanListSuccess(cardBeanList, pages);
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


    public void getexperiencelist(String experienceId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("experienceId", experienceId);
            obj.put("pageNum", "1");
            obj.put("pageSize", "110");
            ApiHelper.generalApi(Constant.getexperiencelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<ExperienceBean>>() {
                        }.getType();
                        List<ExperienceBean> experienceBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetExperienceBeanSuccess(experienceBeans, pages);
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


    public void getforwardingdetail(String cardId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", cardId);
            ApiHelper.generalApi(Constant.getforwardingdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CardBean>>() {
                        }.getType();
                        List<CardBean> cardBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCardBeanListSuccess(cardBeanList, pages);
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
     * 转送给好友
     * @param cardId
     * @param mobile
     */
    public void getforwarding(String cardId, String mobile) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", cardId);
            obj.put("mobile", mobile);
            ApiHelper.generalApi(Constant.getforwarding, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                       if(response.optString("code").equals("200")){
                           mView.onForwardingSuccess();
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
