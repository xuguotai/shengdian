package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 琴谱详情-短视频
 */
public class SheetMusicVideoAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {

    public SheetMusicVideoAdapter(Context context, List<VideoModel> videoModels) {
        super(R.layout.item_sheetmusic_video, videoModels);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoModel videoModel) {

        GlideEngine.createGlideEngine().loadImage(mContext, videoModel.getCoverUrl(), holder.getView(R.id.iv_cover));

        holder.setText(R.id.tv_time, videoModel.getVideoTimeStr());
        holder.setText(R.id.tv_look, videoModel.getPlayCount());
        holder.setText(R.id.tv_title, videoModel.title);

        holder.addOnClickListener(R.id.ll_root);

    }


}
