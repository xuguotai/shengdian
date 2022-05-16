package com.tryine.sdgq.common.home.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 校区展示list
 */
public class CampusItemAdapter extends BaseQuickAdapter<CampusBean, BaseViewHolder> {

    public CampusItemAdapter(Context context, List<CampusBean> data) {
        super(R.layout.item_campus, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CampusBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext, data.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_name, data.getName());
        holder.setText(R.id.tv_contact, data.getContact());
        holder.setText(R.id.tv_addrDes, data.getAddrDes());
        holder.setText(R.id.tv_distance, data.getDistance() + "km");

        holder.addOnClickListener(R.id.ll_root);
    }


}
