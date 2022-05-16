package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.activity.ProtocolActivity;


public class AgreementDialog extends Dialog implements View.OnClickListener {

    Context mContext;
    TextView tvMessage;
    TextView leftTV;
    TextView rightTV;
    View.OnClickListener confirmListener;
    View.OnClickListener cancelListener;


    public AgreementDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_agreement_pop_windows);
        tvMessage = findViewById(R.id.messageTV);
        leftTV = findViewById(R.id.leftTV);
        rightTV = findViewById(R.id.rightTV);
        leftTV.setOnClickListener(this);
        rightTV.setOnClickListener(this);
        setCancelable(false);
        linkText();
    }

    private void linkText() {
        tvMessage.setText("本应用尊重并保护所有用户的个人隐私权。为了给您提供更准确、更有个性化的服务，本应用会按照隐私政策的规定使用和披露您的个人信息。可阅读");
        SpannableString clickString = new SpannableString("《服务协议》");
        clickString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //跳转用户协议
                ProtocolActivity.start(mContext,1);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);//去掉下划线
                ds.setColor(Color.parseColor("#A98678"));
            }
        }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMessage.append(clickString);
        tvMessage.append("和");
        SpannableString clickString1 = new SpannableString("《隐私政策》");
        clickString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //跳转隐私政策
                ProtocolActivity.start(mContext,2);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);//去掉下划线
                ds.setColor(Color.parseColor("#A98678"));
            }
        }, 0, clickString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMessage.append(clickString1);
        tvMessage.append("了解详细信息。如你同意，请点击“同意”开始接受我们的服务。");
        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
    }

    public View.OnClickListener getConfirmListener() {
        return confirmListener;
    }

    public void setConfirmListener(View.OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public View.OnClickListener getCancelListener() {
        return cancelListener;
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.leftTV) {
            if (confirmListener != null) {
                confirmListener.onClick(view);
            }
            return;
        }
        if (id == R.id.rightTV) {
            if (cancelListener != null) {
                cancelListener.onClick(view);
            }
            return;
        }
    }
}
