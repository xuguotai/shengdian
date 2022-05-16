package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.TaskBean;

import java.util.List;

/**
 * 任务
 */
public class TaskAdapter extends BaseQuickAdapter<TaskBean, BaseViewHolder> {

    public TaskAdapter(Context context, List<TaskBean> data) {
        super(R.layout.item_task, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TaskBean data) {
        ImageView img = holder.getView(R.id.img);

        if(data.getName().contains("发帖")){
            img.setBackgroundResource(R.mipmap.ic_task_ft);
        }else if(data.getName().contains("分享")){
            img.setBackgroundResource(R.mipmap.ic_task_share);
        }else if(data.getName().contains("赞")){
            img.setBackgroundResource(R.mipmap.ic_task_dz);
        }else if(data.getName().contains("签到")){
            img.setBackgroundResource(R.mipmap.ic_task_qd);
        }else if(data.getName().contains("邀请")){
            img.setBackgroundResource(R.mipmap.ic_task_yq);
        }

        holder.setText(R.id.tv_title,data.getName());
        holder.setText(R.id.tv_title1,data.getRuleRemark());


        //任务状态 0-未完成 1-领取奖励 2-已领取
        TextView tv_submit = holder.getView(R.id.tv_submit);
        if(data.getTaskStatus().equals("0")){
            tv_submit.setText("马上完成");
            tv_submit.setBackgroundResource(R.drawable.stroke_tab_button_radius_af8c7e);
        }else if(data.getTaskStatus().equals("1")){
            tv_submit.setText("领取奖励");
            tv_submit.setBackgroundResource(R.drawable.stroke_tab_button_radius_f9ede8_pre);
        }else if(data.getTaskStatus().equals("2")){
            tv_submit.setText("已领取");
            tv_submit.setBackgroundResource(R.drawable.stroke_tab_button_radius_af8c7e);
        }

        holder.addOnClickListener(R.id.tv_submit);
    }


}
