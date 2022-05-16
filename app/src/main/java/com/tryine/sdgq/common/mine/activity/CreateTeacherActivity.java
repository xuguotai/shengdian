package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.mine.presenter.TeacherPresenter;
import com.tryine.sdgq.common.mine.view.TeacherView;
import com.tryine.sdgq.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成为老师
 *
 * @author: zhangshuaijun
 * @time: 2021-12-01 16:20
 */
public class CreateTeacherActivity extends BaseActivity implements TeacherView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_educationLayer)
    EditText et_educationLayer;
    @BindView(R.id.et_graduateSchool)
    EditText et_graduateSchool;
    @BindView(R.id.et_remake)
    EditText et_remake;
    @BindView(R.id.tv_size)
    TextView tv_size;

    TeacherPresenter teacherPresenter;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CreateTeacherActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_createteacher;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("成为老师");
        teacherPresenter = new TeacherPresenter(this);
        teacherPresenter.attachView(this);

        et_remake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tv_size.setText(charSequence.length() + "/300");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.iv_black, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }


    private void submit() {
        if ("".equals(getTextStr(et_name))) {
            ToastUtil.toastLongMessage("请输入您的姓名");
            return;
        }
        if (getTextStr(et_phone).length() != 11) {
            ToastUtil.toastLongMessage("请输入联系方式");
            return;
        }
        if ("".equals(getTextStr(et_educationLayer))) {
            ToastUtil.toastLongMessage("请输入工作年限");
            return;
        }
        if ("".equals(getTextStr(et_graduateSchool))) {
            ToastUtil.toastLongMessage("请输入毕业院校");
            return;
        }

        teacherPresenter.addTeacher(getTextStr(et_name), getTextStr(et_educationLayer), getTextStr(et_graduateSchool),getTextStr(et_phone), getTextStr(et_remake));
    }


    @Override
    public void onAddTeacherSuccess() {
        ToastUtil.toastLongMessage("申请成功，请等待审核");
        finish();

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
