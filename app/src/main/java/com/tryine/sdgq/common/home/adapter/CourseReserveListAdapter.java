package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class CourseReserveListAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    String type = "1";
    String addrDes = "1";

    public CourseReserveListAdapter(Context context, List<CourseBean> data, String type,String addrDes) {
        super(R.layout.item_course_reserve_list, data);
        mContext = context;
        this.type = type;
        this.addrDes = addrDes;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean data) {
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(), holder.getView(R.id.iv_head));
        holder.setText(R.id.tv_teacherName, data.getTeacherName());
        holder.setText(R.id.tv_couresName, data.getCouresName());

        LinearLayout ll_btn = holder.getView(R.id.ll_btn);
        TextView tv_yy = holder.getView(R.id.tv_yy);

        if ("1".equals(type)) {
            ll_btn.setVisibility(View.GONE);
        } else {
            ll_btn.setVisibility(View.VISIBLE);
            if (data.getIsStart().equals("0")) {
                tv_yy.setText("未开始");
                tv_yy.setBackgroundResource(R.mipmap.ic_home_yyy);
            } else if (data.getCourseStatus().equals("1")) {
                tv_yy.setText("已结束");
                tv_yy.setBackgroundResource(R.mipmap.ic_home_yyy);
            }
        }

        holder.setText(R.id.tv_startTime, "上课时间：" + data.getStartTime());
        holder.setText(R.id.tv_addrDes, "上课地址：" + addrDes);

        holder.addOnClickListener(R.id.tv_cancel);
    }


}
