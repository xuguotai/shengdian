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

/**
 * 通用提示dialog
 *
 * @author：qujingfeng
 * @time：2020.06.15 15:06
 */
public class PromptDialog extends Dialog implements View.OnClickListener {

    TextView tv_title, tv_prompt, tv_cancel, tv_insure;
    View view;
    ImageView iv_image;
    private String titleStr = "";
    private String promotStr = "";
    private String cancelStr = "";
    private String insureStr = "";
    private int resourceId = 0;

    /**
     * @param context
     * @param resourceId 图片id
     * @param titleStr   标题
     * @param promotStr  提示内容 为""隐藏
     * @param insureStr  确认文字
     * @param cancelStr  取消文字 为""隐藏左边
     */
    public PromptDialog(Context context, int resourceId, String titleStr, String promotStr, String insureStr, String cancelStr) {
        super(context, R.style.ActionSheetDialogStyle);
        this.titleStr = titleStr;
        this.promotStr = promotStr;
        this.cancelStr = cancelStr;
        this.insureStr = insureStr;
        this.resourceId = resourceId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_promot);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_title = findViewById(R.id.tv_title);
        tv_prompt = findViewById(R.id.tv_prompt);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_insure = findViewById(R.id.tv_insure);
        view = findViewById(R.id.view);
        iv_image = findViewById(R.id.iv_image);
        tv_cancel.setOnClickListener(this);
        tv_insure.setOnClickListener(this);

        tv_title.setText(titleStr);
        tv_prompt.setText(Html.fromHtml(promotStr));
        tv_cancel.setText(cancelStr);
        tv_insure.setText(insureStr);
        if (0 != resourceId) {
            //显示图片
            iv_image.setBackgroundResource(resourceId);
            iv_image.setVisibility(View.VISIBLE);
        }
        if ("".equals(promotStr)) {
            //隐藏内容
            tv_prompt.setVisibility(View.GONE);
        }
        if ("".equals(cancelStr)) {
            //隐藏左边取消
            view.setVisibility(View.GONE);
            tv_cancel.setVisibility(View.GONE);
        }

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
            case R.id.tv_cancel:
                if (null != onItemClickListener)
                    onItemClickListener.cancel();
                dismiss();
                break;
            case R.id.tv_insure:
                if (null != onItemClickListener)
                    onItemClickListener.insure();
                dismiss();
                break;
        }

    }
}
