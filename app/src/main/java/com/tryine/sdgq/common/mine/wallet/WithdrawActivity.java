package com.tryine.sdgq.common.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.presenter.RechargPresenter;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.view.dialog.VerifyPayPwdDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-02-23 13:19
 */
public class WithdrawActivity extends BaseActivity implements RechargeView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.et_price)
    EditText et_price;

    @BindView(R.id.rb_wx)
    RadioButton rb_wx;
    @BindView(R.id.rb_zfb)
    RadioButton rb_zfb;
    @BindView(R.id.tv_title1)
    TextView tv_title1;
    @BindView(R.id.tv_title2)
    TextView tv_title2;

    RechargPresenter rechargPresenter;

    private IWXAPI api;

    String teaBean;
    String realStatus;
    int miniWithdraw;
    int serviceCharge;
    int proportion;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WithdrawActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("提现");
        regToWx();
        rechargPresenter = new RechargPresenter(this);
        rechargPresenter.attachView(this);
        rechargPresenter.getAgreement(3);

    }


    /**
     * 微信登录授权
     */
    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Parameter.WX_APP_ID, false);
        api.registerApp(Parameter.WX_APP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rechargPresenter.proportion();

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        String responseInfo = sp.getString("responseInfo", "");

        if (!responseInfo.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(responseInfo);

                VerifyPayPwdDialog verifyPayPwdDialog = new VerifyPayPwdDialog(mContext, R.style.dialog_center);
                verifyPayPwdDialog.show();
                verifyPayPwdDialog.setmOnTextSendListener(new VerifyPayPwdDialog.OnTextSendListener() {
                    @Override
                    public void onbackPwd(String password) {
                        rechargPresenter.withdraw("WXAPP", et_price.getText().toString(), serviceCharge + "", jsonObject.optString("openid"), password);
                    }

                    @Override
                    public void dismiss() {

                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }
    }


    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                if ("".equals(et_price.getText().toString())) {
                    ToastUtil.toastShortMessage("请输入提现金额");
                    return;
                }

                if (Integer.parseInt(et_price.getText().toString()) < miniWithdraw) {
                    ToastUtil.toastShortMessage(miniWithdraw + "元以上才能提现");
                    return;
                }

                if (rb_wx.isChecked()) {
                    if (!api.isWXAppInstalled()) {
                        ToastUtils.showShort("未安装微信客户端");
                    } else {
                        final SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "wechat_sdk_demo_test";
                        api.sendReq(req);
                    }
                } else {
                    AlipayActivity.start(mContext, et_price.getText().toString(), serviceCharge + "");
                }

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

    }

    @Override
    public void onGetWalletListSuccess(List<PayRecordBean> payRecordBeanList, int pages) {

    }

    @Override
    public void onGetProportionSuccess(String teaBean1, String realStatus, String miniWithdraw, String serviceCharge, String proportion) {
        this.teaBean = String.valueOf((int) Math.floor(Double.parseDouble(teaBean1)));
        this.realStatus = realStatus;
        this.miniWithdraw = Integer.parseInt(miniWithdraw);
        this.serviceCharge = Integer.parseInt(serviceCharge);
        this.proportion = Integer.parseInt(proportion);
        tv_balance.setText(teaBean + "");
        int p = Integer.parseInt(teaBean) / Integer.parseInt(proportion);
        et_price.setHint("可提取金额" + p + "，最少" + miniWithdraw);

        tv_title1.setText("金豆提现比例 " + proportion + ":1，平台将收取" + serviceCharge + "%手续费 当前账户拥有金豆" + teaBean + "，预计可提现金额" + p);


    }

    @Override
    public void onWithdrawSuccess() {
        ToastUtil.toastLongMessage("提现成功请等待审核!");
        finish();
    }

    @Override
    public void onGetProtocolSuccess(String agreement) {
        tv_title2.setText(Html.fromHtml(agreement));
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
