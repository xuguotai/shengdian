package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mall.bean.OrderGoodsBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 订单详情-商品list
 */
public class OrderDetailGoodsItemAdapter extends BaseQuickAdapter<OrderGoodsBean, BaseViewHolder> {

    public OrderDetailGoodsItemAdapter(Context context, List<OrderGoodsBean> data) {
        super(R.layout.item_orderdetail_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderGoodsBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext,data.getGoodsImgUrl(), holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_goodName, data.getGoodsName());
        if (data.getBeanType() == 0) {
            holder.setText(R.id.tv_price, data.getUnitPrice() + " 金豆");
        } else {
            holder.setText(R.id.tv_price, data.getUnitPrice() + " 银豆");
        }
        TextView tv_status = holder.getView(R.id.tv_status);
        if(data.getStatus().equals("7")){
            tv_status.setVisibility(View.VISIBLE);
            tv_status.setText(data.getStatusVal());
        }else{
            tv_status.setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_count, "x" + data.getCount());


    }



}
