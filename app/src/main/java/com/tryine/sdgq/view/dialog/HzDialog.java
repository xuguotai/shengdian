package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.util.GlideEngine;

/**
 * 合种dialog
 *
 * @author：qujingfeng
 * @time：2020.06.15 15:06
 */
public class HzDialog extends Dialog implements View.OnClickListener {

    TextView tv_title;
    ImageView ic_jrhz, ic_zxx, iv_close, iv_head;
    View view;

    String titleStr;
    String avatar;

    public HzDialog(Context context, String titleStr, String avatar) {
        super(context, R.style.ActionSheetDialogStyle);
        this.titleStr = titleStr;
        this.avatar = avatar;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_hz);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_title = findViewById(R.id.tv_title);
        ic_jrhz = findViewById(R.id.ic_jrhz);
        ic_zxx = findViewById(R.id.ic_zxx);
        iv_close = findViewById(R.id.iv_close);
        iv_head = findViewById(R.id.iv_head);

        tv_title.setText("您的好友 " + titleStr);
        GlideEngine.createGlideEngine().loadImage(getContext(), avatar, iv_head);

        ic_jrhz.setOnClickListener(this);
        ic_zxx.setOnClickListener(this);
        iv_close.setOnClickListener(this);


    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void insure();

        void cancel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_zxx:
                if (null != onItemClickListener)
                    onItemClickListener.cancel();
                dismiss();
                break;
            case R.id.ic_jrhz:
                if (null != onItemClickListener)
                    onItemClickListener.insure();
                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }

    }
}
