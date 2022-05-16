package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.LabelBean;

import java.util.List;

/**
 * 个人主页标签
 */
public class PersonalHomePageLabelAdapter extends BaseQuickAdapter<LabelBean, BaseViewHolder> {

    int selectedTabPosition = 0;

    public PersonalHomePageLabelAdapter(Context context, List<LabelBean> data) {
        super(R.layout.item_per_label, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LabelBean data) {
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(data.getName());
        holder.addOnClickListener(R.id.tv_title);

    }

    public void setSelectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();

    }

}
