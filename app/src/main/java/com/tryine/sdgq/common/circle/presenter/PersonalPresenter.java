package com.tryine.sdgq.common.circle.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.view.PersonalView;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;


public class PersonalPresenter extends BasePresenter {
    public PersonalPresenter(Context context) {
        super(context);
    }

    PersonalView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (PersonalView) view;
    }


    /**
     * 个人主页
     *
     * @param selectType
     * @param userId
     */
    public void getUserHomeInfo(String selectType, String userId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("selectType", selectType);//  查看类型 0-看自己 1-看他人
            obj.put("userId", userId);//   用户id（看自己传自己的UserId、看他人传他人的UserId）
            ApiHelper.generalApi(Constant.getUserHomeInfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject data = response.optJSONObject("result");
                        PersonalBean bean = new Gson().fromJson(data.toString(), PersonalBean.class);
                        mView.onGetPersonaBeanSuccess(bean);
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
     * 设置-编辑用户信息
     *
     * @param userExplain
     * @param isShowLabel
     * @param isReceive
     */
    public void updateUserInfo(String userExplain, String isShowLabel, String isReceive) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("userExplain", userExplain);// 个人简介
            obj.put("isShowLabel", isShowLabel);//    是否显示兴趣标签 0-否 1-是
            obj.put("isReceive", isReceive);//    是否接收未关注人私信 0-否 1-是
            ApiHelper.generalApi(Constant.updateUserInfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUpdateUserInfoSuccess();
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
     * 用户标签列表
     */
    public void getLabelList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getLabelList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<LabelBean>>() {
                    }.getType();
                    List<LabelBean> bannerBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetLabelListSuccess(bannerBeanList);
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
     * 添加标签
     *
     * @param name
     */
    public void addLabel(String name) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            ApiHelper.generalApi(Constant.addLabel, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onUpdateLabelSuccess();
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
     * 添加标签
     */
    public void deleteLabel(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.deleteLabel, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onUpdateLabelSuccess();
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
     * 主页（琴友圈内容列表）
     * @param pageNum
     * @param selectType
     * @param userId
     */
    public void getPersonalCircleList(int pageNum, String selectType, String userId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("selectType", selectType); //查看类型 0-看自己 1-看他人
            obj.put("userId", userId);//用户id（看自己传自己的UserId、看他人传他人的UserId）
            ApiHelper.generalApi(Constant.getPersonalCircleList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CircleBean>>() {
                        }.getType();
                        List<CircleBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCircleBeanListSuccess(goodsBeanList, pages);
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
     * 视频列表
     * @param pageNum
     * @param selectType
     * @param userId
     */
    public void getPersonalVideoList(int pageNum, String selectType, String userId,int orderByType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("selectType", selectType); //查看类型 0-看自己 1-看他人
            obj.put("userId", userId);//用户id（看自己传自己的UserId、看他人传他人的UserId）
            obj.put("orderByType", orderByType);//排序类型 0-默认 1-最新视频 2-播放最多 3-购买最多
            ApiHelper.generalApi(Constant.getPersonalVideoList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModelList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetVideoBeanListSuccess(videoModelList, pages);
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
     * 话题
     * @param pageNum
     * @param selectType
     * @param userId
     */
    public void getPersonalTopicList(int pageNum, String selectType, String userId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("selectType", selectType);
            obj.put("userId", userId);
            ApiHelper.generalApi(Constant.getPersonalTopicList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<TopicBean>>() {
                        }.getType();
                        List<TopicBean> topicBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetTopicBeanListSuccess(topicBeanList, pages);
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
                        if ("200".equals(response.optString("code"))){
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


    public void deletepyq(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.deletepyq, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))){
                            mView.onDeletePyqSuccess();
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
     * 点赞
     *
     * @param id
     * @param type
     */
    public void setGive(String id, String type,int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("resourceId", id);//点赞资源id
            obj.put("resourceType", "1");//(0:短视频 1:琴友圈)
            obj.put("type", type);// 0-点赞 1-取消点赞
            ApiHelper.generalApi(Constant.setGive, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onGiveSuccess(type.equals("0") ? "1" : "0",position);
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
