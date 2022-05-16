package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.common.mine.bean.TecheCasBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 我的课程
 */
public class MineCourseclassAdapter extends BaseQuickAdapter<TecheCasBean, BaseViewHolder> {


    public MineCourseclassAdapter(Context context, List<TecheCasBean> data) {
        super(R.layout.item_minecourseclass, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TecheCasBean data) {

        holder.setText(R.id.tv_title, data.getCouresName());
        holder.addOnClickListener(R.id.ll_root);
    }


}
