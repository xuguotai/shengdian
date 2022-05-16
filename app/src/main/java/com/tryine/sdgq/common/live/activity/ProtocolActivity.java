package com.tryine.sdgq.common.live.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
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
public class ProtocolActivity extends BaseActivity implements ProtocolView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webview)
    WebView webview;

    int type; //0直播协议 1.服务协议2隐私政策3提现规程4钱包说明

    ProtocolPresenter protocolPresenter;

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, ProtocolActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }


    @Override
    protected void init() {
        setWhiteBar();
        protocolPresenter = new ProtocolPresenter(this);
        protocolPresenter.attachView(this);

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 0:
                tv_title.setText("直播协议");
                break;
            case 1:
                tv_title.setText("服务协议");
                break;
            case 2:
                tv_title.setText("隐私政策");
                break;
            case 3:
                tv_title.setText("提现规程");
                break;
            case 4:
                tv_title.setText("钱包说明");
                break;
            case 5:
                tv_title.setText("APP使用说明");
                break;
        }

        protocolPresenter.getAgreement(type);

        // 设置WebView的客户端
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// 返回false
            }
        });
//        WebSettings webSettings = webview.getSettings();
//        // 让WebView能够执行javaScript
//        webSettings.setJavaScriptEnabled(true);
//        // 让JavaScript可以自动打开windows
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        // 设置缓存
//        webSettings.setAppCacheEnabled(true);
//        // 设置缓存模式,一共有四种模式
//        webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
//        // 设置缓存路径
////        webSettings.setAppCachePath("");
//        // 支持缩放(适配到当前屏幕)
//        webSettings.setSupportZoom(true);
//        // 将图片调整到合适的大小
//        webSettings.setUseWideViewPort(true);
//        // 支持内容重新布局,一共有四种方式
//        // 默认的是NARROW_COLUMNS
//        // 设置可以被显示的屏幕控制
//        webSettings.setDisplayZoomControls(true);
        // 设置WebView属性，能够执行Javascript脚本
        // mWebView.getSettings().setJavaScriptEnabled(true);
        //3、 加载需要显示的网页

        ///4、设置响应超链接，在安卓5.0系统，不使用下面语句超链接也是正常的，但在MIUI中安卓4.4.4中需要使用下面这条语句，才能响应超链接
        // mWebView.setWebViewClient(new HelloWebViewClient());
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
