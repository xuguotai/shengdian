package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.tryine.sdgq.view.StarBarView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课程评价
 *
 * @author: zhangshuaijun
 * @time: 2021-11-24 10:32
 */
public class CourseCommentActivity extends BaseActivity implements CourseView {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.sb_lxs)
    StarBarView sb_lxs;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.cb_agree)
    CheckBox cb_agree;

    CoursePresenter coursePresenter;

    String courseId;

    public static void start(Context context, String courseId) {
        Intent intent = new Intent();
        intent.setClass(context, CourseCommentActivity.class);
        intent.putExtra("courseId", courseId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_comment;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("课程评价");

        courseId = getIntent().getStringExtra("courseId");
        coursePresenter = new CoursePresenter(this);
        coursePresenter.attachView(this);

        sb_lxs.setMarkType(1);

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_size.setText(charSequence.length() + "/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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

    private void submit() {
        if ("".equals(getTextStr(et_content))) {
            ToastUtil.toastLongMessage("请输入您的评价内容");
            return;
        }
        coursePresenter.evaluation(courseId, getTextStr(et_content), sb_lxs.getStarMark() + "", cb_agree.isChecked() ? "1" : "0");
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
        ToastUtil.toastLongMessage("评价成功");
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
