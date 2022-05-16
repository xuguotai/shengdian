package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.bean.ReportTypeBean;
import com.tryine.sdgq.common.circle.presenter.ReportPresenter;
import com.tryine.sdgq.common.circle.view.ReportView;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.CampusDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity implements ReportView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.tv_xq)
    TextView tv_xq;

    ReportPresenter reportPresenter;

    List<CampusBean> campusBeanLists = new ArrayList<>(); //校区
    CampusBean selectCampusBean;//选中的校区

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("投诉建议");
        reportPresenter = new ReportPresenter(this);
        reportPresenter.attachView(this);
        reportPresenter.getFicationList();

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tv_size.setText(charSequence.length() + "/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.iv_black, R.id.tv_submit, R.id.ll_xq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_submit:
                String content = et_content.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();

                if (TextUtils.isEmpty(content)) {
                    ToastUtil.toastLongMessage("请输入您的投诉或建议");
                    return;
                }

                if (phone.length() == 0 || phone.length() < 11 || !phone.startsWith("1")) {
                    ToastUtil.toastLongMessage("请输入正确的手机号码");
                    return;
                }

                if (null != selectCampusBean) {
                    reportPresenter.usercomplain(content, phone, selectCampusBean.getId());
                } else {
                    reportPresenter.usercomplain(content, phone, "");
                }


                break;
            case R.id.ll_xq:
                if (null != campusBeanLists) {
                    CampusDialog campusDialog = new CampusDialog(FeedbackActivity.this, campusBeanLists);
                    campusDialog.show();
                    campusDialog.setOnItemClickListener(new CampusDialog.OnItemClickListener() {
                        @Override
                        public void resultReason(CampusBean homeMenuBean) {
                            selectCampusBean = homeMenuBean;
                            tv_xq.setText(homeMenuBean.getName());
                        }
                    });
                }

                break;

        }
    }

    @Override
    public void onGetReportTypeListSuccess(List<ReportTypeBean> reportTypeBeans) {

    }

    @Override
    public void onUploadFileSuccess(String fileUrl, int type) {

    }

    @Override
    public void onReportSuccess() {

    }

    @Override
    public void onUserComplainSuccess() {
        ToastUtil.toastLongMessage("提交成功");
        finish();
    }

    @Override
    public void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList) {
        this.campusBeanLists = campusBeanList;
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }
}
