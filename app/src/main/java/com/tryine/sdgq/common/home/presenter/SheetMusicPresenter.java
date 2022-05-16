package com.tryine.sdgq.common.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.view.SheetMusicView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class SheetMusicPresenter extends BasePresenter {

    public SheetMusicPresenter(Context context) {
        super(context);
    }

    SheetMusicView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (SheetMusicView) view;
    }

    /**
     * 获取琴谱列表
     */
    public void getQpTypeList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getQpTypeList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetHomeMenuBeanSuccess(homeMenuBeanList);
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
     * 根据琴谱类型id查询琴谱列表
     *
     * @param id
     */
    public void getQpChildTypeList(String id, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getQpChildTypeList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                        }.getType();
                        List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetChildMenuSuccess(homeMenuBeanList, pages);
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
     * 我的琴谱
     * @param id
     * @param pageNum
     */
    public void getMypiaonscorelist(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getMypiaonscorelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                        }.getType();
                        List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetChildMenuSuccess(homeMenuBeanList, pages);
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
     * 购买琴谱
     *
     * @param id
     */
    public void buypiaonscore(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.buypiaonscore, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onBuypiaonscoreSuccess();
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

    /**
     * 琴谱详情
     *
     * @param id
     */
    public void getpiaonscoredetail(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.getpiaonscoredetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject resultObj = new JSONObject(response.optString("result"));
                        SheetMusicBean sheetMusicBean = new Gson().fromJson(resultObj.optString("piaonScore"), SheetMusicBean.class);
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModelList = new Gson().fromJson(resultObj.optJSONArray("videoList").toString(), collectionType);
                        mView.onGetpiaonscoredetailSuccess(sheetMusicBean, videoModelList);
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
     * 搜索琴谱
     * @param name
     * @param pageNum
     */
    public void getSearchMusic(String name, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getSearchMusic, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                        }.getType();
                        List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetChildMenuSuccess(homeMenuBeanList, pages);
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
