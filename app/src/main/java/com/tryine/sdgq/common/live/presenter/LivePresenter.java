package com.tryine.sdgq.common.live.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.view.LiveHomeView;
import com.tryine.sdgq.common.live.view.LiveView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class LivePresenter extends BasePresenter {

    public LivePresenter(Context context) {
        super(context);
    }

    LiveView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (LiveView) view;
    }


    /**
     * 直播课程详情
     *
     * @param id
     */
    public void getLivecoursedetail(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.getLivecoursedetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        LiveCourseBean liveCourseBean = new Gson().fromJson(response.optString("result"), LiveCourseBean.class);
                        mView.onLiveCourseBeanSuccess(liveCourseBean);
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
     * 购买课程
     *
     * @param id
     * @param phone
     */
    public void buyLivecourse(String id, String phone, JSONArray carIdArray) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("phone", phone);
            obj.put("idList", carIdArray);
            ApiHelper.generalApi(Constant.buyLivecourse, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onBuyCourseSuccess();
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
     * 开直播-直播课程列表-下拉框所用
     */
    public void selectlivecourse(String courseType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseType", courseType);
            ApiHelper.generalApi(Constant.selectlivecourse, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetCourseBeanSuccess(homeMenuBeanList);
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
     * 开直播-直播章节列表-下拉框所用
     *
     * @param courseId
     */
    public void selectlivecoursedetail(String courseId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", courseId);
            ApiHelper.generalApi(Constant.selectlivecoursedetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetCourseChildBeanSuccess(homeMenuBeanList);
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
     * 开启直播
     *
     * @param courseType
     * @param courseId
     * @param courseDetailId
     * @param title
     * @param quality
     */
    public void addroom(String courseType, String courseId, String courseDetailId, String title, String quality) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseType", courseType); // 课程类型 0-直播大课 1-一对一辅导
            obj.put("courseId", courseId); //课程ID
            obj.put("courseDetailId", courseDetailId); //课程章节ID
            obj.put("title", title); //直播标题
            obj.put("quality", quality);  // 直播画质 0-480标清 1-720高清 2-1080超清
            ApiHelper.generalApi(Constant.addroom, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject obj = new JSONObject(response.optString("result"));
                            if ("1".equals(obj.optString("result"))) {//创建结果 0-创建失败 1-创建成功
                                mView.onAddroomSuccess(obj.optInt("liveId"));
                            }
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

    public void getLiveroomdetail(int liveId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("liveId", liveId);//直播间id
            ApiHelper.generalApi(Constant.getLiveroomdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetliveroomdetailSuccess(liveId, result.optString("trtcPushAddr"));
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
     * 查询是否开直播
     */
    public void getIsLive() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getislive, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject obj = new JSONObject(response.optString("result"));
                            mView.onGetIsLiveSuccess(obj.optInt("isLive"), obj.optInt("realStatus"), obj.optInt("liveId"),obj.optString("pushAddr"));

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
