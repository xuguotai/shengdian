package com.tryine.sdgq.common.live.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 直播首页菜单
 */
public class LiveMenuAdapter extends BaseQuickAdapter<HomeMenuBean, BaseViewHolder> {

    public LiveMenuAdapter(Context context, List<HomeMenuBean> data) {
        super(R.layout.item_live_menu, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeMenuBean data) {
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(data.getName());
        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_img));
        holder.addOnClickListener(R.id.ll_root);
    }



}
