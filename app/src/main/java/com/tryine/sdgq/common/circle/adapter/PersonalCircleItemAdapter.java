package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 个人主页-琴友圈列表
 */
public class PersonalCircleItemAdapter extends BaseQuickAdapter<CircleBean, BaseViewHolder> {
    String selectType;

    public PersonalCircleItemAdapter(Context context, List<CircleBean> data, String selectType) {
        super(R.layout.item_per_circle, data);
        mContext = context;
        this.selectType = selectType;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleBean data) {
        ImageView iv_play = holder.getView(R.id.iv_play);
        ImageView iv_delete = holder.getView(R.id.iv_delete);
        if ("0".equals(data.getContentType())) {
            iv_play.setVisibility(View.GONE);
        } else if ("1".equals(data.getContentType())) {
            iv_play.setVisibility(View.VISIBLE);
        }
        GlideEngine.createGlideEngine().loadImage(mContext, data.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        if (selectType.equals("0")) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }

        holder.setText(R.id.tv_title, data.getTitle());
        holder.setText(R.id.tv_giveCount, data.getGiveCount());

        holder.addOnClickListener(R.id.ll_root);
        holder.addOnClickListener(R.id.tv_giveCount);
        holder.addOnClickListener(R.id.iv_delete);
    }


}
