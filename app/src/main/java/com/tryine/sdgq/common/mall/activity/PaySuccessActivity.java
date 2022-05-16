package com.tryine.sdgq.common.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.ActivityManager;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mall.bean.GoodsBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付成功
 *
 * @author: zhangshuaijun
 * @time: 2021-12-03 14:47
 */
public class PaySuccessActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_orderNo)
    TextView tv_orderNo;
    @BindView(R.id.ll_item)
    LinearLayout ll_item;

    List<GoodsBean> goodsBeanList = new ArrayList<>();
    String orderNo;

    public static void start(Context context, List<GoodsBean> goodsBeanList, String orderNo) {
        Intent intent = new Intent();
        intent.setClass(context, PaySuccessActivity.class);
        intent.putExtra("goodsBeanList", (Serializable) goodsBeanList);
        intent.putExtra("orderNo", orderNo);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_paysuccess;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("支付成功");
        orderNo = getIntent().getStringExtra("orderNo");
        goodsBeanList = (List<GoodsBean>) getIntent().getSerializableExtra("goodsBeanList");

        initViews();
    }

    private void initViews() {
        tv_orderNo.setText("订单编号： " + orderNo);
        for (GoodsBean goodsBean : goodsBeanList) {
            if (goodsBean.isChecked()) {
                View view = View.inflate(mContext, R.layout.item_paygood, null);
                TextView tv_goodName = view.findViewById(R.id.tv_goodName);
                TextView tv_num = view.findViewById(R.id.tv_num);
                tv_goodName.setText("商品信息：" + goodsBean.getGoodsName());
                tv_num.setText("购买数量：" + "x" + goodsBean.getQuantity());
                ll_item.addView(view);
            }
        }

    }


    @OnClick({R.id.iv_black, R.id.tv_sc, R.id.tv_dd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_sc:
                ActivityManager.getScreenManager().popActivity(MallTypeListActivity.class);
                ActivityManager.getScreenManager().popActivity(GoodsDetailActivity.class);
                ActivityManager.getScreenManager().popActivity(MallCartActivity.class);
                finish();
                break;
            case R.id.tv_dd:
                ActivityManager.getScreenManager().popActivity(MallTypeListActivity.class);
                ActivityManager.getScreenManager().popActivity(GoodsDetailActivity.class);
                ActivityManager.getScreenManager().popActivity(MallCartActivity.class);
                OrderListHomeActivity.start(mContext,0);
                finish();
                break;
        }
    }


}
