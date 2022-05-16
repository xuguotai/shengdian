package com.tryine.sdgq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.live.bean.LiveBean;

/**
 * @author: zhangshuaijun
 * @time: 2022-04-07 09:37
 */
public class BuyBannerLiveCourseDialog extends Dialog implements View.OnClickListener {

    TextView tv_buy;
    View view;
    ImageView iv_close;
    private LiveBean liveBean;


    public BuyBannerLiveCourseDialog(Context context, LiveBean liveBean) {
        super(context, R.style.ActionSheetDialogStyle);
        this.liveBean = liveBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_buy_course);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        iv_close = findViewById(R.id.iv_close);
        tv_buy = findViewById(R.id.tv_buy);
        iv_close.setOnClickListener(this);
        tv_buy.setOnClickListener(this);

        tv_buy.setText("购买本节课程  " + liveBean.getDetailGoldBean() + " SD金豆");

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void insure();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_buy:
                if (null != onItemClickListener)
                    onItemClickListener.insure();
                dismiss();
                break;
        }

    }
}
