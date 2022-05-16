package com.tryine.sdgq.common.live.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.bean.LiveCourseDetailBean;

import java.util.List;

/**
 * 直播课程章节
 */
public class LiveChapterAdapter extends BaseQuickAdapter<LiveCourseDetailBean, BaseViewHolder> {

    public LiveChapterAdapter(Context context, List<LiveCourseDetailBean> data) {
        super(R.layout.item_live_chapter_1, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, LiveCourseDetailBean data) {
        LinearLayout ll_live = holder.getView(R.id.ll_live);
        LinearLayout ll_buy = holder.getView(R.id.ll_buy);

        if(data.getIsBuy().equals("1")){  //已购买
            ll_live.setVisibility(View.VISIBLE);
            ll_buy.setVisibility(View.GONE);

            holder.setText(R.id.tv_teacherName, data.getName());
            TextView tv_name = holder.getView(R.id.tv_name);
            TextView tv_roomStatus = holder.getView(R.id.tv_roomStatus);
            TextView tv_go = holder.getView(R.id.tv_go);
            if (null == data.getRoomStatus()) {
                tv_name.setText("未开始");
                tv_name.setBackgroundResource(R.mipmap.ic_live_yjs);
                tv_roomStatus.setText(data.getStartTime());
                tv_go.setVisibility(View.GONE);
            } else {
                if (data.getRoomStatus().equals("0")) { //直播状态 0-未开始 1-直播中 2-离开 3-已结束 4-封禁
                    tv_name.setText("未开始");
                    tv_name.setBackgroundResource(R.mipmap.ic_live_yjs);
                    tv_roomStatus.setText(data.getStartTime());
                    tv_go.setVisibility(View.GONE);
                } else if (data.getRoomStatus().equals("1") || data.getRoomStatus().equals("2")) {
                    tv_name.setText("直播中");
                    tv_name.setBackgroundResource(R.mipmap.ic_live_skz);
                    tv_roomStatus.setText(data.getStartTime() + "  " + data.getLookCount() + "人正在观看");
                    tv_go.setVisibility(View.VISIBLE);
                    tv_go.setText("进入教室");
                } else if (data.getRoomStatus().equals("3")) {
                    tv_name.setText("已结束");
                    tv_go.setText("进入观看回放");
                    tv_name.setBackgroundResource(R.mipmap.ic_live_yjs);
                    tv_roomStatus.setText(data.getStartTime() + "  " + data.getLookCount() + "人学习过");
                }
            }
        }else{
            ll_live.setVisibility(View.GONE);
            ll_buy.setVisibility(View.VISIBLE);
            if(data.getIsLive().equals("1")){
                holder.setText(R.id.tv_name1, data.getName()+"(已直播)");
            }else{
                holder.setText(R.id.tv_name1, data.getName());
            }


            holder.setText(R.id.tv_startTime, data.getStartTime());
            holder.setText(R.id.tv_price, data.getGoldBean());

        }


        holder.addOnClickListener(R.id.ll_root);
        holder.addOnClickListener(R.id.tv_go);
    }


}
