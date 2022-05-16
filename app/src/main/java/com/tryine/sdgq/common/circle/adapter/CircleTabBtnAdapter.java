package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;

import java.util.List;

/**
 * tab
 */
public class CircleTabBtnAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    int selectedTabPosition = 0;

    public CircleTabBtnAdapter(Context context, List<String> data) {
        super(R.layout.item_circle_tabbtn, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String data) {
        TextView tv_title = holder.getView(R.id.tv_title);
        if (holder.getAdapterPosition() == selectedTabPosition) {
            tv_title.setTextColor(0xffffffff);
            tv_title.setBackgroundResource(R.drawable.stroke_tab_button_radius_pre);
        } else {
            tv_title.setTextColor(0xff84594B);
            tv_title.setBackgroundResource(R.drawable.stroke_tab_button_radius);
        }
        tv_title.setText(data);
        holder.addOnClickListener(R.id.tv_title);

    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();

    }

}
