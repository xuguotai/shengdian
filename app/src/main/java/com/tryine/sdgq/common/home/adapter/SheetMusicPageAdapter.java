package com.tryine.sdgq.common.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 琴谱全屏
 */
public class SheetMusicPageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SheetMusicPageAdapter(Context context, List<String> imgUrl) {
        super(R.layout.item_sheetmusic_page, imgUrl);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String imgUrl) {

        ImageView iv_img = holder.getView(R.id.iv_img);
//        int screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
//        ViewGroup.LayoutParams lp = iv_img.getLayoutParams();
//        lp.height = screenWidth;
//        lp.width = screenWidth;
//        iv_img.setLayoutParams(lp);

        GlideEngine.createGlideEngine().loadImage(mContext, imgUrl,iv_img);
        holder.addOnClickListener(R.id.iv_img);
    }


}
