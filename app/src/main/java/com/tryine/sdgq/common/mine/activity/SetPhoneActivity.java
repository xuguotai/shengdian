package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.presenter.MinePresenter;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.util.Utils;
import com.tryine.sdgq.view.CountDownTimerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改手机号码
 *
 * @author: zhangshuaijun
 * @time: 2021-11-19 15:09
 */
public class SetPhoneActivity extends BaseActivity implements MineView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_newphone)
    EditText tv_newphone;
    @BindView(R.id.et_verifityCode)
    EditText et_verifityCode;
    @BindView(R.id.tv_verifityCode)
    TextView tv_verifityCode;

    MinePresenter minePresenter;

    CountDownTimerView countDownTimerView;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SetPhoneActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("修改手机号码");
        minePresenter = new MinePresenter(this);
        minePresenter.attachView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setphone;
    }


    @OnClick({R.id.iv_black, R.id.ll_submit, R.id.tv_verifityCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_verifityCode:
                if ("".equals(tv_newphone.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入新的手机号码");
                    return;
                }
                if (getTextStr(tv_newphone).length() != 11) {
                    ToastUtil.toastLongMessage("请输入正确的手机号码");
                    return;
                }
                if (Utils.isFastClick()) {
                    minePresenter.getCode(getTextStr(tv_newphone));
                }

                break;
            case R.id.ll_submit:
                if ("".equals(getTextStr(tv_newphone))) {
                    ToastUtil.toastLongMessage("请输入手机号码");
                    return;
                }
                if (getTextStr(tv_newphone).length() != 11) {
                    ToastUtil.toastLongMessage("请输入正确的手机号码");
                    return;
                }
                if ("".equals(getTextStr(et_verifityCode))) {
                    ToastUtil.toastLongMessage("请输入验证码");
                    return;
                }
                UserBean userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
                minePresenter.updatemobile(userBean.getMobile(), getTextStr(tv_newphone), getTextStr(et_verifityCode));
                break;
        }
    }

    @Override
    public void onGetUserdetailSuccess(UserBean userBean) {

    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList) {

    }

    @Override
    public void onGetFansBeanSuccess(List<FansBean> fansBeanList, int pages) {

    }

    @Override
    public void onFocusSuccess() {

    }

    @Override
    public void onUpdatepasswordSuccess() {
        ToastUtil.toastLongMessage("修改成功");
        finish();
    }

    @Override
    public void onCodeSuccess() {
        countDownTimerView = new CountDownTimerView(tv_verifityCode, "#FFFFFF", 60000, 1000);
        countDownTimerView.start();
    }

    @Override
    public void onGetIsLiveSuccess(int isLive, int realStatus, int liveId, String trtcPushAddr) {

    }

    @Override
    public void onGetaboutinfoSuccess(String rewardDesc) {

    }


    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
