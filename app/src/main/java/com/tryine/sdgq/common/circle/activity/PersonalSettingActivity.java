package com.tryine.sdgq.common.circle.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.bean.LabelBean;
import com.tryine.sdgq.common.circle.bean.PersonalBean;
import com.tryine.sdgq.common.circle.bean.TopicBean;
import com.tryine.sdgq.common.circle.presenter.PersonalPresenter;
import com.tryine.sdgq.common.circle.view.PersonalView;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人主页设置
 *
 * @author: zhangshuaijun
 * @time: 2021-12-02 13:13
 */
public class PersonalSettingActivity extends BaseActivity implements PersonalView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    PersonalPresenter personalPresenter;

    @BindView(R.id.switch_xq)
    Switch switch_xq;
    @BindView(R.id.switch_sx)
    Switch switch_sx;
    @BindView(R.id.et_userExplain)
    EditText et_userExplain;


    PersonalBean personalBean;

    public static void start(Context context, PersonalBean personalBean) {
        Intent intent = new Intent();
        intent.setClass(context, PersonalSettingActivity.class);
        intent.putExtra("personalBean", personalBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_setting;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("个人主页设置");
        personalPresenter = new PersonalPresenter(this);
        personalPresenter.attachView(this);

        personalBean = (PersonalBean) getIntent().getSerializableExtra("personalBean");

        switch_xq.setChecked("0".equals(personalBean.getIsShowLabel()) ? false : true);
        switch_sx.setChecked("0".equals(personalBean.getIsReceive()) ? false : true);
        et_userExplain.setText(personalBean.getUserExplain());

    }


    @OnClick({R.id.iv_black, R.id.rl_xqbq, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.rl_xqbq:
                LabelListActivity.start(mContext);
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        personalPresenter.updateUserInfo(et_userExplain.getText().toString(), switch_xq.isChecked() ? "1" : "0", switch_sx.isChecked() ? "1" : "0");
    }


    @Override
    public void onGetPersonaBeanSuccess(PersonalBean personalBean) {

    }

    @Override
    public void onGetLabelListSuccess(List<LabelBean> labelBeanList) {

    }

    @Override
    public void onUpdateUserInfoSuccess() {
        ToastUtil.toastLongMessage("修改成功");
        finish();
    }

    @Override
    public void onUpdateLabelSuccess() {

    }

    @Override
    public void onFocusSuccess() {

    }

    @Override
    public void onDeletePyqSuccess() {

    }

    @Override
    public void onGetCircleBeanListSuccess(List<CircleBean> circleBeanList, int pages) {

    }

    @Override
    public void onGetTopicBeanListSuccess(List<TopicBean> topicBeanList, int pages) {

    }

    @Override
    public void onGetVideoBeanListSuccess(List<VideoModel> videoModels, int pages) {

    }

    @Override
    public void onGiveSuccess(String type, int position) {

    }

    @Override
    public void onFailed(String message) {

    }
}
