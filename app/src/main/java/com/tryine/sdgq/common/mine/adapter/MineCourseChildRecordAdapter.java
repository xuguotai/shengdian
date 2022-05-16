package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoRecordBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.GradeTextView;

import java.util.List;

/**
 * 我的课程-学生列表-上课记录列表
 */
public class MineCourseChildRecordAdapter extends BaseQuickAdapter<TecheCasinfoRecordBean, BaseViewHolder> {


    public MineCourseChildRecordAdapter(Context context, List<TecheCasinfoRecordBean> data) {
        super(R.layout.item_minecourseclasschild_record, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TecheCasinfoRecordBean data) {
        holder.setText(R.id.tv_couresName, data.getCouresName());
        holder.setText(R.id.tv_startDate, data.getStartDate());
    }


}
