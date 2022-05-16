package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 支付记录
 */
public class PayRecordAdapter extends BaseQuickAdapter<PayRecordBean, BaseViewHolder> {


    public PayRecordAdapter(Context context, List<PayRecordBean> data) {
        super(R.layout.item_payrecord, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, PayRecordBean data) {

        holder.setText(R.id.tv_title, data.getPayTypeVal());
        holder.setText(R.id.tv_time, data.getCreateTime());

        if ("0".equals(data.getBeanType())) {
            holder.setText(R.id.tv_beanCount, data.getBeanCountVal() + "SD金豆");
        } else {
            holder.setText(R.id.tv_beanCount, data.getBeanCountVal() + "SD银豆");
        }

        holder.addOnClickListener(R.id.ll_root);
    }


}
