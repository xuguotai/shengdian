package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.AnnouncementBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 公告列表list
 */
public class NoticeListAdapter extends BaseQuickAdapter<AnnouncementBean, BaseViewHolder> {

    public NoticeListAdapter(Context context, List<AnnouncementBean> data) {
        super(R.layout.item_notice_list, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, AnnouncementBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, data.getName());
        holder.setText(R.id.tv_content, data.getContent());
        holder.setText(R.id.tv_time, data.getCreateTime());

        holder.addOnClickListener(R.id.ll_root);
    }


}
