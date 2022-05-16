package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;

import java.util.List;

/**
 * 金豆明细-消耗记录
 */
public class JDDetailRecordAdapter extends BaseQuickAdapter<PayRecordBean, BaseViewHolder> {


    public JDDetailRecordAdapter(Context context, List<PayRecordBean> data) {
        super(R.layout.item_jd_detail_record, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, PayRecordBean data) {
        holder.setText(R.id.tv_title, data.getPayTypeVal());
        holder.setText(R.id.tv_time, data.getCreateTime());
        holder.setText(R.id.tv_beanCount, data.getBeanCountVal());
        TextView tv_remake = holder.getView(R.id.tv_remake);
        if(!TextUtils.isEmpty(data.getRemake())){
            tv_remake.setVisibility(View.VISIBLE);
            tv_remake.setText(data.getRemake());
        }else{
            tv_remake.setVisibility(View.GONE);
        }
        holder.addOnClickListener(R.id.ll_root);
    }



}
