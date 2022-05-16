package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 我的体验卡使用记录list
 */
public class TyCardUseRecordListAdapter extends BaseQuickAdapter<ExperienceBean, BaseViewHolder> {

    public TyCardUseRecordListAdapter(Context context, List<ExperienceBean> data) {
        super(R.layout.item_tycard_record, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ExperienceBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext, data.getAvatar()
                , holder.getView(R.id.iv_head));

        holder.setText(R.id.tv_couresCatsName, data.getCouresCatsName());
        holder.setText(R.id.tv_campusName, data.getCampusName());
        holder.setText(R.id.tv_name, data.getTeacherName());
        holder.setText(R.id.tv_startDate, data.getStartDate());

        holder.addOnClickListener(R.id.ll_root);
    }


}
