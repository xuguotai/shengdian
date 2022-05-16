package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 购物车商品
 */
public class CartAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public CartAdapter(Context context, List<GoodsBean> data) {
        super(R.layout.item_cart_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoodsBean data) {
        if (!TextUtils.isEmpty(data.getGoodsFaceImage())) {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getGoodsFaceImage().split("!#!")[0], holder.getView(R.id.iv_cover));
        } else {
            GlideEngine.createGlideEngine().loadImage(mContext, data.getGoodsFaceImage(), holder.getView(R.id.iv_cover));
        }

        holder.setText(R.id.tv_title, data.getGoodsName());
        ImageView iv_price = holder.getView(R.id.iv_price);
        if (data.getBeanType() == 0) {
            iv_price.setBackgroundResource(R.mipmap.ic_jdz);
            holder.setText(R.id.tv_price, data.getPrice() + " 金豆");
        } else {
            iv_price.setBackgroundResource(R.mipmap.ic_ydz);
            holder.setText(R.id.tv_price, data.getPrice() + " 银豆");
        }


        TextView tv_num = holder.getView(R.id.tv_num);
        tv_num.setText(data.getQuantity()+"");

        CheckBox cb_checked = holder.getView(R.id.cb_checked);
        cb_checked.setChecked(data.isChecked());
        cb_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (null != onItemCheckedChangeListener) {
                    onItemCheckedChangeListener.onCheckedChange(isChecked, holder.getAdapterPosition());
                }

            }
        });

        holder.addOnClickListener(R.id.iv_jian);
        holder.addOnClickListener(R.id.iv_add);
    }


    OnItemCheckedChangeListener onItemCheckedChangeListener;

    public interface OnItemCheckedChangeListener {
        void onCheckedChange(boolean isChecked, int position);
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener onItemCheckedChangeListener) {
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

}
