package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.live.adapter.ChapterAdapter;
import com.tryine.sdgq.common.live.bean.LiveCourseBean;
import com.tryine.sdgq.common.live.bean.LiveCourseDetailBean;
import com.tryine.sdgq.common.live.presenter.LivePresenter;
import com.tryine.sdgq.common.live.view.LiveView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播课详情
 *
 * @author: zhangshuaijun
 * @time: 2022-01-04 11:30
 */
public class LiveCourseBuyDetailActivity extends BaseActivity implements LiveView {

    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.tv_teacherName)
    TextView tv_teacherName;
    @BindView(R.id.tv_price)
    TextView tv_price;


    @BindView(R.id.tv_totalPayPrice)
    TextView tv_totalPayPrice;
    @BindView(R.id.tv_payPrice)
    TextView tv_payPrice;


    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    LivePresenter livePresenter;

    LiveCourseBean liveCourseBean;
    ChapterAdapter liveChapterAdapter;

    String id;
    String liveRoomId;

    UserBean userBean;

    List<LiveCourseDetailBean> liveCourseDetailBeanList = new ArrayList<>();


    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, LiveCourseBuyDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_coursedetail1;
    }

    @Override
    protected void init() {
        setWhiteBar();
        id = getIntent().getStringExtra("id");
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        livePresenter = new LivePresenter(mContext);
        livePresenter.attachView(this);
        livePresenter.getLivecoursedetail(id);
    }

    private void initViews() {
        if (null != liveCourseBean) {
            GlideEngine.createGlideEngine().loadImage(liveCourseBean.getImgUrl(), iv_cover);

            tv_price.setText(liveCourseBean.getGoldBean() + " 金豆");
            tv_teacherName.setText(liveCourseBean.getName());

            liveCourseDetailBeanList.clear();

            for (LiveCourseDetailBean liveCourseDetailBean : liveCourseBean.getDetailVoList()) {
                if (liveCourseDetailBean.getIsBuy().equals("0")) {
                    liveCourseDetailBeanList.add(liveCourseDetailBean);
                }
            }

            liveChapterAdapter = new ChapterAdapter(mContext, liveCourseDetailBeanList);
            LinearLayoutManager lin = new LinearLayoutManager(mContext);
            lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
            rv_list.setLayoutManager(lin);
            rv_list.setAdapter(liveChapterAdapter);

            liveChapterAdapter.setOnItemCheckedChangeListener(new ChapterAdapter.OnItemCheckedChangeListener() {
                @Override
                public void onCheckedChange(boolean isChecked, int position) {
                    liveCourseDetailBeanList.get(position).setChecked(isChecked);
                    calculatePrice();
                }
            });

            calculateAllPrice();

        }

    }

    private void calculatePrice() {

        double total_price = 0;

        for (LiveCourseDetailBean liveCourseDetailBean : liveCourseDetailBeanList) {
            if (liveCourseDetailBean.isChecked()) {
                total_price = total_price + Double.parseDouble(liveCourseDetailBean.getGoldBean());
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0");
        tv_payPrice.setText(decimalFormat.format(total_price) + " 金豆");
    }


    /**
     * 计算全部的价格
     */
    private void calculateAllPrice() {

        double total_price = 0;

        for (LiveCourseDetailBean liveCourseDetailBean : liveCourseDetailBeanList) {
            total_price = total_price + Double.parseDouble(liveCourseDetailBean.getGoldBean());
        }
        DecimalFormat decimalFormat = new DecimalFormat("0");
        tv_totalPayPrice.setText(decimalFormat.format(total_price) + " 金豆");
    }


    @OnClick({R.id.iv_black, R.id.ll_sBuy, R.id.ll_allBuy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_allBuy:

                allShowPromptDialog();
                break;
            case R.id.ll_sBuy:

                showPromptDialog();
                break;
//            case R.id.tv_look:
//                if (!TextUtils.isEmpty(liveRoomId) && null != liveCourseBean) {
//                    enterRoom(liveRoomId);
//                }
//                break;
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
        ToastUtil.toastLongMessage("购买成功");
        finish();
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


    private void showPromptDialog() {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                "确认购买当前选中直播课程", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                JSONArray ids = new JSONArray();
                for (LiveCourseDetailBean liveCourseDetailBean : liveCourseDetailBeanList) {
                    if (liveCourseDetailBean.isChecked()) {
                        ids.put(liveCourseDetailBean.getId());
                    }
                }
                livePresenter.buyLivecourse(id, userBean.getMobile(), ids);
            }

            @Override
            public void cancel() {

            }
        });
    }

    /**
     * 购买全部
     */
    private void allShowPromptDialog() {
        PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                "确认购买当前全部直播课程", "确认", "取消");
        promptDialog.show();
        promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
            @Override
            public void insure() {
                JSONArray ids = new JSONArray();
                for (LiveCourseDetailBean liveCourseDetailBean : liveCourseDetailBeanList) {
                    ids.put(liveCourseDetailBean.getId());
                }
                livePresenter.buyLivecourse(id, userBean.getMobile(), ids);
            }

            @Override
            public void cancel() {

            }
        });
    }

}
