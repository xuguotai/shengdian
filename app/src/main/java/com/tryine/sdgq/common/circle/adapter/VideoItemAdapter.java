package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 首页琴友圈-发现列表
 */
public class VideoItemAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {

    public VideoItemAdapter(Context context, List<VideoModel> data) {
        super(R.layout.item_per_circle, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoModel data) {
        ImageView iv_play = holder.getView(R.id.iv_play);
        iv_play.setVisibility(View.VISIBLE);
        GlideEngine.createGlideEngine().loadImage(mContext, data.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        TextView tv_zy = holder.getView(R.id.tv_zy);
        if (null != data.getVideoType() && data.getVideoType().equals("1")) {
            tv_zy.setVisibility(View.VISIBLE);
        } else {
            tv_zy.setVisibility(View.GONE);
        }

        holder.setText(R.id.tv_title, data.title);
//        holder.setText(R.id.tv_giveCount, data.getGiveCount());

        holder.addOnClickListener(R.id.ll_root);
    }


}
