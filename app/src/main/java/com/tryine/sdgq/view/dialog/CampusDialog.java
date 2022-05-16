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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.util.SPUtils;

import java.util.List;


public class CampusDialog extends Dialog {
    private Activity mContext;
    List<CampusBean> reasonList;

    LinearLayout ll_type;
    ImageView iv_close;

    public CampusDialog(Activity context, final List<CampusBean> reasonList) {
        super(context, R.style.ActionSheetDialogStyle);
        this.reasonList = reasonList;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_videotype);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ll_type = findViewById(R.id.ll_type);
        initView();
    }

    private void initView() {
        for (int i = 0; i < reasonList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_videotype, null);
            TextView tv_title = view.findViewById(R.id.tv_title);
            if(null != reasonList.get(i).getName() && !reasonList.get(i).getName().equals("")){
                tv_title.setText(reasonList.get(i).getName());
            }
            final int index = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.resultReason(reasonList.get(index));
                        SPUtils.saveString("receiptId",reasonList.get(index).getId());
                    }
                    dismiss();
                }
            });
            ll_type.addView(view);
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void resultReason(CampusBean reason);
    }

}
