package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.tryine.sdgq.R;

public class DeleteCommentDialog extends Dialog implements View.OnClickListener {

    private Button btDelete;
    private Button cancelBtn;

    private View.OnClickListener confirmListener;
    private View.OnClickListener middleListener;

    boolean isBlack;

    /**
     * @param context
     */
    public DeleteCommentDialog(Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.isBlack = isBlack;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_bottom);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        btDelete = (Button) findViewById(R.id.tv1);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(this);
        btDelete.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dismiss();
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv1) {
            if (confirmListener != null) {
                confirmListener.onClick(v);
            }
            dismiss();
        }
        if (id == R.id.cancelBtn) {

            dismiss();
        }
    }

    public View.OnClickListener getConfirmListener() {
        return confirmListener;
    }

    public void setConfirmListener(View.OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public View.OnClickListener getMiddleListener() {
        return middleListener;
    }

    public void setMiddleListener(View.OnClickListener middleListener) {
        this.middleListener = middleListener;
    }
}
