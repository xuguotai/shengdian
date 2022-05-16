package com.tryine.sdgq.common.circle.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.CircleCommentBean;
import com.tryine.sdgq.common.circle.bean.CircleDetailBean;
import com.tryine.sdgq.common.circle.view.CircleDetailView;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CircleDetailPresenter extends BasePresenter {
    public CircleDetailPresenter(Context context) {
        super(context);
    }

    CircleDetailView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (CircleDetailView) view;
    }

    /**
     * 圈子详情
     *
     * @param id
     */
    public void getCircleDetail(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//内容id
            ApiHelper.generalApi(Constant.getCircleDetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    JSONObject data = response.optJSONObject("result");
                    CircleDetailBean bean = new Gson().fromJson(data.toString(), CircleDetailBean.class);
                    mView.onGetCircleDetailSuccess(bean);
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
     * 获取琴友圈详情评论列表
     *
     * @param id
     */
    public void getCirclecommentlist(String id, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//内容id
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getCirclecommentlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CircleCommentBean>>() {
                        }.getType();
                        List<CircleCommentBean> circleCommentBeanLists = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCircleCommentBeanListSuccess(circleCommentBeanLists, pages);
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
     * 发布评论
     *
     * @param id
     * @param content
     */
    public void setComment(String id, String content) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//内容id
            obj.put("content", content);
            ApiHelper.generalApi(Constant.setComment, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onCommentSuccess();
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
    public void setGive(String id, String type) {
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
                            mView.onGiveSuccess();
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
     * 收藏
     *
     * @param id
     * @param type
     */
    public void setCollect(String id, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("resourcesId", id);//点赞资源id
            obj.put("resourceType", "4");// 收藏类型(1:视频 2:课程 3:商品 4:琴友圈)
            obj.put("type", type);//0-收藏 1-取消收藏
            ApiHelper.generalApi(Constant.setcollect, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onGiveSuccess();
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
     * 琴友圈回复评论
     *
     * @param replyCommmentId
     * @param content
     */
    public void replycomment(String replyCommmentId, String content,String replyType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("replyCommmentId", replyCommmentId);//  回复的评论id
            obj.put("content", content);// 回复内容
            obj.put("replyType", replyType);// 回复类型 0-回复一级评论 1-回复二级评论
            ApiHelper.generalApi(Constant.replycomment, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onReplycommentSuccess();
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
     * 获取琴友圈详情二级评论列表
     *
     * @param id
     */
    public void getSelecttwocommentlist(String id, int pageNum, int selectedPosition) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//评论id
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "5");
            ApiHelper.generalApi(Constant.getSelecttwocommentlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CircleCommentBean>>() {
                        }.getType();
                        List<CircleCommentBean> circleCommentBeanLists = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCircleCommentBeanChildListSuccess(circleCommentBeanLists, pageNum, pages, selectedPosition);
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


    public void deletecomment(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//评论id
            ApiHelper.generalApi(Constant.deletecomment, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onReplycommentSuccess();
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
