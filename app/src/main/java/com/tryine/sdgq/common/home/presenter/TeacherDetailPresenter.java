package com.tryine.sdgq.common.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CommentBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.common.home.view.TeacherDetailView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;

public class TeacherDetailPresenter extends BasePresenter {

    public TeacherDetailPresenter(Context context) {
        super(context);
    }

    TeacherDetailView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (TeacherDetailView) view;
    }

    /**
     * 评价列表
     */
    public void getCommentlist(int pageNum, String teacherId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("teacherId", teacherId);
            obj.put("pageSize", "10");
            obj.put("pageNum", pageNum);
            ApiHelper.generalApi(Constant.getCommentlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CommentBean>>() {
                        }.getType();
                        List<CommentBean> commentBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCommentSuccess(commentBeanList, pages);
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
     * 获取老师信息
     *
     * @param teacherId
     */
    public void getTeacherdetail(String teacherId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("teacherId", teacherId);
            ApiHelper.generalApi(Constant.getteacherdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        try {
                            JSONObject result = new JSONObject(response.optString("result"));
                            Type collectionType = new TypeToken<Collection<TeacherBean>>() {
                            }.getType();
                            List<TeacherBean> commentBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                            if (commentBeanList.size() > 0) {
                                mView.onGetTeacherBeanSuccess(commentBeanList.get(0));
                            } else {
                                mView.onGetTeacherBeanSuccess();
                            }

                        } catch (Exception e) {
                            e.getMessage();
                        }
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
     * 首页获取课程
     */
    public void getTeacherCoureList(String id, String startDate, String teacherId, int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("startDate", startDate);
            obj.put("teacherId", teacherId);
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
                        mView.onGetCourseBeanListSuccess(courseTimeBeanList, startDate, sysdate, position);
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


}
