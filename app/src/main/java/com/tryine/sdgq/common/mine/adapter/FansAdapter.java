package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.GradeTextView;

import java.util.List;

/**
 * 粉丝
 */
public class FansAdapter extends BaseQuickAdapter<FansBean, BaseViewHolder> {

    private String type;

    public FansAdapter(Context context, List<FansBean> data,String type) {
        super(R.layout.item_fans, data);
        mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, FansBean data) {

        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar()
                , holder.getView(R.id.iv_head));

        holder.setText(R.id.tv_name, data.getUserName());

        TextView tv_gz = holder.getView(R.id.tv_gz);

        if(type.equals("0")){//关注列表
            if(data.getIsMutual().equals("0")){
                tv_gz.setText("取消关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yyy);
                tv_gz.setTextColor(0xffffffff);
            }else{
                tv_gz.setText("取消关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yyy);
                tv_gz.setTextColor(0xffffffff);
            }
        }else{ //粉丝列表
            if(data.getIsMutual().equals("0")){
                tv_gz.setText("关注");
                tv_gz.setBackgroundResource(R.mipmap.ic_home_yy);
                tv_gz.setTextColor(0xffffffff);
            }else{
                tv_gz.setText("相互关注");
                tv_gz.setBackgroundResource(R.drawable.recharge_stroke_radius_5);
                tv_gz.setTextColor(0xffAF8C7E);
            }
        }

        GradeTextView tv_dj = holder.getView(R.id.tv_dj);
        if(!TextUtils.isEmpty(data.getLevel())){
            tv_dj.setData(Integer.parseInt(data.getLevel()));
        }else{
            tv_dj.setData(0);
        }

        holder.addOnClickListener(R.id.tv_gz);
        holder.addOnClickListener(R.id.ll_root);
    }


}
