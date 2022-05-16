package com.tryine.sdgq.common.circle.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageContainerBean;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.util.DemoLog;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

/**
 * 会话界面
 */
public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_page;
    }

    @Override
    protected void init() {
//        setWhiteBar();
//        setColorBar(R.color.white);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        chat(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        DemoLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
        chat(intent);
    }

    @Override
    protected void onResume() {
        DemoLog.i(TAG, "onResume");
        super.onResume();
    }

    private void chat(Intent intent) {
        Bundle bundle = intent.getExtras();
        DemoLog.i(TAG, "bundle: " + bundle + " intent: " + intent);
        if (bundle == null) {
//            startSplashActivity(null);
            return;
        }

//        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(intent);
        String ext = bundle.getString("ext");

        OfflineMessageContainerBean bean = new OfflineMessageContainerBean();

        if (TextUtils.isEmpty(ext)) {
            bean = new Gson().fromJson(ext, OfflineMessageContainerBean.class);
        }
        if (intent.getData() != null) {
            mChatInfo = new ChatInfo();
            mChatInfo.setType(bean.entity.chatType);
            mChatInfo.setId(bean.entity.sender);
            mChatInfo.setHeadImgage(bean.entity.faceUrl);
            bundle.putSerializable("chatInfo", mChatInfo);
            DemoLog.i(TAG, "offline mChatInfo: " + mChatInfo);
        } else {
            mChatInfo = (ChatInfo) bundle.getSerializable("chatInfo");
            if (mChatInfo == null) {
//                startSplashActivity(null);
                return;
            }
        }

        if (V2TIMManager.getInstance().getLoginStatus() == V2TIM_STATUS_LOGINED) {
            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        } else {
//            startSplashActivity(bundle);
        }

        checkStoragePermission();
    }


    /**
     * 权限检测申请
     **/
    public boolean checkStoragePermission() {
        List<String> permissionList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
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

}
