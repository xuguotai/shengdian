package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.mine.bean.MessageBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 系统消息list
 */
public class MessageAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {

    public MessageAdapter(Context context, List<MessageBean> data) {
        super(R.layout.item_message, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, MessageBean data) {


        holder.setText(R.id.tv_title, data.getContent());
        holder.setText(R.id.tv_time, data.getCreateDate());
        TextView tv_status = holder.getView(R.id.tv_status);
        if (data.getIsRead().equals("1")) {
            tv_status.setText("已读");
            tv_status.setBackgroundResource(R.mipmap.ic_msg_yd);
        } else {
            tv_status.setText("未读");
            tv_status.setBackgroundResource(R.mipmap.ic_msg_wd);
        }

        holder.addOnClickListener(R.id.ll_root);
    }


}
