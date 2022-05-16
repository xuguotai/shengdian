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
 * 修改昵称
 * @author：qujingfeng
 * @time：2020.06.18 11:02
 */
public class UpdateUserNameActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_nick_name;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("修改昵称");
    }

    @OnClick({R.id.iv_black, R.id.ll_btn_upload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_black:
                finish();
                break;
            case R.id.ll_btn_upload:
                if ("".equals(et_name.getText().toString())) {
                    ToastUtil.toastLongMessage("请输入昵称");
                    return;
                }
                setResult(RESULT_OK, new Intent().putExtra("nickName", et_name.getText().toString()));
                finish();
                break;
        }
    }
}
