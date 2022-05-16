package com.tryine.sdgq.common.circle.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.roundImageView.RoundImageView;

import java.util.List;

/**
 * 展示图片适配器
 * 作者：qujingfeng
 * 创建时间：2020.06.09 17:27
 */

public class ShowImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    OnLookListener onLookListener;
    int width = 0;
    int radius = 0;
    Context mContext;
    double aspectRatio = 2;
    int num = 3;

    public ShowImageAdapter(Context context, List<String> data, int num, int margin, int radius) {
        super(R.layout.item_show_image, data);
        mContext = context;
        this.radius = radius;
        this.num = num;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width = (dm.widthPixels - (int) (margin * dm.density + 0.5f)) / num;
    }

    public void setRatio(double aspectRatio) {
        num = 1;
        if (0 == aspectRatio) {
            aspectRatio = 1;
        }
        this.aspectRatio = aspectRatio;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        RelativeLayout ll_item = holder.getView(R.id.ll_item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_item.getLayoutParams();

        if (num == 1) {
            lp.width = width * 2 / 3;
            lp.height = (int) (lp.width / aspectRatio);
        } else {
            lp.width = width;
            lp.height = lp.width;
        }
        ll_item.setLayoutParams(lp);

        RoundImageView image = holder.getView(R.id.image);
        image.setCornerRadius(radius);
        GlideEngine.createGlideEngine().loadImage(mContext, item, image);
        if (null != onLookListener) {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLookListener.look(holder.getAdapterPosition());
                }
            });
        }
    }


    public interface OnLookListener {
        void look(int index);
    }

    public void setOnLookListener(OnLookListener onLookListener) {
        this.onLookListener = onLookListener;
    }

}
