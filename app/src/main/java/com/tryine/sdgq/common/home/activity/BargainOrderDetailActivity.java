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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.ActivityManager;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.home.adapter.BargainOrderDetailAdapter;
import com.tryine.sdgq.common.home.adapter.HomeBargainAdapter;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.BargainOrderDetailBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.presenter.BargainPresenter;
import com.tryine.sdgq.common.home.view.BargainView;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;
import com.tryine.sdgq.common.user.activity.LoginActivity;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.DateTimeHelper;
import com.tryine.sdgq.util.GlideEngine;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.CountDownTextView;
import com.tryine.sdgq.view.dialog.PromptDialog;
import com.tryine.sdgq.view.dialog.ShareDialog;
import com.tryine.sdgq.view.roundImageView.RoundImageView;
import com.xuexiang.xqrcode.XQRCode;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 砍价订单详情
 *
 * @author: zhangshuaijun
 * @time: 2022-01-17 16:51
 */
public class BargainOrderDetailActivity extends BaseActivity implements BargainView {

    @BindView(R.id.iv_cover)
    RoundImageView iv_cover;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.countdown_text_view)
    CountDownTextView countdown_text_view;
    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_rulingPrice)
    TextView tv_rulingPrice;
    @BindView(R.id.tv_bargainPrice)
    TextView tv_bargainPrice;

    @BindView(R.id.progesss)
    ProgressBar progesss;

    @BindView(R.id.rc_data)
    RecyclerView rc_data;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    @BindView(R.id.tv_orderNo)
    TextView tv_orderNo;
    @BindView(R.id.tv_hxm)
    TextView tv_hxm;
    @BindView(R.id.iv_qrCode)
    ImageView iv_qrCode;
    @BindView(R.id.tv_jxyq)
    TextView tv_jxyq;
    @BindView(R.id.tv_buy)
    TextView tv_buy;


    BargainPresenter bargainPresenter;
    BargainOrderDetailAdapter bargainOrderDetailAdapter;

    BargainOrderDetailBean bargainOrderDetailBean;

    String detailId;

    public static void start(Context context, String detailId) {
        Intent intent = new Intent();
        intent.setClass(context, BargainOrderDetailActivity.class);
        intent.putExtra("detailId", detailId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bargain_orderdetail;
    }

    @Override
    protected void init() {
        setWhiteBar();
        detailId = getIntent().getStringExtra("detailId");
        bargainPresenter = new BargainPresenter(mContext);
        bargainPresenter.attachView(this);
        bargainPresenter.getBargainOrderdetail(detailId);
    }

    private void initDetailViews(BargainOrderDetailBean bargainOrderDetailBean) {
        GlideEngine.createGlideEngine().loadImage(mContext, bargainOrderDetailBean.getBargainImgUrl(), iv_cover);
        tv_title.setText(bargainOrderDetailBean.getBargainName());
        if (bargainOrderDetailBean.getBargainType().equals("0")) {
            tv_price.setText(bargainOrderDetailBean.getCostPrice() + " SD金豆");
            tv_bargainPrice.setText(" 金豆     还可砍 " + bargainOrderDetailBean.getBargainPrice() + " 金豆");
            tv_buy.setVisibility(View.VISIBLE);
        } else if (bargainOrderDetailBean.getBargainType().equals("1")){
            tv_price.setText(bargainOrderDetailBean.getCostPrice() + " 元");
            tv_bargainPrice.setText(" 元     还可砍 " + bargainOrderDetailBean.getBargainPrice() + " 元");
            tv_buy.setVisibility(View.GONE);
        }else if (bargainOrderDetailBean.getBargainType().equals("2")){
            tv_price.setText(bargainOrderDetailBean.getCostPrice() + " 元");
            tv_bargainPrice.setText(" 元     还可砍 " + bargainOrderDetailBean.getBargainPrice() + " 元");
            tv_buy.setVisibility(View.VISIBLE);
        }


        float f = Float.valueOf(DateTimeHelper.getSurplusTime(bargainOrderDetailBean.getBargainEndTime()));
        countdown_text_view.setMaxTime(Math.round(f));
        countdown_text_view.setmFinishText("砍价已结束");
        countdown_text_view.startCountDown();
        countdown_text_view.setishour(true);


        tv_rulingPrice.setText(bargainOrderDetailBean.getRulingPrice());


        progesss.setMax(Integer.parseInt(bargainOrderDetailBean.getCostPrice()));
        progesss.setProgress(Integer.parseInt(bargainOrderDetailBean.getRulingPrice()));


        if (null != bargainOrderDetailBean.getRecordVoList() && bargainOrderDetailBean.getRecordVoList().size() > 0) {
            bargainOrderDetailAdapter = new BargainOrderDetailAdapter(this, bargainOrderDetailBean.getRecordVoList(), bargainOrderDetailBean.getBargainType());
            LinearLayoutManager lin = new LinearLayoutManager(this);
            lin.setOrientation(RecyclerView.VERTICAL);//选择竖直列表
            rc_data.setLayoutManager(lin);
            rc_data.setAdapter(bargainOrderDetailAdapter);
            rc_data.setVisibility(View.VISIBLE);
            ll_no_data.setVisibility(View.GONE);
        } else {
            rc_data.setVisibility(View.GONE);
            ll_no_data.setVisibility(View.VISIBLE);
        }


        tv_orderNo.setText(bargainOrderDetailBean.getOrderNo());
        if (bargainOrderDetailBean.getStatus().equals("4")) { //订单状态  1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
            tv_jxyq.setVisibility(View.GONE);
            tv_buy.setVisibility(View.GONE);
        } else if (bargainOrderDetailBean.getStatus().equals("3") && !TextUtils.isEmpty(bargainOrderDetailBean.getCheckCode())) { //订单状态  1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
            Bitmap bitmap = XQRCode.createQRCodeWithLogo(bargainOrderDetailBean.getCheckCode(), 250, 250, null);
            iv_qrCode.setImageBitmap(bitmap);
            tv_hxm.setVisibility(View.VISIBLE);
            iv_qrCode.setVisibility(View.VISIBLE);
            tv_jxyq.setVisibility(View.GONE);
            tv_buy.setVisibility(View.GONE);
        } else {
            tv_hxm.setVisibility(View.GONE);
            iv_qrCode.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.iv_black, R.id.tv_jxyq, R.id.tv_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_jxyq:
                if (null != bargainOrderDetailBean && bargainOrderDetailBean.getStatus().equals("1") ||  bargainOrderDetailBean.getStatus().equals("2")) {
                    ShareDialog shareDialog = new ShareDialog(mContext, 1, bargainOrderDetailBean.getId(), "7");
                    shareDialog.show();
                    shareDialog.setShareListener(new ShareDialog.OnShareListener() {
                        @Override
                        public void insure(int shareType, ShareInfoBean bean) {
                            ShareUtils.share(BargainOrderDetailActivity.this, shareType, bean);
                        }
                    });
                }
                break;
            case R.id.tv_buy:
                if (null != bargainOrderDetailBean && bargainOrderDetailBean.getStatus().equals("2")) {
                    bargainPresenter.goBargainBuy(bargainOrderDetailBean.getId());
                    //订单状态  1:砍价中 2:待付款 3:待核销 4:已完成 5:已过期
                } else if (null != bargainOrderDetailBean && bargainOrderDetailBean.getStatus().equals("1")) {
                    ToastUtil.toastLongMessage("订单还在砍价中不能付款，立即分享给小伙伴帮忙砍价!");
                } else if (null != bargainOrderDetailBean && bargainOrderDetailBean.getStatus().equals("1")) {
                    ToastUtil.toastLongMessage("订单还在砍价中不能付款，立即分享给小伙伴帮忙砍价!");
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
        if (null != bargainOrderDetailBean) {
            this.bargainOrderDetailBean = bargainOrderDetailBean;
            initDetailViews(bargainOrderDetailBean);
        }
    }

    @Override
    public void onBargainBuySuccess() {
        ToastUtil.toastLongMessage("购买成功");
        bargainPresenter.getBargainOrderdetail(detailId);
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


}
