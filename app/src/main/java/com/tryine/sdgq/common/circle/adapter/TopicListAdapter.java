package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 首页琴友圈-话题列表
 */
public class TopicListAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {

    public TopicListAdapter(Context context, List<TopicBean> data) {
        super(R.layout.item_topic, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TopicBean data) {
        holder.setText(R.id.tv_title, data.getName());
        holder.setText(R.id.tv_topicDesc, data.getTopicDesc());
        holder.setText(R.id.tv_partakeCount, data.getPartakeCount() + " 人参与话题讨论/发布");
        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_cover));
        holder.addOnClickListener(R.id.ll_root);
    }


}
