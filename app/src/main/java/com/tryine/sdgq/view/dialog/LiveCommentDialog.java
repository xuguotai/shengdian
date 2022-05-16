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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.widget.InputTextMsgDialog;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;


/**
 * 直播评论
 */
public class LiveCommentDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText et_content;
    private TextView tv_confirm;

    UserBean userBean;

    private OnTextSendListener mOnTextSendListener;

    public interface OnTextSendListener {
        void onTextSend(String msg, boolean tanmuOpen);

        void dismiss();
    }


    public LiveCommentDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        init();
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        setLayout();
    }


    private void init() {
        setContentView(R.layout.dialog_comment_live);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        et_content.requestFocus();
        et_content.addTextChangedListener(new TextWatcher() {
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
                if (TextUtils.isEmpty(et_content.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入评论");
                    return;
                }
                userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
                String msg = et_content.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    //1.用户等级 2.是否vip 3.用户userid
                    msg = msg + "!#!" + userBean.getLevel() + "!#!" + userBean.getIsVip() + "!#!" + userBean.getId();
                    mOnTextSendListener.onTextSend(msg, false);
                    dismiss();
                    et_content.setText("");
                } else {
                    Toast.makeText(mContext, R.string.trtcliveroom_warning_not_empty, Toast.LENGTH_LONG).show();
                }
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

}
