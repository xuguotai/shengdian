package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 圣典视频首页-综合视频
 */
public class SdHomeVideoAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {
    String type;//

    public SdHomeVideoAdapter(Context context, List<VideoModel> data, String type) {
        super(R.layout.item_sdhome_video, data);
        mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoModel videoModel) {
        GlideEngine.createGlideEngine().loadImage(mContext, videoModel.getCoverUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_title, videoModel.title);
        holder.setText(R.id.tv_time, videoModel.getVideoTimeStr());
        holder.setText(R.id.tv_look, videoModel.getPlayCount());
        TextView tv_price = holder.getView(R.id.tv_price);
        if (type.equals("1")) {
            if(videoModel.getGoldenBean().equals("0")){
                tv_price.setText("免费");
            }else{
                if (videoModel.getBeanType() == 0) {
                    tv_price.setText(videoModel.getGoldenBean() + "金豆");
                } else {
                    tv_price.setText(videoModel.getGoldenBean() + "银豆");
                }
            }
            tv_price.setVisibility(View.VISIBLE);
        } else {
            tv_price.setVisibility(View.INVISIBLE);
        }

        TextView tv_zy = holder.getView(R.id.tv_zy);
        if (null != videoModel.getVideoType() && videoModel.getVideoType().equals("1")) {
            tv_zy.setVisibility(View.VISIBLE);
        } else {
            tv_zy.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.ll_root);
    }


}
