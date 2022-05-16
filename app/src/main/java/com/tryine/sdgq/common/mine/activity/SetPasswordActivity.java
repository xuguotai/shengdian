package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.ActivityManager;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.presenter.MinePresenter;
import com.tryine.sdgq.common.mine.view.MineView;
import com.tryine.sdgq.common.user.activity.LoginActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.PromptDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置密码
 *
 * @author: zhangshuaijun
 * @time: 2021-11-19 15:09
 */
public class SetPasswordActivity extends BaseActivity implements MineView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_oldpassword)
    EditText tv_oldpassword;
    @BindView(R.id.tv_password)
    EditText tv_password;
    @BindView(R.id.tv_thepassword)
    EditText tv_thepassword;

    MinePresenter minePresenter;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SetPasswordActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("设置密码");
        minePresenter = new MinePresenter(this);
        minePresenter.attachView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setpassword;
    }


    @OnClick({R.id.iv_black, R.id.ll_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_submit:
                if ("".equals(tv_oldpassword.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入旧密码");
                    return;
                }
                if ("".equals(tv_password.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入新密码");
                    return;
                }
                if ("".equals(tv_thepassword.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入确认密码");
                    return;
                }
                if (!tv_password.getText().toString().equals(tv_thepassword.getText().toString())) {
                    ToastUtil.toastLongMessage("密码不一致");
                    return;
                }

                minePresenter.updatepassword(tv_oldpassword.getText().toString(), tv_password.getText().toString());
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
