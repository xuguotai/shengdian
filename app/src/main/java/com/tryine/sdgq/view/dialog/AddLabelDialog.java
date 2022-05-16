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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.tryine.sdgq.R;
import com.tryine.sdgq.util.ToastUtil;



public class AddLabelDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText et_amount;
    private TextView tv_confirm;

    public interface OnTextSendListener {
        void onMoney(String money);
        void dismiss();
    }

    private OnTextSendListener mOnTextSendListener;

    public AddLabelDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        init();
        setLayout();
    }


    private void init() {
        setContentView(R.layout.dialog_add_label);
        et_amount = (EditText) findViewById(R.id.et_amount);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        et_amount.requestFocus();
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);


        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_amount.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入金额");
                    return;
                }
                if (null != mOnTextSendListener)
                    mOnTextSendListener.onMoney(et_amount.getText().toString());
                dismiss();

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
