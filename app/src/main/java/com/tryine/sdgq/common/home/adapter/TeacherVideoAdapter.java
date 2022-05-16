package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 教师详情-视频列表
 */
public class TeacherVideoAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {

    public TeacherVideoAdapter(Context context, List<VideoModel> data) {
        super(R.layout.item_teacher_video, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoModel videoModel) {
        GlideEngine.createGlideEngine().loadImage(mContext, videoModel.getCoverUrl()
                , holder.getView(R.id.iv_cover));

        holder.setText(R.id.tv_title, videoModel.title);
        holder.setText(R.id.tv_time, videoModel.getVideoTimeStr());
        holder.setText(R.id.tv_look, videoModel.getPlayCount());
        TextView tv_zy = holder.getView(R.id.tv_zy);
        if (null != videoModel.getVideoType() && videoModel.getVideoType().equals("1")) {
            tv_zy.setVisibility(View.VISIBLE);
        } else {
            tv_zy.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.ll_root);
    }



}
