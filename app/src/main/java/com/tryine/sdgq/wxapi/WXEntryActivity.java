package com.tryine.sdgq.wxapi;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;
    private String unionid;
    private String openid;
    private ProgressBar progressBar;
    private WXEntryActivity mContext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, Parameter.WX_APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);

    }

    private void createProgressDialog() {
        mContext=this;
        mProgressDialog=new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("登录中，请稍后");
        mProgressDialog.show();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //登录成功的回调
                //登录成功后resp的返回类型为 SendAuth.Resp
                if (baseResp instanceof SendAuth.Resp) {
                String code = ((SendAuth.Resp) baseResp).code;
                SPUtils.saveString("wxCode",code);
                getAccessToken(code);
                Log.d("fantasychongwxlogin", code.toString()+ "");

                } else {//否则是分享的回调
                    ToastUtil.toastLongMessage("分享成功");
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void getAccessToken(String code) {
        createProgressDialog();
        //获取授权
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append(Parameter.WX_APP_ID)
                .append("&secret=")
                .append(Parameter.WX_APP_SECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        Log.d("urlurl", loginUrl.toString());

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(loginUrl.toString())
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fan12", "onFailure: ");
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseInfo= response.body().string();
                Log.d("fan12", "onResponse: " +responseInfo);

                String access = null;
                String openId = null;
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo);
                    access = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getUserInfo(access, openId);
            }
        });


    }

    private void getUserInfo(String access, String openid) {
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openid;

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(getUserInfoUrl)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fan12", "onFailure: ");
                mProgressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseInfo= response.body().string();
                Log.d("fan123", "onResponse: " + responseInfo);
                SharedPreferences.Editor editor= getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                editor.putString("responseInfo", responseInfo);
                editor.commit();
                finish();
                mProgressDialog.dismiss();
            }
        });
    }

}
