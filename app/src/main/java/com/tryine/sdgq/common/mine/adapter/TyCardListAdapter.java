package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 体验卡首页list
 */
public class TyCardListAdapter extends BaseQuickAdapter<CardBean, BaseViewHolder> {

    public TyCardListAdapter(Context context, List<CardBean> data) {
        super(R.layout.item_tycard_home, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CardBean data) {
        LinearLayout ll_bg = holder.getView(R.id.ll_bg);
        TextView tv_status = holder.getView(R.id.tv_status);
        TextView tv_couresName = holder.getView(R.id.tv_couresName);
        TextView tv_experienceClass = holder.getView(R.id.tv_experienceClass);
        TextView tv_dete = holder.getView(R.id.tv_dete);


        TextView tv_zzhy = holder.getView(R.id.tv_zzhy);
        TextView ljsy = holder.getView(R.id.ljsy);
        TextView tv_jxsy = holder.getView(R.id.tv_jxsy);

        tv_jxsy.setVisibility(View.GONE);
        tv_zzhy.setVisibility(View.GONE);
        ljsy.setVisibility(View.GONE);

        //状态 0:未开始 1:使用中2：暂停使用3：已使用 4、转赠好友
        if (data.getStatus().equals("0")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_dsybg);
            tv_status.setText("待使用");
            ljsy.setVisibility(View.VISIBLE);
            tv_zzhy.setVisibility(View.VISIBLE);
        } else if (data.getStatus().equals("1")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_syzbg);
            tv_status.setText("使用中");
            tv_jxsy.setVisibility(View.VISIBLE);
        } else if (data.getStatus().equals("3")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_ysybg);
            tv_status.setText("已使用");
        } else if (data.getStatus().equals("4")) {
            ll_bg.setBackgroundResource(R.mipmap.ic_card_yzzbg);
            tv_status.setText("转赠好友");
        }

        tv_couresName.setText("包含课程：" + data.getCouresName());
        tv_experienceClass.setText("课 时：" + data.getExperienceClass());
        tv_dete.setText("体验期限：" + data.getStartTime() + " 至 " + data.getEndTime());

        holder.addOnClickListener(R.id.tv_detail);
        holder.addOnClickListener(R.id.tv_zzhy);
        holder.addOnClickListener(R.id.tv_jxsy);
        holder.addOnClickListener(R.id.ljsy);
        holder.addOnClickListener(R.id.ll_root);
    }


}
