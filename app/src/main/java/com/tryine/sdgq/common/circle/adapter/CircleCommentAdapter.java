package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
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
public class CircleCommentAdapter extends BaseQuickAdapter<CircleCommentBean, BaseViewHolder> {


    public CircleCommentAdapter(Context context, List<CircleCommentBean> data) {
        super(R.layout.item_circle_comment, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleCommentBean data) {
        holder.setText(R.id.tv_name, data.getUserName());
        holder.setText(R.id.tv_content, data.getContent());
        holder.setText(R.id.tv_time, data.getCreateTimeStr());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(), (ImageView) holder.getView(R.id.iv_head));
        TextView tv_zkpl = holder.getView(R.id.tv_zkpl);
        TextView tv_zkpl1 = holder.getView(R.id.tv_zkpl1);
        tv_zkpl.setVisibility(View.GONE);
        tv_zkpl1.setVisibility(View.GONE);

        if (data.getIsTwoCommon().equals("1") && data.isExpand()) {
            tv_zkpl.setVisibility(View.VISIBLE);
            tv_zkpl.setText("展开" + data.getTwoCouont() + "条回复");
        } else {
            tv_zkpl.setVisibility(View.GONE);
            if (data.getPageNum() < data.getPages()) {
                tv_zkpl1.setVisibility(View.VISIBLE);
            } else {
                tv_zkpl1.setVisibility(View.GONE);
            }
        }
        RecyclerView rc_comment = holder.getView(R.id.rc_comment);
        CircleCommentChildAdapter circleCommentAdapter = new CircleCommentChildAdapter(mContext, data.getCommentVoList());
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_comment.setLayoutManager(lin);
        rc_comment.setAdapter(circleCommentAdapter);
        rc_comment.setVisibility(View.VISIBLE);

        holder.addOnClickListener(R.id.tv_zkpl);
        holder.addOnClickListener(R.id.tv_zkpl1);
        holder.addOnClickListener(R.id.iv_head);
        holder.addOnClickListener(R.id.ll_root);
    }


}
