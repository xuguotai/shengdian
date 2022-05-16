package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.just.agentweb.AgentWeb;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.home.bean.CourseBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 加载H5
 */
public class H5WebViewActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_web)
    LinearLayout ll_web;

    private AgentWeb agentWeb;

    public static void start(Context context, String  url) {
        Intent intent = new Intent();
        intent.setClass(context, H5WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_h5_webview;
    }

    @Override
    protected void init() {
        setWhiteBar();
        String url = getIntent().getStringExtra("url");
        tv_title.setText("网页");

        agentWeb = AgentWeb.with(H5WebViewActivity.this)
                .setAgentWebParent(ll_web, new ConstraintLayout.LayoutParams(-1, -1)) //-1是指父布局
                .useDefaultIndicator() //默认进度条 可带颜色 例如 0xffffff
                .createAgentWeb()
                .ready()
                .go(url); //本地html文件
    }

    @OnClick({R.id.iv_black})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (agentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
