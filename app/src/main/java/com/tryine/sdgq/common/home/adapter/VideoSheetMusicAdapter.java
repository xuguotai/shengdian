package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 视频详情琴谱list
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:18
 */
public class VideoSheetMusicAdapter extends BaseQuickAdapter<SheetMusicBean, BaseViewHolder> {

    public VideoSheetMusicAdapter(Context context, List<SheetMusicBean> data) {
        super(R.layout.item_video_sheetmusic, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, SheetMusicBean data) {

        if (!TextUtils.isEmpty(data.getImgUrl())) {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl().split(",")[0], holder.getView(R.id.iv_cover));
        } else {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_cover));
        }

        holder.setText(R.id.tv_title,data.getName());
        holder.addOnClickListener(R.id.ll_root);

    }


}
