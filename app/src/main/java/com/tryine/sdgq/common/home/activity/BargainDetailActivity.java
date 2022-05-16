package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.adapter.BargainDetailAdapter;
import com.tryine.sdgq.common.home.adapter.BargainOrderDetailAdapter;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.presenter.BargainPresenter;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CircleImageView;
import com.tryine.sdgq.view.CountDownTextView;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;
import com.tryine.sdgq.view.roundImageView.RoundImageView;
import com.xuexiang.xqrcode.XQRCode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 帮砍价详情
 *
 * @author: zhangshuaijun
 * @time: 2022-01-17 16:51
 */
public class BargainDetailActivity extends BaseActivity implements BargainView {

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.iv_cover)
    RoundImageView iv_cover;
    @BindView(R.id.tv_couresName)
    TextView tv_couresName;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_ruingPrice)
    TextView tv_ruingPrice;
//    @BindView(R.id.tv_helpPrice)
//    TextView tv_helpPrice;
    @BindView(R.id.tv_bargainPrice)
    TextView tv_bargainPrice;
    @BindView(R.id.tv_endTime)
    TextView tv_endTime;
    @BindView(R.id.rc_data)
    RecyclerView rc_data;

    BargainPresenter bargainPresenter;

    String detailId;

    BargainOrderDetailBean bargainOrderDetailBean;
    BargainDetailAdapter bargainDetailAdapter;

    public static void start(Context context, String detailId) {
        Intent intent = new Intent();
        intent.setClass(context, BargainDetailActivity.class);
        intent.putExtra("detailId", detailId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bargain_detail;
    }

    @Override
    protected void init() {
        setWhiteBar();
        detailId = getIntent().getStringExtra("detailId");
        bargainPresenter = new BargainPresenter(mContext);
        bargainPresenter.attachView(this);
        bargainPresenter.getbargainh5info(detailId);
    }


    private void initViews() {
        if (null != bargainOrderDetailBean) {
            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, bargainOrderDetailBean.getBargainImgUrl(), iv_head);
            GlideEngine.createGlideEngine().loadUserHeadImage(mContext, bargainOrderDetailBean.getAvatar(), iv_cover);
            tv_content.setText(bargainOrderDetailBean.getUserName() + "在圣典钢琴发现一件好物 帮Ta砍到最低价吧~");
            tv_couresName.setText(bargainOrderDetailBean.getBargainName());
            tv_price.setText("原价 ￥" + bargainOrderDetailBean.getPrice());
            tv_ruingPrice.setText(bargainOrderDetailBean.getRuingPrice());
//            tv_helpPrice.setText(bargainOrderDetailBean.getHelpPrice());
            tv_bargainPrice.setText("还能再砍 " + bargainOrderDetailBean.getBargainPrice() + " 元");
            tv_endTime.setText(bargainOrderDetailBean.getEndTime() + " 结束，赶紧帮忙砍价！");


            bargainDetailAdapter = new BargainDetailAdapter(this, bargainOrderDetailBean.getRecordVoList(), bargainOrderDetailBean.getBargainType());
            LinearLayoutManager lin = new LinearLayoutManager(this);
            lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
            rc_data.setLayoutManager(lin);
            rc_data.setAdapter(bargainDetailAdapter);
            rc_data.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                if (null != bargainOrderDetailBean) {
                    if (bargainOrderDetailBean.getUserId().equals(SPUtils.getString(Parameter.USER_ID))) {
                        ToastUtil.toastLongMessage("不能帮自己砍价");
                        return;
                    }
                    bargainPresenter.helpbargain(bargainOrderDetailBean.getOrderId());
                }

                break;
        }
    }

    @Override
    public void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onSaveorderSuccess(String orderId) {

    }

    @Override
    public void onGetBargainOrderDetailBeanSuccess(BargainOrderDetailBean bargainOrderDetailBean) {
        this.bargainOrderDetailBean = bargainOrderDetailBean;
        initViews();

    }

    @Override
    public void onBargainBuySuccess() {
        ToastUtil.toastLongMessage("砍价成功");
        bargainPresenter.getbargainh5info(detailId);
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


}
