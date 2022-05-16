package com.tryine.sdgq.common.home.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tryine.sdgq.R;
import com.tryine.sdgq.adapter.CommonAdapter;
import com.tryine.sdgq.adapter.ViewHolder;
import com.tryine.sdgq.base.BaseFragment;
import com.tryine.sdgq.common.circle.activity.VideoPlayFullScreenActivity;
import com.tryine.sdgq.common.circle.adapter.CircleTabBtnAdapter;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.activity.CancelReserveCourseActivity;
import com.tryine.sdgq.common.home.activity.MineOnlineCourseListActivity;
import com.tryine.sdgq.common.home.activity.OnlineCourseRecordActivity;
import com.tryine.sdgq.common.home.activity.ReserveCourseActivity;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.adapter.OnlineCourseAdapter;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.BargainPresenter;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.common.live.activity.LiveTypeListActivity;
import com.tryine.sdgq.common.live.activity.push.LivePlayerMainActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.LiveRoomManager;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoom;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.audience.TCAudienceActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCConstants;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.UIUtils;
import com.tryine.sdgq.view.banner.BannerCourseViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的线上课程
 *
 * @author: zhangshuaijun
 * @time: 2021-11-30 13:22
 */
public class MineOnlineCourseFragment extends BaseFragment implements CourseView {

    @BindView(R.id.sl_refreshLayout)
    SmartRefreshLayout slRefreshLayout;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.tv_qkk)
    TextView tv_qkk;


    List<String> tabBeanList = new ArrayList<String>();
    @BindView(R.id.rv_tabbtn)
    public RecyclerView rv_tabbtn;
    CircleTabBtnAdapter circleTabBtnAdapter;

    List<CourseBean> courseBeanLists = new ArrayList<>();

    CoursePresenter coursePresenter;

    CommonAdapter commonAdapter;

    int pageNum = 1;
    int searchType = 0;

    private TRTCLiveRoom mTRTCLiveRoom;

    @Override
    public int getlayoutId() {
        return R.layout.fragment_mineonlinecourse;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTRTCLiveRoom = TRTCLiveRoom.sharedInstance(mContext);
        initViews();
        smartRefresh();
        coursePresenter = new CoursePresenter(mContext);
        coursePresenter.attachView(this);
        coursePresenter.getOnLineCourseList(pageNum, searchType);
    }

    protected void initViews() {

        tabBeanList.add("全部");
        tabBeanList.add("未完结");
        tabBeanList.add("已完结");
        circleTabBtnAdapter = new CircleTabBtnAdapter(getContext(), tabBeanList);
        rv_tabbtn.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_tabbtn.setAdapter(circleTabBtnAdapter);
        circleTabBtnAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                searchType = position;
                pageNum = 1;
                coursePresenter.getOnLineCourseList(pageNum, searchType);
                circleTabBtnAdapter.setSelectedTabPosition(position);
            }
        });


        tv_qkk.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_qkk.getPaint().setAntiAlias(true);//抗锯齿

        commonAdapter = new CommonAdapter(mContext, R.layout.item_online_course_title, courseBeanLists) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                CourseBean courseBean = (CourseBean) o;
                holder.setText(R.id.tv_title, courseBean.getName());
                holder.setText(R.id.tv_teacherName, courseBean.getTeacherName());
                holder.setText(R.id.tv_courseType, courseBean.getCourseType().equals("0") ? "直播大课" : "一对一课程");
                GlideEngine.createGlideEngine().loadUserHeadImage(mContext, courseBean.getTeacherHeadImg()
                        , holder.getView(R.id.iv_head));

                LinearLayout ll_root = holder.getView(R.id.ll_root);
                ll_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if(courseBean.getCourseType().equals("0")){
                            MineOnlineCourseListActivity.start(mContext, courseBean.getName(), courseBean);
//                        }

                    }
                });
            }
        };
        LinearLayoutManager lin = new LinearLayoutManager(mContext);
        lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
        rc_data.setLayoutManager(lin);
        rc_data.setAdapter(commonAdapter);
    }

    @OnClick({R.id.tv_qkk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_qkk:
                LiveTypeListActivity.start(mContext, 0);
                break;
        }
    }

    /**
     * 监听下拉和上拉状态
     **/
    private void smartRefresh() {
        //设置刷新样式
        slRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        slRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        //下拉刷新
        slRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                coursePresenter.getOnLineCourseList(pageNum, searchType);
            }
        });
        //上拉加载
        slRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                coursePresenter.getOnLineCourseList(pageNum, searchType);
            }
        });
    }


    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {
        slRefreshLayout.finishRefresh();
        slRefreshLayout.finishLoadMore();
        if (pageNum >= pages) {
            slRefreshLayout.setEnableLoadMore(false);
        } else {
            slRefreshLayout.setEnableLoadMore(true);
        }
        if (pageNum == 1) {
            courseBeanLists.clear();
        }
        courseBeanLists.addAll(courseBeanList);
        if (courseBeanLists.size() == 0) {
            ll_no_data.setVisibility(View.VISIBLE);
            slRefreshLayout.setVisibility(View.GONE);
        } else {
            ll_no_data.setVisibility(View.GONE);
            slRefreshLayout.setVisibility(View.VISIBLE);
        }
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList, int pages) {

    }

    @Override
    public void onGetcancelledSuccess(int count, int positions) {

    }

    @Override
    public void onAddCampusSuccess() {

    }

    @Override
    public void onAddCourseDataSuccess() {

    }

    @Override
    public void onCancellationSuccess() {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {

    }

    @Override
    public void onGetDetailinfoSuccess(String classContent, String problemContent, String homeworkContent, String nextContent, String attachmentUrl, String videoUrl) {

    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onselectsuspendedSuccess(int selectsuspended, int positions) {

    }

    @Override
    public void onSuspendedSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    /**
     * 进入房间
     *
     * @param roomIdStr
     */
    private void enterRoom(final String roomIdStr, String name, String userId, String headImg) {
        LiveRoomManager.getInstance().getGroupInfo(roomIdStr, new LiveRoomManager.GetGroupInfoCallback() {
            @Override
            public void onSuccess(V2TIMGroupInfoResult result) {
                if (isRoomExist(result)) {
                    realEnterRoom(roomIdStr, name, userId, headImg);
                } else {
                    ToastUtils.showLong("当前房间不存在");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showLong(msg);
            }
        });
    }

    private void realEnterRoom(String roomIdStr, String name, String userId, String headImg) {
        int roomId;
        try {
            roomId = Integer.parseInt(roomIdStr);
        } catch (Exception e) {
            roomId = 10000;
        }
        final int roomId2 = roomId;
        mTRTCLiveRoom.getRoomInfos(Collections.singletonList(roomId), new TRTCLiveRoomCallback.RoomInfoCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveRoomInfo> list) {
                if (0 == code && null != list && !list.isEmpty()) {
                    final TRTCLiveRoomDef.TRTCLiveRoomInfo info = list.get(0);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gotoAudience(roomId2, info.ownerId, info.ownerName, name, userId, headImg);
                        }
                    });
                }
            }
        });
    }

    private void gotoAudience(int roomId, String anchorId, String anchorName, String name, String userId, String headImg) {
        Intent intent = new Intent(mContext, TCAudienceActivity.class);
        intent.putExtra(TCConstants.GROUP_ID, roomId);
        intent.putExtra(TCConstants.USER_ID, userId);
        intent.putExtra(TCConstants.PUSHER_ID, anchorId);
        intent.putExtra(TCConstants.PUSHER_NAME, anchorName);
        intent.putExtra(TCConstants.PUSHER_AVATAR, headImg);
        intent.putExtra(TCConstants.KECHENG_NAME, name);
        startActivity(intent);
    }

    private boolean isRoomExist(V2TIMGroupInfoResult result) {
        if (result == null) {
            return false;
        }
        return result.getResultCode() == 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNum = 1;
        coursePresenter.getOnLineCourseList(pageNum, searchType);
    }
}
