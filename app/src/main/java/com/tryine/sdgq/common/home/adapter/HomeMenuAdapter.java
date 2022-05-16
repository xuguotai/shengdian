package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;

import java.util.List;

/**
 * 首页菜单
 */
public class HomeMenuAdapter extends BaseQuickAdapter<HomeMenuBean, BaseViewHolder> {

    public HomeMenuAdapter(Context context, List<HomeMenuBean> data) {
        super(R.layout.item_home_menu, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeMenuBean data) {
        ImageView iv_img = holder.getView(R.id.iv_img);
        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(data.getTitle());
        iv_img.setBackgroundResource(data.getImgId());
        holder.addOnClickListener(R.id.ll_root);
    }



}
