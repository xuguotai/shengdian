package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 校区详情-更多老师列表
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 11:18
 */
public class CampusListTeacherAdapter extends BaseQuickAdapter<TeacherBean, BaseViewHolder> {

    public CampusListTeacherAdapter(Context context, List<TeacherBean> data) {
        super(R.layout.item_list_teacher, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TeacherBean data) {
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getHeadImg(), holder.getView(R.id.iv_headImg));
        holder.setText(R.id.tv_name, data.getName());
        if(!TextUtils.isEmpty(data.getCouresName()) && data.getCouresName().contains(",")){
            holder.setText(R.id.tv_couresName, data.getCouresName().split(",")[0]);
        }else{
            holder.setText(R.id.tv_couresName, data.getCouresName());
        }



        holder.addOnClickListener(R.id.ll_root);
    }


}
