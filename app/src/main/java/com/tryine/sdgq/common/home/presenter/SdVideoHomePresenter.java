package com.tryine.sdgq.common.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.home.view.SdHomeVideoView;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class SdVideoHomePresenter extends BasePresenter {

    public SdVideoHomePresenter(Context context) {
        super(context);
    }

    SdHomeVideoView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (SdHomeVideoView) view;
    }


    /**
     * 获取视频首页类型列表
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
     * 获取热门视频
     */
    public void getVideoList() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("isRecommend", "1");
            ApiHelper.generalApi(Constant.getVideoList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModels = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetVideoListSuccess(videoModels);
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
     * 综合视频
     */
    public void getZhVideoList() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageSize", "3");
            obj.put("pageNum", "1");
            ApiHelper.generalApi(Constant.getVideoList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModels = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetZhVideoListSuccess(videoModels);
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
     * 获取所有视频
     *
     * @param typeId
     * @param pageNum
     */
    public void getVideoList(String typeId, int pageNum,int orderByType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageSize", 10);
            obj.put("pageNum", pageNum);
            if ("-1".equals(typeId)) { //付费类型 0-免费 1-付费
                obj.put("payType", "0");
            } else if ("-2".equals(typeId)) { //付费类型 0-免费 1-付费
                obj.put("payType", "1");
            } else {
                obj.put("typeId", typeId);
            }
            obj.put("orderByType", orderByType);// 排序类型 0-默认 1-最新视频 2-播放最多 3-购买最多

            ApiHelper.generalApi(Constant.getVideoList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        int pages = result.optInt("pages");
                        List<VideoModel> videoModels = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetVideoListSuccess(videoModels, pages);
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
     * 视频详情
     *
     * @param videoId
     */
    public void getVideodetail(String videoId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", videoId);
            ApiHelper.generalApi(Constant.getvideodetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModels = new Gson().fromJson(result.optJSONArray("videoVoList").toString(), collectionType);
                        VideoModel videoModel = new Gson().fromJson(result.optString("detailVo"), VideoModel.class);

                        Type collectionType1 = new TypeToken<Collection<SheetMusicBean>>() {
                        }.getType();
                        List<SheetMusicBean> sheetMusicBeans = new Gson().fromJson(result.optJSONArray("scoreVoList").toString(), collectionType1);
                        mView.onGetVideoDetailSuccess(videoModel, videoModels, sheetMusicBeans);
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
     * 解锁视频
     *
     * @param videoId
     */
    public void unlockvideo(String videoId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", videoId);
            ApiHelper.generalApi(Constant.unlockvideo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUnlockVideoSuccess();
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
            obj.put("resourceType", "1");// 收藏类型(1:视频 2:课程 3:商品 4:琴友圈)
            obj.put("type", type);//0-收藏 1-取消收藏
            ApiHelper.generalApi(Constant.setcollect, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onCollectSuccess("0".equals(type) ? "1" : "0");
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
                            mView.onFocusSuccess("0".equals(type) ? "1" : "0");
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
     * 搜索视频
     * @param title
     * @param pageNum
     */
    public void getSearchvideolist(String title, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("title", title);//商品名称
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getSearchvideolist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModelLists = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetVideoListSuccess(videoModelLists, pages);
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
     * 我的视频
     * @param pageNum
     */
    public void getSelectmyvideolist( int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getselectmyvideolist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<VideoModel>>() {
                        }.getType();
                        List<VideoModel> videoModelLists = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetVideoListSuccess(videoModelLists, pages);
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


}
