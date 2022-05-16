package com.tryine.sdgq.common.live.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 直播列表
 */
public class LiveAdapter extends BaseQuickAdapter<LiveBean, BaseViewHolder> {

    public LiveAdapter(Context context, List<LiveBean> data) {
        super(R.layout.item_live, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveBean liveBean) {
        GlideEngine.createGlideEngine().loadImage(liveBean.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, liveBean.getName());
        holder.setText(R.id.tv_goldBean, liveBean.getGoldBean() + " 金豆解锁");
        TextView tv_zbz = holder.getView(R.id.tv_zbz);
        if ("1".equals(liveBean.getLiveStatus())) {
            tv_zbz.setVisibility(View.VISIBLE);
        } else {
            tv_zbz.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.ll_root);
    }


}
