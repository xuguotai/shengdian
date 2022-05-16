package com.tryine.sdgq.view.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.user.activity.PayPasswordActivity;
import com.tryine.sdgq.util.ToastUtil;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;


/**
 * 验证支付密码
 */
public class VerifyPayPwdDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private ImageView iv_close;
    private VerifyCodeEditText et_payPassword;
    private TextView tv_wjmm;

    public interface OnTextSendListener {
        void onbackPwd(String password);

        void dismiss();
    }

    private OnTextSendListener mOnTextSendListener;

    public VerifyPayPwdDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        init();
        setLayout();
    }


    private void init() {
        setContentView(R.layout.dialog_verifypaypwd);
        et_payPassword = (VerifyCodeEditText) findViewById(R.id.et_payPassword);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        tv_wjmm = (TextView) findViewById(R.id.tv_wjmm);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        tv_wjmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayPasswordActivity.start(mContext);
            }
        });

        et_payPassword.setOnInputListener(new VerifyCodeEditText.OnInputListener() {
            @Override
            public void onComplete(String input) {
                if (null != mOnTextSendListener) {
                    mOnTextSendListener.onbackPwd(input);
                }
                dismiss();
            }

            @Override
            public void onChange(String input) {

            }

            @Override
            public void onClear() {

            }
        });


    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        if (mOnTextSendListener != null) mOnTextSendListener.dismiss();

    }

    @Override
    public void show() {
        super.show();
    }
}
