package com.tryine.sdgq.common.mine.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.mine.bean.TecheCasBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.GradeTextView;

import java.util.List;

/**
 * 我的课程-学生列表
 */
public class MineCourseChildAdapter extends BaseQuickAdapter<TecheCasinfoBean, BaseViewHolder> {


    public MineCourseChildAdapter(Context context, List<TecheCasinfoBean> data) {
        super(R.layout.item_minecourseclass_child, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TecheCasinfoBean data) {

        GlideEngine.createGlideEngine().loadImage(mContext, data.getImgUrl()
                , holder.getView(R.id.iv_cover));
        holder.setText(R.id.tv_name, data.getName());
        GradeTextView tv_dj = holder.getView(R.id.tv_dj);
        holder.addOnClickListener(R.id.ll_root);
    }


}
