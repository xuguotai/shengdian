package com.tryine.sdgq.common.home.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.AnnouncementBean;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.view.HomeView;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;

public class HomePresenter extends BasePresenter {

    public HomePresenter(Context context) {
        super(context);
    }

    HomeView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (HomeView) view;
    }

    /**
     * 获取Banner
     */
    public void getBannerList() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", "0");
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

    public void getRefreshBannerList() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", "0");
            ApiHelper.generalApi(Constant.getBannerList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<BannerBean>>() {
                    }.getType();
                    List<BannerBean> bannerBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
//                    mView.onGetRefreshBannerBeanListSuccess(bannerBeanList);
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
     * 获取首页菜单列表
     */
    public void getVideoTypeList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getVideoTypeList, obj, new JsonCallBack() {
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
     * 砍价列表
     */
    public void getHomeKjList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getHomeKjList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<BargainBean>>() {
                    }.getType();
                    List<BargainBean> bargainBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetBargainBeanListSuccess(bargainBeanList);
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
     * 首页获取课程
     */
    public void getTeacherCoureList(String id, String startDate, int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("startDate", startDate);
            ApiHelper.generalApi(Constant.getTeacherCoureList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<CourseTimeBean>>() {
                        }.getType();
                        List<CourseTimeBean> courseTimeBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
                        String sysdate = dff.format(new Date());
                        mView.onGetCourseTimeBeanListSuccess(courseTimeBeanList, startDate, sysdate, position);
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
     * 近期授课老师
     */
    public void getrecentlylist() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getrecentlylist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if(!TextUtils.isEmpty(response.optString("result"))){
                            JSONObject result = new  JSONObject(response.optString("result"));
                            Type collectionType = new TypeToken<Collection<CourseBean>>() {
                            }.getType();
                            List<CourseBean> courseBeanLists = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                            mView.onGetJqCourseBeanListSuccess(courseBeanLists);
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

    public <T> ArrayList<T> fromJsonList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(new Gson().fromJson(elem, cls));
        }
        return mList;
    }

    /**
     * 获取校区
     */
    public void getFicationList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getFicationList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CampusBean>>() {
                        }.getType();
                        List<CampusBean> campusBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetCampusBeanListSuccess(campusBeanList);
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
     * 获取公告列表
     *
     * @param pageNum
     */
    public void getAnnouncement(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getAnnouncement, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<AnnouncementBean>>() {
                        }.getType();
                        List<AnnouncementBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetAnnouncementBeanListSuccess(courseBeanList, pages);
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
     * 预约课程
     *
     * @param id
     */
    public void appointment(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//课程id
            obj.put("appointmentType", "0");//预约类型 0正式卡 1体验卡
            obj.put("experienceId", "0");//体验卡id
            ApiHelper.generalApi(Constant.appointment, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onAppointmentSuccess();
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

    public void appointment1(String id, String appointmentType, String experienceId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//课程id
            obj.put("appointmentType", appointmentType);//预约类型 0正式卡 1体验卡
            obj.put("experienceId", experienceId);//体验卡id
            ApiHelper.generalApi(Constant.appointment, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onAppointmentSuccess();
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
     * 获取首页直播推荐列表
     */
    public void getHomeLive() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getHomeLive, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<LiveBean>>() {
                        }.getType();
                        List<LiveBean> liveBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetLiveBeanListSuccess(liveBeanList);
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


    public void getRefreshHomeLive() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getHomeLive, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<LiveBean>>() {
                        }.getType();
                        List<LiveBean> liveBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetRefreshLiveBeanListSuccess(liveBeanList);
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
     * 去砍价
     *
     * @param id
     */
    public void saveorder(String id, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//砍价活动id
            obj.put("type", type);
            ApiHelper.generalApi(Constant.saveorder, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onSaveorderSuccess(result.optString("orderId"));
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
     * 获取个人用户信息
     */
    public void userdetail() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.userdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        UserBean bean = new Gson().fromJson(response.optString("result"), UserBean.class);
                        mView.onGetUserdetailSuccess(bean);
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


    public void getHomeVideo() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getHomeVideo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<BannerBean>>() {
                        }.getType();

                        List<BannerBean> bannerBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetVideoListSuccess(bannerBeanList);

                    } catch (Exception e) {
                        e.printStackTrace();
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
     * 公告课程列表
     *
     * @param courseId
     * @param pageNum
     */
    public void getCourinfoList(String courseId, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", courseId);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "4");
            ApiHelper.generalApi(Constant.getCourinfoList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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

    public void invitefriends() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.invitefriends, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        mView.onGetInvitefriendsSuccess(result.getString("userName"), result.getString("id"),result.optString("avatar"));
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

    public void friendconsent(String userId, int status) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", userId);
            obj.put("status", status);
            ApiHelper.generalApi(Constant.friendconsent, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {

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



}
