package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.mall.adapter.CartAdapter;
import com.tryine.sdgq.common.mall.adapter.MallConfirmOrderGoodsAdapter;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.presenter.CartPresenter;
import com.tryine.sdgq.common.mall.view.CartView;
import com.tryine.sdgq.common.user.activity.PayPasswordActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.VerifyPayPwdDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商城确认订单
 *
 * @author: zhangshuaijun
 * @time: 2021-11-29 15:01
 */
public class MallConfirmOrderActivity extends BaseActivity implements CartView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_addrDes)
    TextView tv_addrDes;
    @BindView(R.id.tv_distance)
    TextView tv_distance;
    @BindView(R.id.tv_total_price)
    TextView tv_total_price;
    @BindView(R.id.tv_total_price1)
    TextView tv_total_price1;
    @BindView(R.id.et_addrDes)
    EditText et_addrDes;

    List<CampusBean> campusBeanLists = new ArrayList<>(); //校区
    CampusBean selectCampusBean;//选中的校区

    List<GoodsBean> goodsBeanList = new ArrayList<>();
    @BindView(R.id.rv_data)
    RecyclerView rv_data;
    MallConfirmOrderGoodsAdapter mallConfirmOrderGoodsAdapter;

    CartPresenter cartPresenter;

    String orderNo;

    UserBean userBean;

    int isInput;//是否显示地址输入框

    public static void start(Context context, List<GoodsBean> goodsBeanList) {
        Intent intent = new Intent();
        intent.setClass(context, MallConfirmOrderActivity.class);
        intent.putExtra("goodsBeanList", (Serializable) goodsBeanList);
        context.startActivity(intent);
    }

    public static void start(Context context, List<GoodsBean> goodsBeanList, int isInput) {
        Intent intent = new Intent();
        intent.setClass(context, MallConfirmOrderActivity.class);
        intent.putExtra("goodsBeanList", (Serializable) goodsBeanList);
        intent.putExtra("isInput", isInput);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mall_confirmorder;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("结算");
        goodsBeanList = (List<GoodsBean>) getIntent().getSerializableExtra("goodsBeanList");
        isInput = getIntent().getIntExtra("isInput", 0);
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        cartPresenter = new CartPresenter(this);
        cartPresenter.attachView(this);
        cartPresenter.getFicationList(1, SPUtils.getConfigString(Parameter.LOCATION, ""), "");

        mallConfirmOrderGoodsAdapter = new MallConfirmOrderGoodsAdapter(this, goodsBeanList);
        LinearLayoutManager lin = new LinearLayoutManager(this);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rv_data.setLayoutManager(lin);
        rv_data.setAdapter(mallConfirmOrderGoodsAdapter);
        calculatePrice();

        if(isInput == 1){
            et_addrDes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
    }

    @OnClick({R.id.iv_black, R.id.tv_xq, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_xq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(MallConfirmOrderActivity.this, campusBeanLists);
                    campusDialog.show();
                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(CampusBean homeMenuBean) {
                            selectCampusBean = homeMenuBean;
                            tv_name.setText(selectCampusBean.getName());
                            tv_addrDes.setText(selectCampusBean.getAddrDes());
                            tv_distance.setText(selectCampusBean.getDistance() + "km");
                        }
                    });
                }
                break;
            case R.id.tv_submit:
                if (null != goodsBeanList) {
                    try {
                        JSONArray traveShopGoodsOrederDtoList = new JSONArray();
                        for (int j = 0; j < goodsBeanList.size(); j++) {
                            GoodsBean goodsBean = goodsBeanList.get(j);
                            JSONObject obj = new JSONObject();
                            obj.put("goodsNum", goodsBean.getQuantity() + "");
                            obj.put("price", goodsBean.getPrice() + "");
                            obj.put("goodsId", goodsBean.getGoodsId());
                            traveShopGoodsOrederDtoList.put(obj);
                        }
                        if (TextUtils.isEmpty(et_addrDes.getText().toString())) {
                            cartPresenter.createOrder(selectCampusBean.getId(), selectCampusBean.getName(), goodsBeanList.size() + "", traveShopGoodsOrederDtoList, et_addrDes.getText().toString());
                        } else {
                            cartPresenter.createOrder(selectCampusBean.getId(), selectCampusBean.getName(), goodsBeanList.size() + "", traveShopGoodsOrederDtoList, et_addrDes.getText().toString());
                        }


                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                break;
        }
    }

    private void calculatePrice() {

        double total_price = 0;
        double total_price1 = 0;

        for (GoodsBean goodsBean : goodsBeanList) {
            if (goodsBean.isChecked()) {
                if (goodsBean.getBeanType() == 0) {
                    total_price = total_price + (Double.parseDouble(goodsBean.getPrice()) * goodsBean.getQuantity());
                } else {
                    total_price1 = total_price1 + (Double.parseDouble(goodsBean.getPrice()) * goodsBean.getQuantity());
                }

            }


        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (total_price > 0 && total_price1 > 0) {
            tv_total_price.setVisibility(View.VISIBLE);
            tv_total_price1.setVisibility(View.VISIBLE);
            tv_total_price.setText(decimalFormat.format(total_price) + " 金豆 + ");
            tv_total_price1.setText(decimalFormat.format(total_price1) + " 银豆");
        } else if (total_price > 0) {
            tv_total_price.setVisibility(View.VISIBLE);
            tv_total_price1.setVisibility(View.GONE);
            tv_total_price.setText(decimalFormat.format(total_price) + " 金豆");
        } else if (total_price1 > 0) {
            tv_total_price.setVisibility(View.GONE);
            tv_total_price1.setVisibility(View.VISIBLE);
            tv_total_price1.setText(decimalFormat.format(total_price1) + " 银豆");
        }

    }


    @Override
    public void onGetCartListSuccess(List<GoodsBean> goodsBeanList, int pages) {

    }

    @Override
    public void onUpdateCarSuccess(int quantity, int position) {

    }

    @Override
    public void onGoodsCarDelSuccess() {

    }

    @Override
    public void onCreateOrderSuccess(String orderNo, String totalPrice) {
        this.orderNo = orderNo;
        showPromptDialog();
    }

    @Override
    public void onPayOrderSuccess() {
        ToastUtil.toastLongMessage("支付成功");
        PaySuccessActivity.start(mContext, goodsBeanList, orderNo);
        finish();
        cartDelete();
    }


    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList, int pages) {
        this.campusBeanLists.clear();
        this.campusBeanLists.addAll(campusBeanList);
        //默认取第一个校区
        if (null != campusBeanLists && campusBeanLists.size() > 0) {
            selectCampusBean = campusBeanLists.get(0);
            tv_name.setText(selectCampusBean.getName());
            tv_addrDes.setText(selectCampusBean.getAddrDes());
            tv_distance.setText(selectCampusBean.getDistance() + "km");
        }

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    private void showPromptDialog() {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                "是否现在购买选择的商品", "确定", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                if (TextUtils.isEmpty(userBean.getPayPassword())) {
                    ToastUtil.toastLongMessage("请先设置支付密码");
                    PayPasswordActivity.start(mContext);
                    return;
                }

                VerifyPayPwdDialog verifyPayPwdDialog = new VerifyPayPwdDialog(mContext, R.style.dialog_center);
                verifyPayPwdDialog.show();
                verifyPayPwdDialog.setmOnTextSendListener(new VerifyPayPwdDialog.OnTextSendListener() {
                    @Override
                    public void onbackPwd(String password) {
                        cartPresenter.payOrder(orderNo, password);
                    }

                    @Override
                    public void dismiss() {

                    }
                });
            }

            @Override
            public void cancel() {

            }
        });
    }


    /**
     * 下单之后清除购车中的商品
     */
    private void cartDelete() {
        JSONArray carIdArray = new JSONArray();
        for (int j = 0; j < goodsBeanList.size(); j++) {
            GoodsBean goodsBean = goodsBeanList.get(j);
            carIdArray.put(goodsBean.getId());
        }

        if (carIdArray.length() > 0) {
            cartPresenter.goodsCarDel(carIdArray);
        }
    }

}
