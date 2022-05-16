package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;

import java.util.List;

/**
 * tab
 */
public class TabMessageAdapter extends BaseQuickAdapter<HomeMenuBean, BaseViewHolder> {

    int selectedTabPosition = 0;

    public TabMessageAdapter(Context context, List<HomeMenuBean> data) {
        super(R.layout.item_tab_msg, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeMenuBean data) {
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_mark = holder.getView(R.id.tv_mark);
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

        tv_title.setText(data.getName());

        if (data.getCount() > 0) {
            tv_mark.setVisibility(View.VISIBLE);
            if(data.getCount() > 99){
                tv_mark.setText("···");
            }else{
                tv_mark.setText(data.getCount() + "");
            }
        } else {
            tv_mark.setVisibility(View.INVISIBLE);
        }

        holder.addOnClickListener(R.id.tv_title);
    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();

    }

}
