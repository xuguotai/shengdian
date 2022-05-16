package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.ReportTypeBean;

import java.util.List;

/**
 * 投诉选项
 */
public class ReportTabAdapter extends BaseQuickAdapter<ReportTypeBean, BaseViewHolder> {

    int selectedTabPosition = 0;

    public ReportTabAdapter(Context context, List<ReportTypeBean> data) {
        super(R.layout.item_tab_report, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ReportTypeBean data) {
        CheckBox cb_tab = holder.getView(R.id.cb_tab);
        cb_tab.setText(" "+data.getName());
        if (holder.getAdapterPosition() == selectedTabPosition) {
            cb_tab.setChecked(true);
        } else {
            cb_tab.setChecked(false);
        }
        cb_tab.setClickable(false);
        holder.addOnClickListener(R.id.ll_root);
    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();

    }

}
