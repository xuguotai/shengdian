package com.tryine.sdgq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tryine.sdgq.R;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-17 10:40
 */
public class GradeTextView extends LinearLayout {

    Context mContext;

    LinearLayout ll_root;
    TextView tv_dj;

    public GradeTextView(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public GradeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    public GradeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews();
    }

    private void initViews() {
        View view = View.inflate(mContext, R.layout.view_grade, null);
        ll_root = view.findViewById(R.id.ll_root);
        tv_dj = view.findViewById(R.id.tv_dj);
        addView(view);
    }


    public void setData(int grade) {
        tv_dj.setText(grade + "");
        if(grade < 10){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg);
        }else if(grade < 20){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg_1);
        }else if(grade < 30){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg_2);
        }else if(grade < 40){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg_3);
        }else if(grade < 50){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg_4);
        }else if(grade < 60){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg_5);
        }else if(grade == 60){
            ll_root.setBackgroundResource(R.mipmap.ic_personal_dz_bg_6);
        }
    }


}
