package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 课程购买list
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:18
 */
public class CourseListAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public CourseListAdapter(Context context, List<CourseBean> data) {
        super(R.layout.item_course_list, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean courseBean) {
        GlideEngine.createGlideEngine().loadImage(mContext, courseBean.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_name, courseBean.getName());
        holder.setText(R.id.tv_layer, "课程期限：" + courseBean.getLayer()+"天");
        holder.setText(R.id.tv_price,   courseBean.getPrice());

        holder.addOnClickListener(R.id.tv_xq);
        holder.addOnClickListener(R.id.tv_submit);

    }


}
