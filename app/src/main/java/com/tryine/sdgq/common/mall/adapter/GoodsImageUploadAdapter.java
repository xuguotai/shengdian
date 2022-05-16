package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;

import java.util.List;

/**
 * 图片上传适配器
 * 作者：qujingfeng
 * 创建时间：2020.06.09 17:27
 */

public class GoodsImageUploadAdapter extends BaseQuickAdapter<ImageUploadBean, BaseViewHolder> {

    int width = 0;

    public GoodsImageUploadAdapter(Context context, List<ImageUploadBean> data, int num, int margin) {
        super(R.layout.item_image_upload, data);
        mContext = context;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width = (dm.widthPixels - (int) (margin * dm.density + 0.5f)) / num;
    }

    @Override
    protected void convert(BaseViewHolder holder, ImageUploadBean item) {
        RelativeLayout ll_item = holder.getView(R.id.ll_item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_item.getLayoutParams();
        lp.width = width;
        lp.height = width;
        ll_item.setLayoutParams(lp);

        holder.addOnClickListener(R.id.image)
                .addOnClickListener(R.id.iv_delete);
        if (0 != item.getResourceId()) {
            holder.getView(R.id.iv_delete).setVisibility(View.GONE);
            holder.getView(R.id.image).setBackgroundResource(item.getResourceId());
        } else {
            if (!"".equals(item.getLocalUrl())) {
                holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                Glide.with(mContext).load(item.getLocalUrl()).into((ImageView) holder.getView(R.id.image));
            }
        }
        if (holder.getAdapterPosition() > 5) {
            ll_item.setVisibility(View.GONE);
        }
    }

}
