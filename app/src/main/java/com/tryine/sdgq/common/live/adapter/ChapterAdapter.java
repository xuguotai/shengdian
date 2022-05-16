package com.tryine.sdgq.common.live.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.bean.LiveCourseDetailBean;
import com.tryine.sdgq.common.mall.adapter.CartAdapter;
import com.tryine.sdgq.util.GlideEngine;

import java.util.List;

/**
 * 直播课程章节
 */
public class ChapterAdapter extends BaseQuickAdapter<LiveCourseDetailBean, BaseViewHolder> {

    public ChapterAdapter(Context context, List<LiveCourseDetailBean> data) {
        super(R.layout.item_live_chapter, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveCourseDetailBean data) {
        holder.setText(R.id.tv_name, data.getName());
        holder.setText(R.id.tv_price, data.getGoldBean()+"金豆");
        holder.addOnClickListener(R.id.ll_root);

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
    }

    OnItemCheckedChangeListener onItemCheckedChangeListener;

    public interface OnItemCheckedChangeListener {
        void onCheckedChange(boolean isChecked, int position);
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener onItemCheckedChangeListener) {
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

}
