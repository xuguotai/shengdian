package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 首页日期授课老师
 */
public class HomeTeachGridChildAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public HomeTeachGridChildAdapter(Context context, List<CourseBean> data) {
        super(R.layout.item_home_teach_grid_child, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean data) {
        LinearLayout ll_suo = holder.getView(R.id.ll_suo);
        holder.setText(R.id.tv_name, data.getName());
        holder.setText(R.id.tv_couresName, data.getCouresName());
        holder.setText(R.id.tv_classLayer, "再上" + data.getClassLayer() + "节课可解锁");
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(), holder.getView(R.id.iv_head));

        TextView tv_yy = holder.getView(R.id.tv_yy);
        if (data.getCourseStatus().equals("0")) {
            tv_yy.setText("预约");
            tv_yy.setBackgroundResource(R.mipmap.ic_home_yy);
        } else if (data.getCourseStatus().equals("1")) {
            tv_yy.setText("已预约");
            tv_yy.setBackgroundResource(R.mipmap.ic_home_yyy);
        } else if (data.getCourseStatus().equals("2")) {
            tv_yy.setText("停课");
            tv_yy.setBackgroundResource(R.mipmap.ic_home_yyy);
        } else if (data.getCourseStatus().equals("3")) {
            tv_yy.setText("完成");
            tv_yy.setBackgroundResource(R.mipmap.ic_home_yyy);
        } else if (data.getCourseStatus().equals("4")) {
            tv_yy.setText("满员");
            tv_yy.setBackgroundResource(R.mipmap.ic_home_yyy);
        }

        if (data.getClassLayer() > 0) {
            ll_suo.setVisibility(View.VISIBLE);
        } else {
            ll_suo.setVisibility(View.GONE);
        }


        holder.addOnClickListener(R.id.iv_head);
        holder.addOnClickListener(R.id.tv_yy);
    }


}
