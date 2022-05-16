package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 收藏视频list
 */
public class CollectgoodAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public CollectgoodAdapter(Context context, List<GoodsBean> data) {
        super(R.layout.item_collect_video, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoodsBean data) {
        if (!TextUtils.isEmpty(data.getImgUrl())) {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl().split("!#!")[0], holder.getView(R.id.iv_cover));
        } else {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_cover));
        }

        holder.setText(R.id.tv_title, data.getName());
        holder.setText(R.id.tv_price, data.getMarketPrice());

        holder.addOnClickListener(R.id.ll_root);
        holder.addOnClickListener(R.id.tv_sc);
    }


}
