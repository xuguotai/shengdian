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
 * 商城首页商品
 */
public class MallGoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public MallGoodsAdapter(Context context, List<GoodsBean> data) {
        super(R.layout.item_mall_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoodsBean data) {

        if (!TextUtils.isEmpty(data.getImgUrl())) {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl().split("!#!")[0], holder.getView(R.id.iv_cover));
        } else {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_cover));
        }

        holder.setText(R.id.tv_goodName, data.getName());
        ImageView iv_price = holder.getView(R.id.iv_price);
        if (data.getBeanType() == 0) {
            iv_price.setBackgroundResource(R.mipmap.ic_jdz);
            holder.setText(R.id.tv_price, data.getMarketPrice() + " 金豆");
        } else {
            iv_price.setBackgroundResource(R.mipmap.ic_ydz);
            holder.setText(R.id.tv_price, data.getMarketPrice() + " 银豆");
        }

        holder.addOnClickListener(R.id.ll_root);
    }



}
