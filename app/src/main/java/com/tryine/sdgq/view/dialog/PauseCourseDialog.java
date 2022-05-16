package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.util.ToastUtil;


public class PauseCourseDialog extends Dialog implements View.OnClickListener {

    LinearLayout ll_jd, ll_yd;
    EditText et_suspendedNum;
    TextView tv_jd, tv_yd, tv_insure, tv_cancel;
    View view;

    RadioButton rb_jd, rb_yd;

    int selectsuspended;
    CourseBean courseBean;


    public PauseCourseDialog(Context context, int selectsuspended, CourseBean courseBean) {
        super(context, R.style.ActionSheetDialogStyle);
        this.selectsuspended = selectsuspended;
        this.courseBean = courseBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_pause);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        et_suspendedNum = findViewById(R.id.tv_suspendedNum);
        et_suspendedNum.setHint("请输入暂停天数(最大" + courseBean.getSuspendedMaxsum() + "天)");

        tv_jd = findViewById(R.id.tv_jd);
        tv_yd = findViewById(R.id.tv_yd);
        ll_jd = findViewById(R.id.ll_jd);
        ll_yd = findViewById(R.id.ll_yd);
        rb_jd = findViewById(R.id.rb_jd);
        rb_yd = findViewById(R.id.rb_yd);

        tv_insure = findViewById(R.id.tv_insure);
        tv_cancel = findViewById(R.id.tv_cancel);

        if (selectsuspended <= 3) {//是否能银豆暂停
            ll_yd.setVisibility(View.VISIBLE);
            ll_jd.setVisibility(View.GONE);
        } else {
            ll_yd.setVisibility(View.GONE);
            ll_jd.setVisibility(View.VISIBLE);
        }

        tv_jd.setText(courseBean.getSuspendedGoldBean() + "金豆可暂停一天");
        tv_yd.setText(courseBean.getSuspendedSilverBean() + "银豆可暂停一天");

        ll_jd.setOnClickListener(this);
        ll_yd.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_insure.setOnClickListener(this);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void suspended(String id, String beanType, String beanNumber, String suspendedNum);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.ll_jd:
                rb_jd.setChecked(true);
                rb_yd.setChecked(false);
                break;
            case R.id.ll_yd:
                rb_jd.setChecked(false);
                rb_yd.setChecked(true);
                break;
            case R.id.tv_insure:
                if (TextUtils.isEmpty(et_suspendedNum.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入暂停天数");
                    return;
                }

                if (Integer.parseInt(et_suspendedNum.getText().toString()) > courseBean.getSuspendedMaxsum()) {
                    ToastUtil.toastLongMessage("最大可暂停" + courseBean.getSuspendedMaxsum() + "天");
                    return;
                }

                if (null != onItemClickListener)
                    onItemClickListener.suspended(courseBean.getId(), rb_jd.isChecked() ? "0" : "1", rb_jd.isChecked() ? courseBean.getSuspendedGoldBean() + "" : courseBean.getSuspendedSilverBean() + "", et_suspendedNum.getText().toString());
                dismiss();
                break;
        }

    }
}
