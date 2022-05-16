package com.tryine.sdgq.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CampusBean;

import java.util.List;

import butterknife.BindView;


public class ReasonDialog extends Dialog {
    private Activity mContext;

    LinearLayout ll_type;
    ImageView iv_close;

    RadioButton rb_1;
    RadioButton rb_2;
    RadioButton rb_3;

    LinearLayout ll_submit;


    public ReasonDialog(Activity context) {
        super(context, R.style.ActionSheetDialogStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reasontype);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        ll_submit = findViewById(R.id.ll_submit);

        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reasonStr = "";
                if (rb_1.isChecked()) {
                    reasonStr = "收货地址填错了";
                } else if (rb_2.isChecked()) {
                    reasonStr = "不想买了";
                } else if (rb_3.isChecked()) {
                    reasonStr = "其他原因";
                }

                if (null != onItemClickListener) {
                    onItemClickListener.resultReason(reasonStr);
                }
                dismiss();
            }
        });

    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void resultReason(String reason);
    }

}
