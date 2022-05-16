package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.RewarBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 我的打赏/我的礼物
 */
public class RewardAdapter extends BaseQuickAdapter<RewarBean, BaseViewHolder> {

    String type;//类型  0:我的打赏 1:我的礼物

    public RewardAdapter(Context context, List<RewarBean> data, String type) {
        super(R.layout.item_reward, data);
        mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, RewarBean data) {
        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, data.getUserName());
        holder.setText(R.id.tv_date, data.getCreateTime());
        holder.addOnClickListener(R.id.ll_root);
    }


}
