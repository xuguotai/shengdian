package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CommentBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.StarBarView;

import java.util.List;

/**
 * 教师详情-课表评价
 */
public class CurriculunCommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    public CurriculunCommentAdapter(Context context, List<CommentBean> data) {
        super(R.layout.item_curriculum_comment, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentBean data) {
        if(data.getTopStatus().equals("1")){
            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, ""
                    , holder.getView(R.id.iv_head));
            holder.setText(R.id.tv_name, "匿名用户");
        }else{
            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar()
                    , holder.getView(R.id.iv_head));
            holder.setText(R.id.tv_name, data.getUserName());
        }

        holder.setText(R.id.tv_content, data.getContent());
        holder.setText(R.id.tv_time, data.getCreateTime());
        holder.setText(R.id.tv_hoursNum, "学习" + data.getHoursNum() + "个课时评价");

        StarBarView starBarView = holder.getView(R.id.sb_lxs);
        starBarView.setStarMark(Float.parseFloat(data.getStar()));
        starBarView.setEnable(false);
        holder.addOnClickListener(R.id.ll_root);
    }


}
