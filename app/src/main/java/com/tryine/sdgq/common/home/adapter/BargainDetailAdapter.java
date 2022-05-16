package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.BargainUserBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 *
 */
public class BargainDetailAdapter extends BaseQuickAdapter<BargainUserBean, BaseViewHolder> {
    String bargainType;

    public BargainDetailAdapter(Context context, List<BargainUserBean> data, String bargainType) {
        super(R.layout.item_bargain_detail, data);
        mContext = context;
        this.bargainType = bargainType;
    }

    @Override
    protected void convert(BaseViewHolder holder, BargainUserBean data) {
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getHelpHeadImg()
                , holder.getView(R.id.iv_head));
        holder.setText(R.id.tv_name, data.getHelpUserName());
        holder.setText(R.id.tv_createTime, data.getCreateTime() + " 受邀");
        if (bargainType.equals("0")) {
            holder.setText(R.id.tv_helpPrice, "砍掉了 " + data.getHelpPrice() + " SD金豆");
        } else {
            holder.setText(R.id.tv_helpPrice, "砍掉了 " + data.getHelpPrice() + " 元");
        }

        holder.addOnClickListener(R.id.ll_root);
    }


}
