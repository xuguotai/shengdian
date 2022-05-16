package com.tryine.sdgq.common.live.tencent.liteav.liveroom.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tryine.sdgq.R;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;


/**
 * Module:   InputTextMsgDialog
 * <p>
 * Function: 观众、主播的弹幕或普通文本的输入框
 */
public class InputTextMsgDialog extends Dialog {
    private static final String TAG = InputTextMsgDialog.class.getSimpleName();

    private TextView mTextConfirm;
    private EditText mEditMessage;
    private RelativeLayout mRelativeLayout;
    private LinearLayout mBarrageArea;
    private LinearLayout mConfirmArea;
    private Context mContext;
    private InputMethodManager mInputMethodManager;
    private OnTextSendListener mOnTextSendListener;

    private boolean mDanmuOpen = false;
    UserBean userBean;

    public interface OnTextSendListener {
        void onTextSend(String msg, boolean tanmuOpen);
    }

    public InputTextMsgDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.trtcliveroom_dialog_input_text);
        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
        mEditMessage = (EditText) findViewById(R.id.et_input_message);
        mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        //修改下划线颜色
        mEditMessage.getBackground().setColorFilter(context.getResources().getColor(R.color.trtcliveroom_transparent), PorterDuff.Mode.CLEAR);


        mTextConfirm = (TextView) findViewById(R.id.confrim_btn);
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    //1.用户等级 2.是否vip 3.用户userid
                    msg = msg + "!#!" + userBean.getLevel() + "!#!" + userBean.getIsVip() + "!#!" + userBean.getId();

                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    mInputMethodManager.showSoftInput(mEditMessage, InputMethodManager.SHOW_FORCED);
                    mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                    mEditMessage.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, R.string.trtcliveroom_warning_not_empty, Toast.LENGTH_LONG).show();
                }
                mEditMessage.setText(null);
            }
        });

        final Button barrageBtn = (Button) findViewById(R.id.barrage_btn);
        barrageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.mipmap.trtcliveroom_barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.mipmap.trtcliveroom_barrage_slider_off);
                }
            }
        });

        mBarrageArea = (LinearLayout) findViewById(R.id.barrage_area);
        mBarrageArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.mipmap.trtcliveroom_barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.mipmap.trtcliveroom_barrage_slider_off);
                }
            }
        });

        mEditMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (mEditMessage.getText().length() > 0) {
                            //                            mOnTextSendListener.onTextSend("" + messageTextView.getText(), mDanmuOpen);
                            //sendText("" + messageTextView.getText());
                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                            mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                            //                            messageTextView.setText("");
                            dismiss();
                        } else {
                            Toast.makeText(mContext, R.string.trtcliveroom_warning_not_empty, Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        mConfirmArea = (LinearLayout) findViewById(R.id.confirm_area);
        mConfirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEditMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    mInputMethodManager.showSoftInput(mEditMessage, InputMethodManager.SHOW_FORCED);
                    mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                    mEditMessage.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, R.string.trtcliveroom_warning_not_empty, Toast.LENGTH_LONG).show();
                }
                mEditMessage.setText(null);
            }
        });

        mEditMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_outside_view);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view)
                    dismiss();
            }
        });

        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void show() {
        super.show();
    }
}
