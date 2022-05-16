package com.tryine.sdgq.common.mall.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.bean.GoodsCommentBean;
import com.tryine.sdgq.common.mall.view.GoodsDetailView;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class GoodsDetailPresenter extends BasePresenter {

    public GoodsDetailPresenter(Context context) {
        super(context);
    }

    GoodsDetailView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (GoodsDetailView) view;
    }

    /**
     * 商品详情
     */
    public void getGoodsDetail(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.getGoodsDetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    JSONObject data = response.optJSONObject("result");
                    GoodsBean bean = new Gson().fromJson(data.toString(), GoodsBean.class);
                    mView.onGetGoodsDetailSuccess(bean);
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
     * 添加购物车
     * @param goodsId
     */
    public void addCar(String goodsId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("goodsId", goodsId);
            obj.put("goodsCount", "1");
            ApiHelper.generalApi(Constant.addCar, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onaddCarSuccess();
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


    public void setCollect(String id, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("resourcesId", id);//点赞资源id
            obj.put("resourceType", "3");// 收藏类型(1:视频 2:课程 3:商品 4:琴友圈)
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

    public void getcommentslist(String goodsId, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("goodsId", goodsId);//分类id
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getcommentslist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsCommentBean>>() {
                        }.getType();
                        List<GoodsCommentBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetGoodsCommentListSuccess(goodsBeanList, pages);
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
