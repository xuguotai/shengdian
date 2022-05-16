package com.tryine.sdgq.common.live.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 直播首页课程列表
 */
public class LiveCourseAdapter extends BaseQuickAdapter<LiveBean, BaseViewHolder> {

    public LiveCourseAdapter(Context context, List<LiveBean> data) {
        super(R.layout.item_live_course, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_couresName, data.getName());
        holder.setText(R.id.tv_price, data.getGoldBean() + " 金豆");
        TextView tv_courseType = holder.getView(R.id.tv_courseType);
        if (data.getIsSatisfy().equals("1")) {
            tv_courseType.setVisibility(View.VISIBLE);
        } else {
            tv_courseType.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.ll_root);
    }
}
