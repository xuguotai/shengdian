package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.presenter.CoursePresenter;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课程报名咨询
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class SignUpActivity extends BaseActivity implements CourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_age)
    EditText et_age;
    @BindView(R.id.femalButtonId)
    RadioButton femalButtonId;
    @BindView(R.id.maleButtonId)
    RadioButton maleButtonId;
    @BindView(R.id.et_remake)
    EditText et_remake;
    @BindView(R.id.tv_size)
    TextView tv_size;

    CoursePresenter coursePresenter;
    CourseBean courseBean;

    public static void start(Context context, CourseBean courseBean) {
        Intent intent = new Intent();
        intent.setClass(context, SignUpActivity.class);
        intent.putExtra("courseBean", courseBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_signup;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("报名咨询");
        courseBean = (CourseBean) getIntent().getSerializableExtra("courseBean");

        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);
        coursePresenter.getFicationList();


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

    private void submit() {

        if ("".equals(getTextStr(et_name))) {
            ToastUtil.toastLongMessage("请输入您的姓名");
            return;
        }
        if ("".equals(getTextStr(et_phone))) {
            ToastUtil.toastLongMessage("请输入您的联系方式");
            return;
        }
        if (getTextStr(et_phone).length() != 11) {
            ToastUtil.toastLongMessage("请输入正确的手机号码");
            return;
        }
        if ("".equals(getTextStr(et_age))) {
            ToastUtil.toastLongMessage("请输入您的年龄");
            return;
        }
        if ("".equals(getTextStr(et_remake))) {
            ToastUtil.toastLongMessage("请输入备注");
            return;
        }

        coursePresenter.addCourse(courseBean.getCampusId(), courseBean.getId(), courseBean.getName(), getTextStr(et_name), getTextStr(et_age)
                , getTextStr(et_phone), femalButtonId.isChecked() ? "1" : "0", getTextStr(et_remake));

    }


    @OnClick({R.id.iv_black, R.id.ll_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_submit:
                submit();
                break;
        }
    }

    @Override
    public void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages) {

    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {

    }

    @Override
    public void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList, int pages) {

    }


    @Override
    public void onGetcancelledSuccess(int count, int positions) {

    }

    @Override
    public void onAddCampusSuccess() {
        ToastUtil.toastLongMessage("报名成功，请等待工作人员与您联系");
        finish();

    }

    @Override
    public void onAddCourseDataSuccess() {

    }

    @Override
    public void onCancellationSuccess() {

    }

    @Override
    public void onGetsignatureSuccess(String signature) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl) {

    }

    @Override
    public void onGetDetailinfoSuccess(String classContent, String problemContent, String homeworkContent, String nextContent, String attachmentUrl, String videoUrl) {

    }

    @Override
    public void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList) {

    }

    @Override
    public void onselectsuspendedSuccess(int selectsuspended, int positions) {

    }

    @Override
    public void onSuspendedSuccess() {

    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);

    }
}
