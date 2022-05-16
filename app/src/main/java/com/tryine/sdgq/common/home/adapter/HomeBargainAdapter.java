package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页砍价list
 */
public class HomeBargainAdapter extends BaseQuickAdapter<BargainBean, BaseViewHolder> {

    public HomeBargainAdapter(Context context, List<BargainBean> data) {
        super(R.layout.item_home_bargain, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, BargainBean data) {

        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl(), holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, data.getName());

        CountDownTextView countdown_text_view = holder.getView(R.id.countdown_text_view);

        float f = Float.valueOf(DateTimeHelper.getSurplusTime(data.getEndTime()));
        countdown_text_view.setMaxTime(Math.round(f));
        countdown_text_view.setmFinishText("砍价已结束");
        countdown_text_view.startCountDown();
        countdown_text_view.setishour(true);

        holder.setText(R.id.tv_bargainCount, data.getBargainCount() + "人正在砍价");
        holder.setText(R.id.tv_appointPrice, data.getAppointPrice());
        TextView tv_price = holder.getView(R.id.tv_price);
        tv_price.setText("￥" + data.getPrice());
        tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_price.getPaint().setAntiAlias(true);

        SDAvatarListLayout sd_head = holder.getView(R.id.sd_head);
        sd_head.setAvatarListListener(data.getUserHeadImgList());


        holder.addOnClickListener(R.id.tv_kj);
    }





}
