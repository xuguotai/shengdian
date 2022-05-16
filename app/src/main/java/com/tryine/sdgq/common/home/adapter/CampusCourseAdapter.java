package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 校区课程展示list
 */
public class CampusCourseAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {
    int type;

    public CampusCourseAdapter(Context context, List<CourseBean> data, int type) {
        super(R.layout.item_campus_course, data);
        mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_name, data.getName());
        TextView tv_zx = holder.getView(R.id.tv_zx);
        if (type == 1) {
            holder.setText(R.id.tv_layer, data.getCampusName());
            tv_zx.setVisibility(View.VISIBLE);
        } else {
            holder.setText(R.id.tv_layer, "课程期限：" + data.getLayer());
            tv_zx.setVisibility(View.GONE);
        }

        holder.setText(R.id.tv_price, "¥" + data.getPrice());

        holder.addOnClickListener(R.id.ll_root);
    }


}
