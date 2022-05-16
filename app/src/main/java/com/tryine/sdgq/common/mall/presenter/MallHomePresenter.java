package com.tryine.sdgq.common.mall.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.view.MallHomeView;
import com.tryine.sdgq.common.mine.view.UploadVideoView;
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

public class MallHomePresenter extends BasePresenter {

    public MallHomePresenter(Context context) {
        super(context);
    }

    MallHomeView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (MallHomeView) view;
    }

    /**
     * 获取视频首页类型列表
     */
    public void getGoodsTypeList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getGoodsTypeList, obj, new JsonCallBack() {
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
     * 商品列表
     */
    public void getGoodsList(String name, String catsId, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", name);//商品名称
            obj.put("catsId", catsId);//分类id
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getGoodsList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetGoodsBeanListSuccess(goodsBeanList, pages);
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
     * 新品专区商品列表
     *
     * @param pageNum
     */
    public void getproductlist(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getproductlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetGoodsBeanListSuccess(goodsBeanList, pages);
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
     * 热销商品
     *
     * @param pageNum
     */
    public void getcakeslist(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getcakeslist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetGoodsBeanListSuccess(goodsBeanList, pages);
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
     * 特惠商品
     *
     * @param pageNum
     */
    public void getpreferentiallist(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getpreferentiallist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetGoodsBeanListSuccess(goodsBeanList, pages);
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


    public void selectgoodslist(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.selectgoodslist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetGoodsBeanListSuccess(goodsBeanList, pages);
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
     * 查询限量商品
     */
    public void selectlimitgoodslist() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.selectlimitgoodslist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetGoodsBeanListSuccess(goodsBeanList);
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
