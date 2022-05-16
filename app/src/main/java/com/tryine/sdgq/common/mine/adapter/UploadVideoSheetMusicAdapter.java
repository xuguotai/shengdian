package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;

import java.util.List;

/**
 * 上传视频关联琴谱
 */
public class UploadVideoSheetMusicAdapter extends BaseQuickAdapter<SheetMusicBean, BaseViewHolder> {

    public UploadVideoSheetMusicAdapter(Context context, List<SheetMusicBean> data) {
        super(R.layout.item_uploadvideo_sheetmusic, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, SheetMusicBean data) {
        if (null != data.getName() && !data.getName().equals("")) {
            holder.setText(R.id.tv_sheetmusic, data.getName());
        } else {
            holder.setText(R.id.tv_sheetmusic, "");
        }

        holder.addOnClickListener(R.id.iv_sheetmusic);
        holder.addOnClickListener(R.id.iv_close);
    }


}
