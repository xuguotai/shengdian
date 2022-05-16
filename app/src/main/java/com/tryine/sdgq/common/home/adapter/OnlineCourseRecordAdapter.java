package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 线上课程记录
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:18
 */
public class OnlineCourseRecordAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    String type = "1";//1线上 2线下
    String addrDes;

    public OnlineCourseRecordAdapter(Context context, List<CourseBean> data, String type, String addrDes) {
        super(R.layout.item_online_course_record, data);
        mContext = context;
        this.type = type;
        this.addrDes = addrDes;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean data) {
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(), holder.getView(R.id.iv_head));
        holder.setText(R.id.tv_teacherName, data.getTeacherName());
        holder.setText(R.id.tv_couresName, data.getCouresName());
        holder.setText(R.id.tv_time, data.getStartTime());
        holder.setText(R.id.tv_addrDes, addrDes);

        TextView tv_pj = holder.getView(R.id.tv_pj);
        if (data.getIsAppraise().equals("1")) {
            tv_pj.setVisibility(View.GONE);
        } else {
            tv_pj.setVisibility(View.VISIBLE);
        }

        holder.addOnClickListener(R.id.tv_pj);
        holder.addOnClickListener(R.id.tv_ktzl);
        holder.addOnClickListener(R.id.ll_root);


    }


}
