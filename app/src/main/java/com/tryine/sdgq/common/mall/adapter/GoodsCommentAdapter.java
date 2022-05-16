package com.tryine.sdgq.common.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mall.bean.GoodsCommentBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.FullScreenImg.AssImgPreviewActivity;
import com.tryine.sdgq.view.StarBarView;
import com.tryine.sdgq.view.roundImageView.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品评价
 */
public class GoodsCommentAdapter extends BaseQuickAdapter<GoodsCommentBean, BaseViewHolder> {

    public GoodsCommentAdapter(Context context, List<GoodsCommentBean> data) {
        super(R.layout.item_goodscomment, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoodsCommentBean data) {

        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getContentImgUrl(), holder.getView(R.id.user_avatar));
        holder.setText(R.id.tv_name, data.getUserName());
        holder.setText(R.id.tv_content, data.getContent());
        holder.setText(R.id.tv_time, data.getCreateTime());

        StarBarView starBar = (StarBarView) holder.getView(R.id.starBar);
        if(!TextUtils.isEmpty(data.getCommentLevel())){
            starBar.setStarMark(Float.parseFloat(data.getCommentLevel()));
        }else{
            starBar.setStarMark(Float.parseFloat("5"));
        }
        starBar.setEnable(false);

        RecyclerView rl_grid_img = holder.getView(R.id.rl_grid_img);
        if (!TextUtils.isEmpty(data.getImgUrl())) {
            rl_grid_img.setVisibility(View.VISIBLE);
            addGridImg(rl_grid_img, data.getImgUrl());
        } else {
            rl_grid_img.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.ll_root);
    }


    private void addGridImg(RecyclerView rl_grid_img, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }
        List<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < imgUrl.split(",").length; i++) {
            imgUrls.add(imgUrl.split(",")[i]);
        }
        CommonAdapter commonAdapter = new CommonAdapter(mContext, R.layout.item_show_image, imgUrls) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                int width = (dm.widthPixels - (int) (20 * dm.density + 0.5f)) / 3;
                RelativeLayout ll_item = holder.getView(R.id.ll_item);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_item.getLayoutParams();
                lp.width = width;
                lp.height = width;
                ll_item.setLayoutParams(lp);
                RoundImageView iv_image = holder.getView(R.id.image);
                GlideEngine.createGlideEngine().loadImage(mContext, (String) o, iv_image);
                iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgPreview(imgUrls);
                    }
                });

            }
        };
        rl_grid_img.setAdapter(commonAdapter);
        rl_grid_img.setLayoutManager(new GridLayoutManager(mContext, 3));
        rl_grid_img.suppressLayout(true);//解决点击冲突问题
    }


    /**
     * 查看大图
     **/
    private void imgPreview(List<String> list) {
        Intent intent = new Intent(mContext, AssImgPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("goodImages", new ArrayList<>(list));
        bundle.putInt("currentIndex", 0);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
