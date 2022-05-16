package com.tryine.sdgq.common.mine.wallet;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.ActivityManager;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.mine.activity.SetPasswordActivity;
import com.tryine.sdgq.common.mine.activity.SetPhoneActivity;
import com.tryine.sdgq.common.mine.bean.PayRecordBean;
import com.tryine.sdgq.common.mine.presenter.RechargPresenter;
import com.tryine.sdgq.common.mine.view.RechargeView;
import com.tryine.sdgq.common.user.activity.LoginActivity;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 金豆/银豆转增
 *
 * @author: zhangshuaijun
 * @time: 2021-11-22 11:31
 */
public class JDIncreaseActivity extends BaseActivity implements RechargeView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_balance_title)
    TextView tv_balance_title;
    @BindView(R.id.tv_title1)
    TextView tv_title1;
    @BindView(R.id.tv_phone)
    EditText et_phone;
    @BindView(R.id.et_beanCount)
    EditText et_beanCount;
    @BindView(R.id.tv_title2)
    TextView tv_title2;
    @BindView(R.id.et_remake)
    EditText et_remake;

    RechargPresenter rechargPresenter;

    int beanType = 0;

    public static void start(Context context, int beanType) {
        Intent intent = new Intent();
        intent.setClass(context, JDIncreaseActivity.class);
        intent.putExtra("beanType", beanType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jd_increase;
    }

    @Override
    protected void init() {
        setWhiteBar();

        beanType = getIntent().getIntExtra("beanType", 0);

        rechargPresenter = new RechargPresenter(this);
        rechargPresenter.attachView(this);
        rechargPresenter.getUserbean();

        if (beanType == 0) {
            tv_title.setText("转赠SD金豆");
            tv_balance_title.setText("当前SD金豆余额");
            tv_title1.setText("转赠金豆");
            tv_title2.setText("*转赠金豆平台将收取1%");
            et_beanCount.setHint("请输入转赠金豆数量");
        } else {
            tv_title.setText("转赠SD银豆");
            tv_balance_title.setText("当前SD银豆余额");
            tv_title1.setText("转赠银豆");
            tv_title2.setText("*转赠银豆平台将收取1%");
            et_beanCount.setHint("请输入转赠银豆数量");
        }

        et_beanCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    if (null != charSequence.toString() && Integer.parseInt(charSequence.toString()) >= 100) {
                        int count = Integer.parseInt(charSequence.toString()) / 100;
                        if( Integer.parseInt(charSequence.toString()) % 100 == 0){
                            if (beanType == 0) {
                                tv_title2.setText("*转赠金豆平台将收取1%      手续费" + count + "SD金豆");
                            } else {
                                tv_title2.setText("*转赠银豆平台将收取1%      手续费" + count + "SD银豆");
                            }
                        }else{
                            ToastUtil.toastLongMessage("转出数量必须为100的倍数");
                        }
                    }else{
                        if (beanType == 0) {
                            tv_title2.setText("*转赠金豆平台将收取1%");
                        } else {
                            tv_title2.setText("*转赠银豆平台将收取1%");
                        }
                    }
                }else{
                    if (beanType == 0) {
                        tv_title2.setText("*转赠金豆平台将收取1%");
                    } else {
                        tv_title2.setText("*转赠银豆平台将收取1%");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick({R.id.iv_black, R.id.tv_submit,R.id.ll_gosj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_gosj:
                intentToContact();
                break;
            case R.id.tv_submit:
                if ("".equals(getTextStr(et_phone))) {
                    ToastUtil.toastLongMessage("请输入手机号码");
                    return;
                }

                if (getTextStr(et_phone).length() != 11) {
                    ToastUtil.toastLongMessage("请输入正确的手机号码");
                    return;
                }

                if ("".equals(getTextStr(et_beanCount))) {
                    if (beanType == 0) {
                        ToastUtil.toastLongMessage("请输入转赠金豆数量");
                    } else {
                        ToastUtil.toastLongMessage("请输入转赠银豆数量");
                    }
                    return;
                }

                PromptDialog promptDialog = new PromptDialog(mContext, 0, "提示",
                        "确认转给手机号码为 <font color=\"#FF3F3F\">" + getTextStr(et_phone) + "</font> 的好友", "确认", "取消");
                promptDialog.show();
                promptDialog.setOnItemClickListener(new PromptDialog.OnItemClickListener() {
                    @Override
                    public void insure() {
                        rechargPresenter.turnadd(beanType, getTextStr(et_beanCount), getTextStr(et_phone),getTextStr(et_remake));
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
        }
    }

    private void intentToContact() {
        // 跳转到联系人界面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 0x30);
    }

    @Override
    public void onRechargeSuccess(String param, int payType) {
        ToastUtil.toastLongMessage("转增成功");
        rechargPresenter.getUserbean();
        et_phone.setText("");
        et_beanCount.setText("");
    }

    @Override
    public void onCreateOrderSuccess(String orderNo) {

    }

    @Override
    public void onGetUserbeanSuccess(int goldenBean, int silverBean) {
        if (beanType == 0) {
            tv_balance.setText(goldenBean + "");
        } else {
            tv_balance.setText(silverBean + "");
        }

    }

    @Override
    public void onGetUserWalletSuccess(int goldenBean, int goldenBeanobtain, int goldenBeanconp, int silverBean, int silverBeanbtain, int silverBeanconp) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x30) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri,
                            new String[]{"display_name", "data1"}, null, null, null);
                }
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum = phoneNum.replaceAll(" ", "");
                }

                et_phone.setText(phoneNum + "");
            }
        }
    }

}
