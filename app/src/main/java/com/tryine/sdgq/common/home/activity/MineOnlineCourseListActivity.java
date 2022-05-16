package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.VideoPlayFullScreenActivity;
import com.tryine.sdgq.common.home.adapter.OnlineCourseAdapter;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseChapterBean;
import com.tryine.sdgq.common.live.activity.push.LivePlayerMainActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCConstants;
import com.tryine.sdgq.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-03-31 18:22
 */
public class MineOnlineCourseListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    CommonAdapter commonAdapter;
    CourseBean courseBean;
    String name;

    public static void start(Context context, String name, CourseBean courseBean) {
        Intent intent = new Intent();
        intent.setClass(context, MineOnlineCourseListActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("courseBean", (Serializable) courseBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mineonlinecourse;
    }

    @Override
    protected void init() {
        setWhiteBar();
        name = getIntent().getStringExtra("name");
        courseBean = (CourseBean) getIntent().getSerializableExtra("courseBean");
        tv_title.setText(name);
        initViews();
    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }

    private void initViews() {
        OnlineCourseAdapter onlineCourseAdapter = new OnlineCourseAdapter(mContext, courseBean.getDetailVoList());
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(onlineCourseAdapter);
        onlineCourseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!TextUtils.isEmpty(courseBean.getDetailVoList().get(position).getLiveId()) && Integer.parseInt(courseBean.getDetailVoList().get(position).getLiveId()) > 0
                        && courseBean.getDetailVoList().get(position).getRoomStatus().equals("1")) { //直播中
                    Intent intent = new Intent(mContext, LivePlayerMainActivity.class);
                    intent.putExtra(TCConstants.GROUP_ID, Integer.parseInt(courseBean.getDetailVoList().get(position).getLiveId()));
                    startActivity(intent);
                } else if (null != courseBean.getDetailVoList().get(position).getRoomStatus() && courseBean.getDetailVoList().get(position).getRoomStatus().equals("3")) {
                    if (!TextUtils.isEmpty(courseBean.getDetailVoList().get(position).getRecordUrl())) {
                        VideoPlayFullScreenActivity.start(mContext, courseBean.getDetailVoList().get(position).getRecordUrl());
                    } else {
                        ToastUtil.toastLongMessage("回放地址错误");
                    }

                }

            }
        });




//        commonAdapter = new CommonAdapter(mContext, R.layout.item_online_course_title, courseBeanLists) {
//            @Override
//            protected void convert(ViewHolder holder, Object o, int position) {
//                CourseBean courseBean = (CourseBean) o;
//                holder.setText(R.id.tv_title, courseBean.getName());
//                holder.setText(R.id.tv_teacherName, courseBean.getTeacherName());
//                GlideEngine.createGlideEngine().loadUserHeadImage(mContext, courseBean.getTeacherHeadImg()
//                        , holder.getView(R.id.iv_head));
//                RecyclerView rc_childData = holder.getView(R.id.rc_data);
//                OnlineCourseAdapter onlineCourseAdapter = new OnlineCourseAdapter(mContext, courseBean.getDetailVoList());
//                LinearLayoutManager lin = new LinearLayoutManager(mContext);
//                lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
//                rc_childData.setLayoutManager(lin);
//                rc_childData.setAdapter(onlineCourseAdapter);
//                onlineCourseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                    @Override
//                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                        if (!TextUtils.isEmpty(courseBean.getDetailVoList().get(position).getLiveId()) && Integer.parseInt(courseBean.getDetailVoList().get(position).getLiveId()) > 0
//                                && courseBean.getDetailVoList().get(position).getRoomStatus().equals("1")) { //直播中
//                            Intent intent = new Intent(mContext, LivePlayerMainActivity.class);
//                            intent.putExtra(TCConstants.GROUP_ID, Integer.parseInt(courseBean.getDetailVoList().get(position).getLiveId()));
//                            startActivity(intent);
//                        } else if (null != courseBean.getDetailVoList().get(position).getRoomStatus() && courseBean.getDetailVoList().get(position).getRoomStatus().equals("3")) {
//                            if (!TextUtils.isEmpty(courseBean.getDetailVoList().get(position).getRecordUrl())) {
//                                VideoPlayFullScreenActivity.start(mContext, courseBean.getDetailVoList().get(position).getRecordUrl());
//                            } else {
//                                ToastUtil.toastLongMessage("回放地址错误");
//                            }
//
//                        }
//
//                    }
//                });
//
//            }
//        };
//        LinearLayoutManager lin = new LinearLayoutManager(mContext);
//        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
//        rc_data.setLayoutManager(lin);
//        rc_data.setAdapter(commonAdapter);

    }


}
