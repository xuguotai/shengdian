package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class MinePresenter extends BasePresenter {

    public MinePresenter(Context context) {
        super(context);
    }

    MineView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (MineView) view;
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

    /**
     * 老师个人课程
     *
     * @param startDate
     */
    public void getTeacherList(String startDate) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("startDate", startDate);
            ApiHelper.generalApi(Constant.getTeacherList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<CourseTimeBean>>() {
                        }.getType();
                        List<CourseTimeBean> courseTimeBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetCourseBeanListSuccess(courseTimeBeanList);
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
     * 关注列表
     *
     * @param pageNum
     * @param userId
     */
    public void getFocusList(int pageNum, String userId,String selectType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("selectType", selectType);//  查看类型 0-看自己 1-看他人
            obj.put("userId", userId);// 用户id（看自己传自己的UserId、看他人传他人的UserId）
            ApiHelper.generalApi(Constant.getFocusList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<FansBean>>() {
                        }.getType();
                        List<FansBean> fansBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetFansBeanSuccess(fansBeanList, pages);
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
     * 粉丝列表
     *
     * @param pageNum
     * @param userId
     */
    public void getFansList(int pageNum, String userId,String selectType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("selectType", selectType);//  查看类型 0-看自己 1-看他人
            obj.put("userId", userId);// 用户id（看自己传自己的UserId、看他人传他人的UserId）
            ApiHelper.generalApi(Constant.getFansList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<FansBean>>() {
                        }.getType();
                        List<FansBean> fansBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetFansBeanSuccess(fansBeanList, pages);
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
     * 关注/取消关注
     *
     * @param focusUserId
     * @param type
     */
    public void setFocus(String focusUserId, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("focusUserId", focusUserId);//  用户id
            obj.put("type", type);// 操作类型 0-关注 1-取消关注
            ApiHelper.generalApi(Constant.setFocus, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onFocusSuccess();
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
     * 修改密码
     *
     * @param password
     * @param thepassword
     */
    public void updatepassword(String password, String thepassword) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("password", password);
            obj.put("thepassword", thepassword);
            ApiHelper.generalApi(Constant.updatepassword, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUpdatepasswordSuccess();
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
     * 获取验证码
     *
     * @param phone
     */
    public void getCode(String phone) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("phone", phone);
            ApiHelper.generalApi(Constant.getCode, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    mView.onCodeSuccess();
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
     * 修改手机号
     * @param mobile
     * @param phone
     * @param code
     */
    public void updatemobile(String mobile, String phone, String code) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("mobile", mobile);
            obj.put("phone", phone);
            obj.put("code", code);
            ApiHelper.generalApi(Constant.updatemobile, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUpdatepasswordSuccess();
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
                            mView.onGetIsLiveSuccess(obj.optInt("isLive"), obj.optInt("realStatus"), obj.optInt("liveId"),obj.optString("trtcPushAddr"));
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

    public void getaboutinfo() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getaboutinfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject obj = new JSONObject(response.optString("result"));
                            mView.onGetaboutinfoSuccess(obj.optString("rewardDesc"));
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
