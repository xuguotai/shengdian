package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mall.bean.OrderGoodsBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 订单列表-商品list
 */
public class OrderGoodsItemAdapter extends BaseQuickAdapter<OrderGoodsBean, BaseViewHolder> {

    public OrderGoodsItemAdapter(Context context, List<OrderGoodsBean> data) {
        super(R.layout.item_order_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderGoodsBean data) {

        GlideEngine.createGlideEngine().loadImage(data.getGoodsImgUrl(), holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_goodName, data.getGoodsName());
        if(data.getBeanType() == 0){
            holder.setText(R.id.tv_price, data.getUnitPrice() + " SD金豆");
        }else{
            holder.setText(R.id.tv_price, data.getUnitPrice() + " SD银豆");
        }

        holder.setText(R.id.tv_count, "x" + data.getCount());

        holder.addOnClickListener(R.id.ll_root);
    }


}
