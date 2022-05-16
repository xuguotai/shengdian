package com.tryine.sdgq.common.live.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.bean.LiveCourseDateBean;

import java.util.List;

/**
 * 一对一直播课时间选择
 */
public class LiveCourseDateAdapter extends BaseQuickAdapter<LiveCourseDateBean, BaseViewHolder> {

    int selectedPosition = -1;

    public LiveCourseDateAdapter(Context context, List<LiveCourseDateBean> data) {
        super(R.layout.item_livecoursebuydate, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveCourseDateBean dateBean) {
        TextView tv_date = holder.getView(R.id.tv_date);
        tv_date.setText(dateBean.getStartTime() + "-" + dateBean.getEndTime());
        if (holder.getAdapterPosition() == selectedPosition) {
            tv_date.setBackgroundResource(R.drawable.recharge_stroke_radius_8);
            tv_date.setTextColor(0xffA98576);
        }else{
            if(dateBean.getIsSub().equals("1")){
                tv_date.setBackgroundResource(R.drawable.recharge_stroke_radius_8_666666);
                tv_date.setTextColor(0xff999999);
            }else{
                tv_date.setBackgroundResource(R.drawable.recharge_stroke_radius_8_333333);
                tv_date.setTextColor(0xff333333);
            }

        }

        holder.addOnClickListener(R.id.ll_root);
    }


    public void setSelectedTabPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();

    }


}
