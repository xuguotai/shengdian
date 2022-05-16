package com.tryine.sdgq.common;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.user.activity.LoginActivity;
import com.tryine.sdgq.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页
 *
 * @author：qujingfeng
 * @time：2020.08.05 10:54
 */


public class SplashActivity extends BaseActivity {

    @Override
    protected void init() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .init();
//        if (checkStoragePermission()) {
            mHandler.sendEmptyMessageDelayed(1,2000);
//        }
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startActivity();
                    break;
                case 2:
                    //存储权限
                    break;
            }
        }
    };

    private void startActivity(){
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if(SPUtils.getIsLogin(this)){
            MainActivity.start(this);
//            TestActivity.start(this);
        }else{
            LoginActivity.start(this);
        }

        finish();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    /**权限检测申请**/
    public boolean checkStoragePermission() {
        List<String> permissionList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        /*if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }*/
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionList != null && permissionList.size() > 0) {
            String[] permissionStr = new String[permissionList.size()];
            for (int i = 0; i < permissionList.size(); i++) {
                permissionStr[i] = permissionList.get(i);
            }
            ActivityCompat.requestPermissions(this, permissionStr, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean isAllow = true;
            int type = 2;
            for (int i = 0; i < permissions.length; i++) {
                String permissionStr = permissions[i];
                if (permissionStr.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || permissionStr.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isAllow = false;
                        type = 2;
                    }
                }
            }
            if (!isAllow) {
                Message msg= new Message();
                msg.what=type;
                mHandler.sendMessage(msg);
            }else{
                startActivity();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(checkStoragePermission()){
                startActivity();
            }
        }
    }
}