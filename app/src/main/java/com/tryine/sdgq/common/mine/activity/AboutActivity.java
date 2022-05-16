package com.tryine.sdgq.common.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: zhangshuaijun
 * @time: 2022-01-11 15:07
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_versionName)
    TextView tv_versionName;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        setWhiteBar();
        tv_title.setText("关于我们");
        tv_versionName.setText("当前版本 V" + getVersion());
    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "无法获取到版本号";
        }
    }
}
