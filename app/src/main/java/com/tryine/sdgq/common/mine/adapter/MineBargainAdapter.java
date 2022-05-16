package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.CountDownTextView;
import com.tryine.sdgq.view.SDAvatarListLayout;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 砍价list
 */
public class MineBargainAdapter extends BaseQuickAdapter<BargainBean, BaseViewHolder> {

    public MineBargainAdapter(Context context, List<BargainBean> data) {
        super(R.layout.item_home_bargain, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, BargainBean data) {

        GlideEngine.createGlideEngine().loadImage(mContext, data.getBargainImgUrl(), holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, data.getBargainName());

        CountDownTextView countdown_text_view = holder.getView(R.id.countdown_text_view);

        float f = Float.valueOf(DateTimeHelper.getSurplusTime(data.getBargainEndTime()));
        countdown_text_view.setMaxTime(Math.round(f));
        countdown_text_view.setmFinishText("砍价已结束");
        countdown_text_view.startCountDown();
        countdown_text_view.setishour(true);
        countdown_text_view.setVisibility(View.GONE);

        TextView tv_status = holder.getView(R.id.tv_status);
        TextView tv_kj = holder.getView(R.id.tv_kj);
        TextView tv_time = holder.getView(R.id.tv_time);
        tv_time.setVisibility(View.GONE);
        if (data.getStatus().equals("1")) {//1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
            tv_status.setText("砍价中");
            tv_kj.setText("去砍价");
            countdown_text_view.setVisibility(View.VISIBLE);
            countdown_text_view.setVisibility(View.VISIBLE);
        } else if (data.getStatus().equals("2")) {
            tv_status.setText("待付款");
            tv_kj.setText("去付款");
        } else if (data.getStatus().equals("3")) {
            tv_status.setText("待核销");
            tv_kj.setText("待核销");
        } else if (data.getStatus().equals("4")) {
            tv_status.setText("已完成");
            tv_kj.setText("已完成");
        } else if (data.getStatus().equals("5")) {
            tv_status.setText("已过期");
            tv_kj.setText("已过期");
        }

        if(TextUtils.isEmpty(data.getHelpCount()) || data.getHelpCount().equals("null")){
            holder.setText(R.id.tv_bargainCount, "0人帮我砍价");
        }else{
            holder.setText(R.id.tv_bargainCount, data.getHelpCount() + "人帮我砍价");
        }

        holder.setText(R.id.tv_appointPrice, data.getCurrentPrice());

        TextView tv_price = holder.getView(R.id.tv_price);
        tv_price.setText("￥" + data.getCostPrice());
        tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_price.getPaint().setAntiAlias(true);

        SDAvatarListLayout sd_head = holder.getView(R.id.sd_head);
        sd_head.setAvatarListListener(data.getHelpHeadImgList());

        holder.addOnClickListener(R.id.tv_kj);
    }


}
