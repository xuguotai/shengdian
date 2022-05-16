package com.tryine.sdgq.common.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.live.activity.ProtocolActivity;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.presenter.RechargPresenter;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的钱包
 *
 * @author: zhangshuaijun
 * @time: 2021-11-19 16:06
 */
public class MyWalletActivity extends BaseActivity implements RechargeView {

    @BindView(R.id.tv_goldenBean)
    TextView tv_goldenBean;
    @BindView(R.id.tv_goldenBeanobtain)
    TextView tv_goldenBeanobtain;
    @BindView(R.id.tv_goldenBeanconp)
    TextView tv_goldenBeanconp;
    @BindView(R.id.tv_silverBean)
    TextView tv_silverBean;
    @BindView(R.id.tv_silverBeanbtain)
    TextView tv_silverBeanbtain;
    @BindView(R.id.tv_silverBeanconp)
    TextView tv_silverBeanconp;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.tv_tx)
    TextView tv_tx;


    RechargPresenter rechargPresenter;

    UserBean userBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyWalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        rechargPresenter.getUserWallet();
    }

    @Override
    protected void init() {
        setWhiteBar();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        rechargPresenter = new RechargPresenter(this);
        rechargPresenter.attachView(this);
        rechargPresenter.getUserWallet();

        if(userBean.getType().equals("3")){
            rl_bg.setBackgroundResource(R.mipmap.ic_wallet_jd_bg_1);
            tv_tx.setVisibility(View.VISIBLE);
        }else{
            rl_bg.setBackgroundResource(R.mipmap.ic_wallet_jd_bg);
            tv_tx.setVisibility(View.GONE);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mywallet;
    }

    @OnClick({R.id.iv_black, R.id.tv_recharge, R.id.tv_detail, R.id.tv_jd_increase, R.id.tv_detail1,R.id.tv_yd_increase,R.id.tv_policy,R.id.tv_tx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_tx://提现
                WithdrawActivity.start(mContext);
                break;
            case R.id.tv_recharge: //充值
                JDRechargeActivity.start(mContext);
                break;
            case R.id.tv_detail: //金豆明细
                JDDetailActivity.start(mContext, 0);
                break;
            case R.id.tv_detail1: //明细
                JDDetailActivity.start(mContext, 1);
                break;
            case R.id.tv_jd_increase: //转增
                JDIncreaseActivity.start(mContext,0);
                break;
            case R.id.tv_yd_increase: //转增
                JDIncreaseActivity.start(mContext,1);
                break;
            case R.id.tv_policy: //说明
                ProtocolActivity.start(mContext,4);
                break;
        }
    }


    @Override
    public void onRechargeSuccess(String param, int payType) {

    }

    @Override
    public void onCreateOrderSuccess(String orderNo) {

    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {

    }

    @Override
    public void onGetUserWalletSuccess(int goldenBean, int goldenBeanobtain, int goldenBeanconp, int silverBean, int silverBeanbtain, int silverBeanconp) {
        tv_goldenBean.setText(goldenBean + "");
        tv_goldenBeanobtain.setText(goldenBeanobtain + "");
        tv_goldenBeanconp.setText(goldenBeanconp + "");
        tv_silverBean.setText(silverBean + "");
        tv_silverBeanbtain.setText(silverBeanbtain + "");
        tv_silverBeanconp.setText(silverBeanconp + "");
    }

    @Override
    public void onGetWalletListSuccess(List<PayRecordBean> payRecordBeanList, int pages) {

    }

    @Override
    public void onGetProportionSuccess(String teaBean, String realStatus, String miniWithdraw, String serviceCharge, String proportion) {

    }


    @Override
    public void onWithdrawSuccess() {

    }

    @Override
    public void onGetProtocolSuccess(String agreement) {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
