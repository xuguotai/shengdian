package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.PersonalHomePageActivity;
import com.tryine.sdgq.common.circle.activity.VideoPlayFullScreenActivity;
import com.tryine.sdgq.common.home.activity.BargainOrderDetailActivity;
import com.tryine.sdgq.common.home.activity.TeacherDetailHomeActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.activity.push.Constants;
import com.tryine.sdgq.common.live.activity.push.LivePlayerMainActivity;
import com.tryine.sdgq.common.live.adapter.LiveChapterAdapter;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.bean.LiveCourseDateBean;
import com.tryine.sdgq.common.live.bean.LiveCourseDetailBean;
import com.tryine.sdgq.common.live.presenter.LivePresenter;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.LiveRoomManager;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoom;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.audience.TCAudienceActivity;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.common.utils.TCConstants;
import com.tryine.sdgq.common.live.view.LiveView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.dialog.BuyLiveCourseDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;

import org.json.JSONArray;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播课详情
 *
 * @author: zhangshuaijun
 * @time: 2022-01-04 11:30
 */
public class LiveCourseDetailActivity extends BaseActivity implements LiveView {

    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.tv_signUpCount)
    TextView tv_signUpCount;
    @BindView(R.id.tv_goldBean)
    TextView tv_goldBean;
    @BindView(R.id.tv_startTime)
    TextView tv_startTime;

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_teacherName)
    TextView tv_teacherName;
    @BindView(R.id.tv_experience)
    TextView tv_experience;
    @BindView(R.id.tv_course)
    TextView tv_course;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_teacherRemark)
    TextView tv_teacherRemark;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.tv_couresName)
    TextView tv_couresName;
    @BindView(R.id.tv_courseType)
    TextView tv_courseType;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    LivePresenter livePresenter;

    LiveCourseBean liveCourseBean;
    LiveChapterAdapter chapterAdapter;

    String id;

    String liveRoomId; //直播间id

    UserBean userBean;

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, LiveCourseDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_coursedetail;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        livePresenter.getLivecoursedetail(id);
    }

    @Override
    protected void init() {
        setWhiteBar();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        id = getIntent().getStringExtra("id");
        livePresenter = new LivePresenter(mContext);
        livePresenter.attachView(this);
        livePresenter.getLivecoursedetail(id);
    }

    private void initViews() {
        if (null != liveCourseBean) {
            GlideEngine.createGlideEngine().loadImage(liveCourseBean.getImgUrl(), iv_cover);
            tv_goldBean.setText(liveCourseBean.getGoldBean() + " 金豆解锁");
            tv_couresName.setText(liveCourseBean.getName());
            tv_signUpCount.setText("已报名 " + liveCourseBean.getSignUpCount() + " 人");
            if (liveCourseBean.getCourseType().equals("1")) {
                tv_startTime.setText("购买选择老师上课时间");
            } else {
                tv_startTime.setText(liveCourseBean.getStartTime());
            }

            tv_courseType.setVisibility(liveCourseBean.getCourseType().equals("1") ? View.VISIBLE : View.GONE);

            chapterAdapter = new LiveChapterAdapter(mContext, liveCourseBean.getDetailVoList());
            LinearLayoutManager lin = new LinearLayoutManager(mContext);
            lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
            rv_list.setLayoutManager(lin);
            rv_list.setAdapter(chapterAdapter);
            chapterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    LiveCourseDetailBean detailBean = liveCourseBean.getDetailVoList().get(position);
                    if (null != detailBean.getRoomStatus() && detailBean.getRoomStatus().equals("1") && detailBean.getIsBuy().equals("1")) {

                        if (!TextUtils.isEmpty(detailBean.getLiveId()) && null != liveCourseBean) {
                            Intent intent = new Intent(mContext, LivePlayerMainActivity.class);
                            intent.putExtra(TCConstants.GROUP_ID, Integer.parseInt(detailBean.getLiveId()));
                            startActivity(intent);
                        }

                    } else if (null != detailBean.getRoomStatus() && detailBean.getRoomStatus().equals("3") && detailBean.getIsBuy().equals("1")) {
                        if (!TextUtils.isEmpty(detailBean.getRecordUrl())) {
                            VideoPlayFullScreenActivity.start(mContext, detailBean.getRecordUrl());
                        } else {
                            ToastUtil.toastLongMessage("回放地址错误");
                        }

                    }

                }
            });

            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, liveCourseBean.getTeacherHeadImg(), iv_head);
            tv_teacherName.setText(liveCourseBean.getTeacherName());
            tv_experience.setText("教龄" + liveCourseBean.getExperience() + "年");
            tv_course.setText(liveCourseBean.getCourse());
            tv_title.setText(liveCourseBean.getTitle());
            tv_teacherRemark.setText(liveCourseBean.getTeacherRemark());

            for (LiveCourseDetailBean data : liveCourseBean.getDetailVoList()) {

                if (null != data.getRoomStatus() && data.getRoomStatus().equals("1") || null != data.getRoomStatus() && data.getRoomStatus().equals("2")) {
                    liveRoomId = data.getLiveId();
                }

            }

            if (liveCourseBean.getIsBuy().equals("1")) {
                tv_buy.setVisibility(View.GONE);
            } else {
                tv_buy.setVisibility(View.VISIBLE);
            }

        }

    }

    @OnClick({R.id.iv_black, R.id.tv_buy, R.id.iv_head, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_buy:
                if (null != liveCourseBean) {
                    if (!liveCourseBean.getUserId().equals(SPUtils.getString(Parameter.USER_ID))) {
                        if (liveCourseBean.getCourseType().equals("1")) {
                            if (liveCourseBean.getIsBuy().equals("0") && liveCourseBean.getIsSatisfy().equals("0")) {
                                BuyLiveCourseDialog buyLiveCourseDialog = new BuyLiveCourseDialog(LiveCourseDetailActivity.this, liveCourseBean.getDetailOneVoList());
                                buyLiveCourseDialog.show();
                                buyLiveCourseDialog.setOnItemClickListener(new BuyLiveCourseDialog.OnItemClickListener() {
                                    @Override
                                    public void resultReason(LiveCourseDateBean liveCourseDateBean) {
                                        JSONArray ids = new JSONArray();
                                        ids.put(liveCourseDateBean.getId());
                                        livePresenter.buyLivecourse(id, userBean.getMobile(), ids);
                                    }
                                });
                            } else {
                                ToastUtil.toastLongMessage("课程已满员");
                            }

                        } else { //直播大课购买
                            LiveCourseBuyDetailActivity.start(mContext, id);
                        }

                    } else {
                        ToastUtil.toastLongMessage("不能购买自己的课程");
                    }
                }

                break;
            case R.id.iv_head:
                if (null != liveCourseBean) {
                    if (liveCourseBean.getTeacherType() == 0) {
                        TeacherDetailHomeActivity.start(mContext, liveCourseBean.getTeacherId(),
                                liveCourseBean.getTeacherType(),
                                liveCourseBean.getUserId(),
                                liveCourseBean.getTeacherHeadImg(),
                                liveCourseBean.getTeacherName());
                    } else {
                        TeacherDetailHomeActivity.start(mContext, liveCourseBean.getOfflineTeacherId(),
                                liveCourseBean.getTeacherType(),
                                liveCourseBean.getUserId(),
                                liveCourseBean.getTeacherHeadImg(),
                                liveCourseBean.getTeacherName());
                    }

                }
                break;
            case R.id.iv_share:
                if (null != liveCourseBean) {
                    ShareDialog shareDialog = new ShareDialog(mContext, 1, liveCourseBean.getId(), "4");
                    shareDialog.show();
                    shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                        @Override
                        public void insure(int shareType, ShareInfoBean bean) {
                            ShareUtils.share(LiveCourseDetailActivity.this, shareType, bean);
                        }
                    });
                }
                break;
        }
    }


    @Override
    public void onLiveCourseBeanSuccess(LiveCourseBean liveCourseBean) {
        this.liveCourseBean = liveCourseBean;
        initViews();
    }

    @Override
    public void onGetCourseBeanSuccess(List<HomeMenuBean> courseBeanList) {

    }

    @Override
    public void onGetCourseChildBeanSuccess(List<HomeMenuBean> courseChildBeanList) {

    }

    @Override
    public void onGetliveroomdetailSuccess(int liveId, String trtcPushAddr) {

    }

    @Override
    public void onBuyCourseSuccess() {
        ToastUtil.toastLongMessage("预约成功");
        livePresenter.getLivecoursedetail(id);
    }

    @Override
    public void onAddroomSuccess(int mRoomId) {

    }

    @Override
    public void onGetIsLiveSuccess(int isLive, int realStatus, int liveId, String trtcPushAddr) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    private boolean isRoomExist(V2TIMGroupInfoResult result) {
        if (result == null) {
            return false;
        }
        return result.getResultCode() == 0;
    }


}
