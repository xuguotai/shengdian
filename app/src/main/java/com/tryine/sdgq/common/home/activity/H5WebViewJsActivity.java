package com.tryine.sdgq.common.home.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.activity.AddHdActivity;
import com.tryine.sdgq.common.mine.activity.UpdateUserNameActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ShareInfoBean;
import com.tryine.sdgq.util.ShareUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.tryine.sdgq.view.dialog.H5ShareDialog;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 加载H5注入js
 */
public class H5WebViewJsActivity extends BaseActivity {
    @BindView(R.id.ll_web)
    LinearLayout ll_web;
    @BindView(R.id.rl_bar)
    RelativeLayout rl_bar;

    private AgentWeb agentWeb;

    String url;

    UserBean userBean;

    String hdId;

    public static void start(Context context, String url) {
        Intent intent = new Intent();
        intent.setClass(context, H5WebViewJsActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    @Override
    protected void init() {
        setWhiteBar();
        url = getIntent().getStringExtra("url");

        if (url.contains("game/tree")) {
            rl_bar.setVisibility(View.VISIBLE);
        }

        userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);

        agentWeb = AgentWeb.with(H5WebViewJsActivity.this)
                .setAgentWebParent(ll_web, new ConstraintLayout.LayoutParams(-1, -1)) //-1是指父布局
                .useDefaultIndicator() //默认进度条 可带颜色 例如 0xffffff
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        agentWeb.getJsAccessEntrace().quickCallJs("getUserId1", userBean.getId());
                        agentWeb.getJsAccessEntrace().quickCallJs("getUserId2", userBean.getId());
                        agentWeb.getJsAccessEntrace().quickCallJs("getUserId3", userBean.getId());
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url); //本地html文件

        agentWeb.getJsInterfaceHolder().addJavaObject("shareListener", new AndroidInterface());

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_h5_webviewjs;
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
        super.onDestroy();
        agentWeb.getWebLifeCycle().onDestroy();
    }

    public class AndroidInterface {
        public AndroidInterface() {

        }

        @JavascriptInterface
        public void onAppBack() {
            finish();
        }

        @JavascriptInterface
        public void onAddHd(int hdIds) {
            hdId = hdIds+"";
            AddHdActivity.start(H5WebViewJsActivity.this, hdId);
        }

        //必须声明此注解
        @JavascriptInterface
        public void onShare(String title, String content, String url, String logo, String thumImage) {
            ShareInfoBean bean = new ShareInfoBean();
            bean.setWebpageUrl(url);
            bean.setTitle(title);
            bean.setContent(content);
            bean.setThumImage(thumImage);
            bean.setDataType("1");

            H5ShareDialog h5ShareDialog = new H5ShareDialog(mContext, bean);
            h5ShareDialog.show();
            h5ShareDialog.setShareListener(new H5ShareDialog.OnShareListener() {
                @Override
                public void insure(int shareType, ShareInfoBean bean) {
                    ShareUtils.share(H5WebViewJsActivity.this, shareType, bean);
                }
            });
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2:
                    String isAdd = data.getStringExtra("isAdd");
                    if (isAdd.equals("1")) {
                        agentWeb.getJsAccessEntrace().quickCallJs("addHdSuccess",hdId);
                    }
                    break;

            }
        }

    }
}
