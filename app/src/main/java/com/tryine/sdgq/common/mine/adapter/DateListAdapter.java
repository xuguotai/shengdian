package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.mine.bean.DateBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 收藏圈子list
 */
public class DateListAdapter extends BaseQuickAdapter<DateBean, BaseViewHolder> {

    int selectedTabPosition = 0;

    public DateListAdapter(Context context, List<DateBean> data) {
        super(R.layout.item_date, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, DateBean data) {
        TextView tv_week = holder.getView(R.id.tv_week);
        TextView tv_day = holder.getView(R.id.tv_day);
        tv_week.setText(data.getWeek());
        tv_day.setText(data.getDay());
        ImageView iv_line = holder.getView(R.id.iv_line);

        if (holder.getAdapterPosition() == 0) {
            tv_week.setText("今天");
        } else {
            tv_week.setText(data.getWeek());
        }

        if (holder.getAdapterPosition() == selectedTabPosition) {
            iv_line.setVisibility(View.VISIBLE);
            tv_day.setTextColor(0xff000000);
            tv_week.setTypeface(null, Typeface.BOLD);
            tv_day.setTypeface(null, Typeface.BOLD);
        } else {
            iv_line.setVisibility(View.INVISIBLE);
            tv_day.setTextColor(0xffB6B6B6);
            tv_week.setTypeface(null, Typeface.NORMAL);
            tv_day.setTypeface(null, Typeface.NORMAL);
        }

        holder.addOnClickListener(R.id.ll_root);
    }

    public void setselectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();
    }

}
