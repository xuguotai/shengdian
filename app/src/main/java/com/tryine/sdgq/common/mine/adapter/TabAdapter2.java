package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;

import java.util.List;

/**
 * tab
 */
public class TabAdapter2 extends BaseQuickAdapter<String, BaseViewHolder> {

    int selectedTabPosition = 0;

    public TabAdapter2(Context context, List<String> data) {
        super(R.layout.item_tab2, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String data) {
        TextView tv_title = holder.getView(R.id.tv_title);
        ImageView iv_line = holder.getView(R.id.iv_line);
        if (holder.getAdapterPosition() == selectedTabPosition) {
            tv_title.setTypeface(null, Typeface.BOLD);
            tv_title.setTextColor(0xff333333);
            iv_line.setVisibility(View.VISIBLE);
        } else {
            tv_title.setTypeface(null, Typeface.NORMAL);
            tv_title.setTextColor(0xff898989);
            iv_line.setVisibility(View.INVISIBLE);
        }

        tv_title.setText(data);

        holder.addOnClickListener(R.id.tv_title);
    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();

    }

}
