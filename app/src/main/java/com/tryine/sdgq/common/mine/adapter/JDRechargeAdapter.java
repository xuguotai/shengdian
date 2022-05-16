package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;

import java.util.List;

/**
 * 金豆充值
 */
public class JDRechargeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    int selectedTabPosition = 0;

    public JDRechargeAdapter(Context context, List<String> data) {
        super(R.layout.item_recharge, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String data) {
        LinearLayout ll_item = holder.getView(R.id.ll_item);
        LinearLayout ll_amount = holder.getView(R.id.ll_amount);
        TextView tv_qtje = holder.getView(R.id.tv_qtje);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_amount = holder.getView(R.id.tv_amount);

        tv_amount.setText("¥" + data);
        if (!data.equals("其他金额")) {
            tv_num.setText(Integer.parseInt(data) * 10 + " SD金豆");
        } else {

        }


        if (holder.getAdapterPosition() == selectedTabPosition) {
            ll_item.setBackgroundResource(R.mipmap.ic_recharge_bg);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);//4个参数按顺序分别是左上右下
            ll_item.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(10, 10, 10, 10);//4个参数按顺序分别是左上右下
            ll_item.setLayoutParams(layoutParams);
            ll_item.setBackgroundResource(R.drawable.recharge_stroke_radius_5);
        }


        if (data.equals("其他金额")) {
            ll_amount.setVisibility(View.GONE);
            tv_qtje.setVisibility(View.VISIBLE);
        } else {
            ll_amount.setVisibility(View.VISIBLE);
            tv_qtje.setVisibility(View.GONE);
        }


        holder.addOnClickListener(R.id.ll_item);

    }

    public void setselectedTabPosition(int selectedTabPosition) {
        this.selectedTabPosition = selectedTabPosition;
        notifyDataSetChanged();

    }


}
