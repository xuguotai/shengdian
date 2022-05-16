package com.tryine.sdgq.base;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.util.ToastUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    public Dialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        ButterKnife.bind(this);
        mContext = this;
        ActivityManager.getScreenManager().pushActivity(this);
        initDalog();
        init();
    }

    public void setWhiteBar(){
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .navigationBarColor(R.color.white)
                .init();
    }

    public void setBlackBar(){
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.black)
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色
                .init();
    }

    public void setColorBar(int color){
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(color)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
    }

    private void initDalog(){
        progressDialog = new Dialog(mContext,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.base_dialog);
        progressDialog.setTitle("正在加载....");
//        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 添加界面布局
     * @return
     */
    public abstract int getLayoutId();


    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        ActivityManager.getScreenManager().popActivity(this);
    }




    public String getTextStr(TextView view){
        return  view.getText().toString().trim();
    }


    /**
     * 关闭输入法
     *
     * @param
     */
    public void closeInput() {
        try {
            if (null != getCurrentFocus().getWindowToken()) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**复制订单号到剪切板**/
    public void copyToClipboard(String context) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("text", context);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtil.toastShortMessage("复制成功");
    }

    public String replaceNull(String str){
        if(null==str){
            str = "";
        }
        return str;
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
