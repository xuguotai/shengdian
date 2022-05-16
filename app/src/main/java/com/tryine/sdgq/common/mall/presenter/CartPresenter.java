package com.tryine.sdgq.common.mall.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.view.CartView;
import com.tryine.sdgq.common.mall.view.GoodsDetailView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class CartPresenter extends BasePresenter {

    public CartPresenter(Context context) {
        super(context);
    }

    CartView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (CartView) view;
    }


    /**
     * 购物车列表
     */
    public void getCarGoodsList(int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "100");
            ApiHelper.generalApi(Constant.getCarGoodsList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<GoodsBean>>() {
                        }.getType();
                        List<GoodsBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCartListSuccess(goodsBeanList, pages);
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
     * 修改购物车
     *
     * @param id
     * @param quantity
     */
    public void updateCar(String id, int quantity, int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("quantity", quantity);
            ApiHelper.generalApi(Constant.updateCar, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onUpdateCarSuccess(quantity, position);
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
     * 删除购物车
     * @param carIdArray
     */
    public void goodsCarDel(JSONArray carIdArray) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("carIds", carIdArray);
            ApiHelper.generalApi(Constant.goodsCarDel, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onGoodsCarDelSuccess();
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
    public void getFicationList(int pageNum, String lonLat, String city) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("lonLat", lonLat);
//            obj.put("city", city);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getFicationList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CampusBean>>() {
                        }.getType();
                        List<CampusBean> campusBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCampusBeanListSuccess(campusBeanList, pages);
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
     * 创建订单
     * @param receiptId
     * @param pickAddress
     * @param totalNum
     * @param traveShopGoodsOrederDtoList
     */
    public void createOrder(String receiptId, String pickAddress, String totalNum,JSONArray traveShopGoodsOrederDtoList,String receiptAddress) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("receiptId", receiptId);// 地址id（校区id）
            obj.put("pickAddress", pickAddress);//详细地址
            obj.put("deliveryType", "1");//   配送方式：0:快递配送 1:自提 2:同城送货
            obj.put("totalNum", totalNum);//订单总件数
            obj.put("traveShopGoodsOrederDtoList", traveShopGoodsOrederDtoList);
            obj.put("receiptAddress", receiptAddress);//收货人地址

            // isCourse  否已是学员 0->否 1->是
            // isInput 是否显示输入 0-否 1-是
            // receiptAddress    收货人地址

            ApiHelper.generalApi(Constant.createOrder, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            JSONObject obj = new JSONObject(response.optString("result"));
                            mView.onCreateOrderSuccess(obj.optString("orderNo"),obj.optString("totalPrice"));
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
     * 支付
     * @param orderNo
     */
    public void payOrder(String orderNo,String payPassword) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("orderNo", orderNo);
            obj.put("payPassword", payPassword);
            ApiHelper.generalApi(Constant.payOrder, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if("200".equals(response.optString("code"))){
                            mView.onPayOrderSuccess();
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
