package com.tryine.sdgq.common.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改性别
 *
 * @author：qujingfeng
 * @time：2020.06.18 11:02
 */
public class UpdateSexActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_sex;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("修改性别");
    }


    @OnClick({R.id.iv_black, R.id.tv_nan,R.id.tv_nv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.tv_nan:
                setResult(RESULT_OK, new Intent().putExtra("sex", "0"));
                finish();
                break;
            case R.id.tv_nv:
                setResult(RESULT_OK, new Intent().putExtra("sex", "1"));
                finish();
                break;
        }
    }
}
