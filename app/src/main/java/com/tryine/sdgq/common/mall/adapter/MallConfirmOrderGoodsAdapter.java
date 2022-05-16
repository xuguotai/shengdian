package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 确认订单商品
 */
public class MallConfirmOrderGoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public MallConfirmOrderGoodsAdapter(Context context, List<GoodsBean> data) {
        super(R.layout.item_mall_confirmorder_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoodsBean data) {
        if (!TextUtils.isEmpty(data.getGoodsFaceImage())) {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getGoodsFaceImage().split("!#!")[0], holder.getView(R.id.iv_cover));
        } else {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getGoodsFaceImage(), holder.getView(R.id.iv_cover));
        }

        holder.setText(R.id.tv_title, data.getGoodsName());
        ImageView iv_price = holder.getView(R.id.iv_price);
        if (data.getBeanType() == 0) {
            iv_price.setBackgroundResource(R.mipmap.ic_jdz);
            holder.setText(R.id.tv_price, data.getPrice() + " 金豆");
        } else {
            iv_price.setBackgroundResource(R.mipmap.ic_ydz);
            holder.setText(R.id.tv_price, data.getPrice() + " 银豆");
        }



        holder.setText(R.id.tv_goodsNum, "x" + data.getQuantity());

    }


}
