package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页日期授课老师
 */
public class HomeTeachGridAdapter extends BaseQuickAdapter<CourseTimeBean, BaseViewHolder> {

    public HomeTeachGridAdapter(Context context, List<CourseTimeBean> data) {
        super(R.layout.item_home_teach_grid, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseTimeBean data) {
        holder.setText(R.id.tv_time, data.getTime());
        RecyclerView rv_teachData = holder.getView(R.id.rv_teachData);

        HomeTeachGridChildAdapter homeTeachGridChildAdapter = new HomeTeachGridChildAdapter(mContext, data.getPiratesTeacherVoList());
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.HORIZONTAL);//选择竖直列表
        rv_teachData.setLayoutManager(lin);
        rv_teachData.setAdapter(homeTeachGridChildAdapter);

    }


}
