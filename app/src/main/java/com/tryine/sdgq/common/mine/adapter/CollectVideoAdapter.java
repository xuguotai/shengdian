package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 收藏视频list
 */
public class CollectVideoAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {

    public CollectVideoAdapter(Context context, List<VideoModel> data) {
        super(R.layout.item_collect_video, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoModel data) {

        GlideEngine.createGlideEngine().loadImage(mContext, data.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, data.title);
        holder.setText(R.id.tv_price, data.getGoldenBean());

        holder.addOnClickListener(R.id.ll_root);
        holder.addOnClickListener(R.id.tv_sc);
    }


}
