package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseChapterBean;

import java.util.List;

/**
 * 线上课程list
 */
public class OnlineCourseAdapter extends BaseQuickAdapter<CourseChapterBean, BaseViewHolder> {

    int size = 0;

    public OnlineCourseAdapter(Context context, List<CourseChapterBean> data) {
        super(R.layout.item_online_course, data);
        size = data.size();
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseChapterBean data) {
        holder.setText(R.id.tv_title, data.getCourseDetailName());
        holder.setText(R.id.tv_time, data.getStartTime());
        View line = holder.getView(R.id.v_line);
        if (size == (holder.getAdapterPosition() + 1)) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

        TextView tv_hf = holder.getView(R.id.tv_hf);
        TextView tv_wzb = holder.getView(R.id.tv_wzb);
        TextView tv_kk = holder.getView(R.id.tv_kk);

        if (!TextUtils.isEmpty(data.getLiveId()) && Integer.parseInt(data.getLiveId()) > 0 && data.getRoomStatus().equals("1")) { //直播中
            tv_wzb.setVisibility(View.VISIBLE);
            tv_wzb.setText("直播中 点击进入");
        } else {
            if (!TextUtils.isEmpty(data.getRecordUrl())) {
                tv_hf.setVisibility(View.VISIBLE);
                tv_wzb.setVisibility(View.GONE);

                if (data.getIsAttend().equals("1")) {
                    tv_kk.setVisibility(View.GONE);
                } else {
                    tv_kk.setVisibility(View.VISIBLE);
                }

            } else {
                tv_hf.setVisibility(View.GONE);
                tv_wzb.setVisibility(View.VISIBLE);
                tv_wzb.setText("未直播");
            }
        }

        holder.addOnClickListener(R.id.ll_root);
    }


}
