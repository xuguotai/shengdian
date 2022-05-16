package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftBean;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.model.TUIGiftModel;
import com.tryine.sdgq.common.mine.bean.GiftBean;
import com.tryine.sdgq.common.mine.view.LiveInView;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.ToastUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class LiveInPresenter extends BasePresenter {

    public LiveInPresenter(Context context) {
        super(context);
    }

    LiveInView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (LiveInView) view;
    }


    /**
     * 关闭直播
     */
    public void closeRoom(int liveId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("liveId", liveId);
            ApiHelper.generalApi(Constant.closeroom, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onCloseRoomSuccess();
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
     * 礼物列表
     */
    public void getiGiftList(int type) {
        try {
            JSONObject obj = new JSONObject();
            String url = "";
            if (type == 0) {
                url = Constant.getiGiftList;
            } else {
                url = Constant.getLiveGiftList;
            }
            ApiHelper.generalApi(url, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<GiftBean>>() {
                        }.getType();
                        List<GiftBean> giftBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGiftBeanListSuccess(giftBeanList);
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
     * 查询用户金豆，银豆
     */
    public void getUserbean() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getUserbean, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetUserbeanSuccess(result.optInt("goldenBean"), result.getInt("silverBean"));

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
     * 送礼物
     */
    public void sendPresent(String id, String liveId, TUIGiftModel giftModel) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//礼物id
            obj.put("num", "1");//礼物数量
            obj.put("liveId", liveId);//直播间id
            ApiHelper.generalApi(Constant.sendPresent, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onSendPresentSuccess(giftModel);
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
     * 送琴友圈礼物
     *
     * @param id
     * @param contentId
     */
    public void sendPresent(String id, String contentId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//礼物id
            obj.put("num", "1");//礼物数量
            obj.put("contentId", contentId);//内容id
            ApiHelper.generalApi(Constant.sendPresent1, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onSendPresentSuccess(null);
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

    public void getLiveroomdetail(String liveId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("liveId", liveId);//直播间id
            ApiHelper.generalApi(Constant.getLiveroomdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
//                           mView.onGetliveroomdetailSuccess(result.optString("pushAddr"));

                            mView.onGetliveroomdetailSuccess(
                                    result.optString("userId"),
                                    result.optString("teacherName"),
                                    result.optString("avatar"),
                                    result.optString("playLiveAddr"),
                                    result.optString("streamName"));
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


    public void getUsertrtcurl(String liveId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("liveId", liveId);//直播间id
            ApiHelper.generalApi(Constant.getUsertrtcurl, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject result = new JSONObject(response.optString("result"));
                            mView.onGetUsertrtcurlSuccess(
                                    result.optString("pushUrl"),
                                    result.optString("playAddr"));
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
     * 统计直播间人数
     *
     * @param liveId
     * @param countType
     */
    public void countroominfo(String liveId, int countType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("liveId", liveId);//直播间id
            obj.put("countType", countType);//统计类型 0-观看人数  1-评论数 2-点赞数
            obj.put("operationType", "0");//运算类型 0-加 1-减
            ApiHelper.generalApi(Constant.countroominfo, obj, new JsonCallBack() {
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


}
