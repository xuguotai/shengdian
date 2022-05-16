package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 首页日期授课老师
 */
public class HomeTeachGridChildAdapter1 extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public HomeTeachGridChildAdapter1(Context context, List<CourseBean> data) {
        super(R.layout.item_home_teach_grid_child1, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean data) {
        holder.setText(R.id.tv_couresName, data.getCouresName());
        holder.setText(R.id.tv_addrDes, "上课地点：" + data.getCampusName());
    }


}
