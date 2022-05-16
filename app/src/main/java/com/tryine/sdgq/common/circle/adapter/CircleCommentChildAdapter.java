package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.CircleCommentBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 圈子详情评价列表
 */
public class CircleCommentChildAdapter extends BaseQuickAdapter<CircleCommentBean, BaseViewHolder> {


    public CircleCommentChildAdapter(Context context, List<CircleCommentBean> data) {
        super(R.layout.item_circle_child_comment, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleCommentBean data) {
        if (!TextUtils.isEmpty(data.getReplyUserName())) {
            holder.setText(R.id.tv_name, data.getUserName() + " 回复 " + data.getReplyUserName());
        } else {
            holder.setText(R.id.tv_name, data.getUserName());
        }

        holder.setText(R.id.tv_content, data.getContent());
        holder.setText(R.id.tv_time, data.getCreateTimeStr());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(), (ImageView) holder.getView(R.id.iv_head));

        holder.addOnClickListener(R.id.tv_zkpl);
        holder.addOnClickListener(R.id.iv_head);
        holder.addOnClickListener(R.id.ll_root);
    }


}
