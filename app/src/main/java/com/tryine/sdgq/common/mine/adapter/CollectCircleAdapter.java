package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.GradeTextView;

import java.util.List;

/**
 * 收藏圈子list
 */
public class CollectCircleAdapter extends BaseQuickAdapter<CircleBean, BaseViewHolder> {

    public CollectCircleAdapter(Context context, List<CircleBean> data) {
        super(R.layout.item_collect_circle, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleBean data) {
        ImageView iv_play = holder.getView(R.id.iv_play);
        if ("0".equals(data.getContentType())) {
            iv_play.setVisibility(View.GONE);
        } else if ("1".equals(data.getContentType())) {
            iv_play.setVisibility(View.VISIBLE);
        }
        GlideEngine.createGlideEngine().loadImage(mContext, data.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        GradeTextView tv_dj = holder.getView(R.id.tv_dj);
        if(!TextUtils.isEmpty(data.getLevel())){
            tv_dj.setData(Integer.parseInt(data.getLevel()));
        }else{
            tv_dj.setData(0);
        }

        holder.setText(R.id.tv_title, data.getTitle());
        holder.setText(R.id.tv_name, data.getUserName());
        holder.setText(R.id.tv_giveCount, data.getGiveCount());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar()
                , holder.getView(R.id.iv_head));

        holder.addOnClickListener(R.id.ll_root);
    }



}
