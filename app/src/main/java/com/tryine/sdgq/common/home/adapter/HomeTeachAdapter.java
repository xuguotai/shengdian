package com.tryine.sdgq.common.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.List;

/**
 * 首页最近授课老师
 */
public class HomeTeachAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public HomeTeachAdapter(Context context, List<CourseBean> data) {
        super(R.layout.item_home_teach, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseBean data) {
        holder.setText(R.id.tv_name, data.getTeacherName());
        holder.setText(R.id.tv_couresName, data.getCouresName());
        GlideEngine.createGlideEngine().loadUserHeadImage(mContext, data.getAvatar(),holder.getView(R.id.iv_head));
        holder.addOnClickListener(R.id.ll_root);
    }



}
