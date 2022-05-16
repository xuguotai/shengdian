package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mall.bean.OrderListBean;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.view.CountDownTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表list
 */
public class OrderItemAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {

    public OrderItemAdapter(Context context, List<OrderListBean> data) {
        super(R.layout.item_order, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, OrderListBean data) {

        holder.setText(R.id.statusVal, data.getStatusVal());
        holder.setText(R.id.tv_totalPayPrice, data.getTotalPayPrice() + " SD金豆");
        LinearLayout ll_cancelTime = holder.getView(R.id.ll_cancelTime);
        CountDownTextView countdown_text_view = holder.getView(R.id.countdown_text_view);
        TextView tv_ztd = holder.getView(R.id.tv_ztd);
        TextView tv_cancel = holder.getView(R.id.tv_cancel);
        TextView tv_tk = holder.getView(R.id.tv_tk);
        TextView tv_pay = holder.getView(R.id.tv_pay);
        TextView tv_fh = holder.getView(R.id.tv_fh);
        TextView tv_confirm = holder.getView(R.id.tv_confirm);
        TextView tv_pj = holder.getView(R.id.tv_pj);

        ll_cancelTime.setVisibility(View.GONE);
        tv_cancel.setVisibility(View.GONE);
        tv_tk.setVisibility(View.GONE);
        tv_pay.setVisibility(View.GONE);
        tv_fh.setVisibility(View.GONE);
        tv_confirm.setVisibility(View.GONE);
        tv_ztd.setVisibility(View.GONE);
        tv_pj.setVisibility(View.GONE);

        //单状态 (0:订单关闭；1：待付款；2:订单支付超时；3:已取消 4：待评价；5:已完成 ；6:退款中；7:退款完成 8待自提)
        if ("0".equals(data.getStatus())) {

        } else if ("1".equals(data.getStatus())) {
            tv_cancel.setVisibility(View.VISIBLE);
            tv_pay.setVisibility(View.VISIBLE);
            ll_cancelTime.setVisibility(View.VISIBLE);

            float f = Float.valueOf(DateTimeHelper.getSurplusTime(data.getAutoCancelTime()));
            countdown_text_view.setMaxTime(Math.round(f));
            countdown_text_view.setmFinishText("");
            countdown_text_view.startCountDown();
            countdown_text_view.setishour(true);
        } else if ("2".equals(data.getStatus())) {

        } else if ("3".equals(data.getStatus())) {

        } else if ("4".equals(data.getStatus())) {
            tv_pj.setVisibility(View.VISIBLE);

        } else if ("5".equals(data.getStatus())) {

        } else if ("6".equals(data.getStatus())) {

        } else if ("7".equals(data.getStatus())) {

        } else if ("8".equals(data.getStatus())) {
            tv_ztd.setText("自提点：" + data.getPickAddress());
            tv_ztd.setVisibility(View.VISIBLE);
            tv_tk.setVisibility(View.VISIBLE);
            tv_confirm.setVisibility(View.VISIBLE);

        }

        RecyclerView rc_data = holder.getView(R.id.rc_data);
        OrderGoodsItemAdapter orderGoodsItemAdapter = new OrderGoodsItemAdapter(mContext, data.getPiratesOrderDetailListVoList());

        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(orderGoodsItemAdapter);
        holder.addOnClickListener(R.id.tv_cancel);
        holder.addOnClickListener(R.id.tv_tk);
        holder.addOnClickListener(R.id.tv_pay);
        holder.addOnClickListener(R.id.tv_fh);
        holder.addOnClickListener(R.id.tv_confirm);
        holder.addOnClickListener(R.id.tv_pj);
        holder.addOnClickListener(R.id.ll_root);
        holder.addOnClickListener(R.id.rc_data);

    }


}
