package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mine.bean.MessageBean;
import com.tryine.sdgq.common.mine.view.CollectView;
import com.tryine.sdgq.common.mine.view.MessageView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class MessagePresenter extends BasePresenter {

    public MessagePresenter(Context context) {
        super(context);
    }

    MessageView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (MessageView) view;
    }

    /**
     * 消息列表
     * @param pageNum
     * @param resourcesType
     */
    public void getMessageList(int pageNum,int resourcesType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("resourcesType", resourcesType);//0-系统消息 1-点赞消息 2-分享消息 3-评论消息 4-打赏消息'
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getMessageList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<MessageBean>>() {
                        }.getType();
                        List<MessageBean> messageBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetMessageBeanListSuccess(messageBeans, pages);
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
     * 消息数量
     */
    public void getMessagenumber() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getMessagenumber, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetMessagenumberSuccess(
                                    result.getInt("sysSum"),
                                    result.getInt("thumbSum"),
                                    result.getInt("shareSum"),
                                    result.getInt("commentsSum"),
                                    result.getInt("exceptionalSum"));
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
     * 一键已读
     */
    public void messageread() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.messageread, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            mView.onAllReadSuccess();
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

    public void updateMessage(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id",id);
            ApiHelper.generalApi(Constant.updateMessage, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            mView.onAllReadSuccess();
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
