package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.GradeTextView;

import java.util.List;

/**
 * 首页琴友圈-发现列表
 */
public class CircleItemAdapter extends BaseQuickAdapter<CircleBean, BaseViewHolder> {
    int searchType = 0;

    public CircleItemAdapter(Context context, List<CircleBean> data) {
        super(R.layout.item_circle, data);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder holder, CircleBean data) {
        ImageView iv_play = holder.getView(R.id.iv_play);
        ImageView iv_vip = holder.getView(R.id.iv_vip);
        TextView tv_distance = holder.getView(R.id.tv_distance);
        TextView tv_giveCount = holder.getView(R.id.tv_giveCount);
        if ("0".equals(data.getContentType())) {
            iv_play.setVisibility(View.GONE);
        } else if ("1".equals(data.getContentType())) {
            iv_play.setVisibility(View.VISIBLE);
        }
        GlideEngine.createGlideEngine().loadImage(mContext, data.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        if (searchType == 2) {
            tv_distance.setVisibility(View.VISIBLE);
            if (data.getDistance().equals("0")) {
                tv_distance.setText("0.01km");
            } else {
                tv_distance.setText(data.getDistance() + "km");
            }

        } else {
            tv_distance.setVisibility(View.GONE);
        }

        iv_vip.setVisibility(data.getIsVip() == 0 ? View.GONE : View.VISIBLE);

        GradeTextView tv_dj = holder.getView(R.id.tv_dj);
        if (!TextUtils.isEmpty(data.getLevel())) {
            tv_dj.setData(Integer.parseInt(data.getLevel()));
        } else {
            tv_dj.setData(0);
        }

        setdrawableLeft(tv_giveCount, data.getIsGive(), R.mipmap.ic_comment_dz, R.mipmap.ic_comment_dz_pre);

        holder.setText(R.id.tv_title, data.getTitle());
        holder.setText(R.id.tv_name, data.getUserName());
        holder.setText(R.id.tv_giveCount, data.getGiveCount());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar()
                , holder.getView(R.id.iv_head));

        holder.addOnClickListener(R.id.tv_giveCount);
        holder.addOnClickListener(R.id.ll_root);
    }


    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }


    /**
     * 是否 0-否 1-是
     */
    private void setdrawableLeft(TextView t, String type, int id, int id1) {
        Drawable drawable;
        if ("0".equals(type)) {
            drawable = mContext.getResources().getDrawable(
                    id);
            t.setTextColor(0xffd3d3d3);
        } else {
            drawable = mContext.getResources().getDrawable(
                    id1);
            t.setTextColor(0xffA98576);
        }
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        t.setCompoundDrawables(drawable, null, null, null);
    }

}
