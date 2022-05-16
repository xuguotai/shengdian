package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 个人标签列表
 */
public class LabelListAdapter extends BaseQuickAdapter<LabelBean, BaseViewHolder> {

    int type = 0;

    public LabelListAdapter(Context context, List<LabelBean> data) {
        super(R.layout.item_label_list, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LabelBean data) {

        holder.setText(R.id.tv_title, data.getName());
        ImageView iv_close = holder.getView(R.id.iv_close);
        if(type == 0){
            iv_close.setVisibility(View.GONE);
        }else{
            iv_close.setVisibility(View.VISIBLE);
        }

        holder.addOnClickListener(R.id.iv_close);
    }


    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }


}
