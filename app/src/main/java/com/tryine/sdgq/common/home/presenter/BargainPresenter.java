package com.tryine.sdgq.common.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.common.home.view.HomeView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class BargainPresenter extends BasePresenter {

    public BargainPresenter(Context context) {
        super(context);
    }

    BargainView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (BargainView) view;
    }


    /**
     * 校区列表
     */
    public void getBargainCampusList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getBargainCampusList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<CampusBean>>() {
                        }.getType();
                        List<CampusBean> campusBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetCampusBeanSuccess(campusBeanList);
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
     * 砍价专区
     */
    public void getBargainAreaList(int pageNum, String campusId, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("campusId", campusId);//校区id
            obj.put("type", type);//  类型 0:直播课 1:线下课程
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getBargainAreaList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<BargainBean>>() {
                        }.getType();
                        List<BargainBean> bargainBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetBargainBeanListSuccess(bargainBeanList, pages);
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
    public void saveorder(String id,String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//砍价活动id
            obj.put("type", type);//课程类型
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
     * 砍价订单-订单详情
     *
     * @param id
     */
    public void getBargainOrderdetail(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//订单id
            ApiHelper.generalApi(Constant.getBargainOrderdetail, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            JSONObject data = response.optJSONObject("result");
                            BargainOrderDetailBean bean = new Gson().fromJson(response.optString("result"), BargainOrderDetailBean.class);
                            mView.onGetBargainOrderDetailBeanSuccess(bean);
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
     * 砍价订单-去付款(立即购买)
     * @param id
     */
    public void goBargainBuy(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//订单id
            ApiHelper.generalApi(Constant.goBargainBuy, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onBargainBuySuccess();
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
     * 砍价订单-订单列表
     * @param pageNum
     * @param orderStatus
     */
    public void getBargainOrderlist(int pageNum, String orderStatus,String bargainName) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderStatus", orderStatus);//订单状态 0-全部 1-砍价中 2-待付款 3-待核销 4-已完成
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("bargainName", bargainName);
            ApiHelper.generalApi(Constant.getBargainOrderlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<BargainBean>>() {
                        }.getType();
                        List<BargainBean> bargainBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetBargainBeanListSuccess(bargainBeanList, pages);
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
     * 邀请砍价-获取H5页面砍价信息
     * @param id
     */
    public void getbargainh5info(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.getbargainh5info, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            BargainOrderDetailBean bean = new Gson().fromJson(response.optString("result"), BargainOrderDetailBean.class);
                            mView.onGetBargainOrderDetailBeanSuccess(bean);
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

    public void helpbargain(String orderId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);
            ApiHelper.generalApi(Constant.helpbargain, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onBargainBuySuccess();

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
