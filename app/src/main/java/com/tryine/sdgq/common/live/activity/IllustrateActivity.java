package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.live.presenter.ProtocolPresenter;
import com.tryine.sdgq.common.live.view.ProtocolView;
import com.tryine.sdgq.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户协议
 */
public class IllustrateActivity extends BaseActivity implements ProtocolView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webview)
    WebView webview;


    String data;
    int type;

    ProtocolPresenter protocolPresenter;

    public static void start(Context context, int type, String data) {
        Intent intent = new Intent();
        intent.setClass(context, IllustrateActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }


    @Override
    protected void init() {
        setWhiteBar();
        protocolPresenter = new ProtocolPresenter(this);
        protocolPresenter.attachView(this);

        type = getIntent().getIntExtra("type", 0);
        data = getIntent().getStringExtra("data");
        switch (type) {
            case 0:
                tv_title.setText("等级说明");
                break;

        }
        setHtmlData(data);

        // 设置WebView的客户端
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_protocol;
    }


    @OnClick({R.id.iv_black})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_black:
                finish();
                break;
        }
    }

    @Override
    public void onGetProtocolSuccess(String agreement) {
        setHtmlData(agreement);
    }

    @Override
    public void onFailed(String message) {
        ToastUtil.toastLongMessage(message);
    }


    private void setHtmlData(String remark) {
        if (TextUtils.isEmpty(remark)) return;
        String htmlData = "" +
                "<html><style>img{width:100%;height:auto}</style><body>" + remark + "</body></html>";
        webview.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }
}
