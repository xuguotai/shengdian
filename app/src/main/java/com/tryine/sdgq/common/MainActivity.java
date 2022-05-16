package com.tryine.sdgq.common;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tryine.sdgq.Application;
import com.tryine.sdgq.R;
import com.tryine.sdgq.base.BaseActivity;
import com.tryine.sdgq.common.circle.fragment.CircleFragment;
import com.tryine.sdgq.common.home.activity.BargainDetailActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.live.fragment.LiveFragment;
import com.tryine.sdgq.common.live.tencent.liteav.basic.GenerateTestUserSig;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModel;
import com.tryine.sdgq.common.live.tencent.liteav.basic.UserModelManager;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoom;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDef;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TRTCLogger;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TXCallback;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.room.impl.TXRoomService;
import com.tryine.sdgq.common.mall.fragment.MallFragment;
import com.tryine.sdgq.common.home.fragment.HomeFragment;
import com.tryine.sdgq.common.mine.fragment.MineFragment;
import com.tryine.sdgq.common.mine.presenter.UploadVideoVideoPresenter;
import com.tryine.sdgq.common.mine.view.UploadVideoView;
import com.tryine.sdgq.common.user.activity.LoginActivity;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.presenter.LoginPresenter;
import com.tryine.sdgq.common.user.view.LoginView;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.event.IMMessageCountEvent;
import com.tryine.sdgq.helper.ConfigHelper;
import com.tryine.sdgq.util.DemoLog;
import com.tryine.sdgq.util.LocationUtils;
import com.tryine.sdgq.util.NavUtil;
import com.tryine.sdgq.util.PermissionsUtils;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.xuexiang.xui.XUI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页
 *
 * @author：zhangshuaijun
 * @time：2020.08.05 10:55
 */


public class MainActivity extends BaseActivity {

    @BindView(R.id.navigationbar)
    LinearLayout navigationbar;
    @BindView(R.id.ll_item1)
    LinearLayout ll_item1;
    @BindView(R.id.ll_item2)
    LinearLayout ll_item2;
    @BindView(R.id.ll_item3)
    LinearLayout ll_item3;
    @BindView(R.id.ll_item4)
    LinearLayout ll_item4;
    @BindView(R.id.ll_item5)
    LinearLayout ll_item5;

    @BindView(R.id.tv_item1)
    TextView tv_item1;
    @BindView(R.id.tv_item2)
    TextView tv_item2;
    @BindView(R.id.tv_item3)
    TextView tv_item3;
    @BindView(R.id.tv_item4)
    TextView tv_item4;
    @BindView(R.id.tv_item5)
    TextView tv_item5;

    @BindView(R.id.iv_item1)
    ImageView iv_item1;
    @BindView(R.id.iv_item2)
    ImageView iv_item2;
    @BindView(R.id.iv_item3)
    ImageView iv_item3;
    @BindView(R.id.iv_item4)
    ImageView iv_item4;
    @BindView(R.id.iv_item5)
    ImageView iv_item5;


    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private MallFragment mallFragment;
    private LiveFragment liveFragment;
    private CircleFragment circleFragment;
    private MineFragment mineFragment;


    private boolean isUseCDNPlay = false;                //用来表示当前是否CDN模式（区别于TRTC模式）

    int position = 0;

    private String TAG = "MainActivity";

    UserBean userBeanInfo;

    //获取定位
    private String[] permissions_location = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void init() {
        //显示用户协议弹窗
        boolean isFirst = SPUtils.getBoolean(this, "isAgreement", true);
        if (!isFirst) {
            Application.initSDK(MainActivity.this);
        }

        fragmentManager = getSupportFragmentManager();
        setSelection(0);
        userBeanInfo = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
//        final UserModel userModel = UserModelManager.getInstance().getUserModel();


        LoginPresenter loginPresenter = new LoginPresenter(this);
        loginPresenter.attachView(new LoginView() {
            @Override
            public void onCodeSuccess() {

            }

            @Override
            public void onLoginSuccess(UserBean bean) {

            }

            @Override
            public void onBind(String type, String openId, String nickName, String headimgurl) {

            }

            @Override
            public void onGetUsersign(String userSign) {
                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TXRoomService.getInstance().login(GenerateTestUserSig.SDKAPPID, userBeanInfo.getId(), userSign, new TXCallback() {
                                @Override
                                public void onCallback(final int code, final String msg) {
                                }
                            });
                        }
                    });

                    TUIKit.login(SPUtils.getUserId(), userSign, new IUIKitCallBack() {
                        @Override
                        public void onError(String module, final int code, final String desc) {
                            runOnUiThread(new Runnable() {
                                public void run() {
//                                ToastUtil.toastLongMessage(getString(R.string.failed_login_tip) + ", errCode = " + code + ", errInfo = " + desc);
                                }
                            });
                            DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
                        }

                        @Override
                        public void onSuccess(Object data) {
                            UserBean userBean = new Gson().fromJson(SPUtils.getString(Parameter.USER_INFO), UserBean.class);
//                        ToastUitls.showShortToast(getBaseContext() , "IM登录成功");
                            V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();
                            v2TIMUserFullInfo.setNickname(userBean.getUserName());
                            if (!StringUtils.isEmpty(userBean.getAvatar())) {
                                v2TIMUserFullInfo.setFaceUrl(userBean.getAvatar());
                            }
                            V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
                                @Override
                                public void onSuccess() {
//                                    ToastUtil.toastLongMessage("登录成功");
//                                ToastUitls.showShortToast(mContext , "设置成功");
                                }

                                @Override
                                public void onError(int code, String desc) {
//                                ToastUitls.showShortToast(mContext , "设置失败");
                                }
                            });



                            /**
                             * 设置私信未读消息数量
                             */
                            V2TIMManager.getConversationManager().setConversationListener(new V2TIMConversationListener() {
                                @Override
                                public void onSyncServerStart() {
                                    super.onSyncServerStart();
                                }

                                @Override
                                public void onSyncServerFinish() {
                                    super.onSyncServerFinish();
                                }

                                @Override
                                public void onSyncServerFailed() {
                                    super.onSyncServerFailed();
                                }

                                @Override
                                public void onNewConversation(List<V2TIMConversation> conversationList) {
                                    ConversationManagerKit.getInstance().onRefreshConversation(conversationList);
                                }

                                @Override
                                public void onConversationChanged(List<V2TIMConversation> conversationList) {
                                    ConversationManagerKit.getInstance().onRefreshConversation(conversationList);
                                }

                                @Override
                                public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                                    ConversationManagerKit.getInstance().updateTotalUnreadMessageCount(totalUnreadCount);
                                    int messageCount = (int) totalUnreadCount;
                                    //通知私信消息数更新
                                    EventBus.getDefault().postSticky(new IMMessageCountEvent(messageCount));
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });

        loginPresenter.getUsersign();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void setSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragmentview, homeFragment, "shortVideoFragment");
                } else {
                    transaction.show(homeFragment);
                }
                transaction.commit();
                break;
            case 1:
                if (mallFragment == null) {
                    mallFragment = new MallFragment();
                    transaction.add(R.id.fragmentview, mallFragment);
                } else {
                    transaction.show(mallFragment);
                }
                transaction.commit();
                break;
            case 2:
                if (liveFragment == null) {
                    liveFragment = new LiveFragment();
                    transaction.add(R.id.fragmentview, liveFragment);
                } else {
                    transaction.show(liveFragment);
                }
                transaction.commit();
                break;
            case 3:
                PermissionsUtils.getInstance().chekPermissions(this, permissions_location, permissionsResult);
                if (circleFragment == null) {
                    circleFragment = new CircleFragment();
                    transaction.add(R.id.fragmentview, circleFragment);
                } else {
                    transaction.show(circleFragment);
                }
                transaction.commit();
                break;
            case 4:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.fragmentview, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                transaction.commit();
                break;
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (mallFragment != null) {
            transaction.hide(mallFragment);
        }
        if (liveFragment != null) {
            transaction.hide(liveFragment);
        }
        if (circleFragment != null) {
            transaction.hide(circleFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        paste();
    }

    public void paste() {
        // 获取剪贴板数据
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //此处可放 调用获取剪切板内容的代码
                ClipboardManager clipboard = (ClipboardManager) Application.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    ClipData clipData = clipboard.getPrimaryClip();
                    ClipData.Item item = clipData.getItemAt(0);
                    String content = item.getText().toString();

                    if (!StringUtils.isEmpty(content)) {
                        if (content.contains("Bargain")) {
                            BargainDetailActivity.start(mContext, content.split("!#!")[1]);
                            // 清除剪贴板
                            ClipData clip = ClipData.newPlainText("", "");
                            clipboard.setPrimaryClip(clip);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);//1秒后执行Runnable中的run方法
    }

    @OnClick({R.id.ll_item1, R.id.ll_item2, R.id.ll_item3, R.id.ll_item4, R.id.ll_item5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item1:
                position = 0;
                setSelection(0);
                check(0);
                break;
            case R.id.ll_item2:
                position = 1;
                setSelection(1);
                check(1);
                break;
            case R.id.ll_item3:
                position = 2;
                setSelection(2);
                check(2);
                break;
            case R.id.ll_item4:
                position = 3;
                setSelection(3);
                check(3);
                break;
            case R.id.ll_item5:
                position = 4;
                setSelection(4);
                check(4);
                break;
            default:
                break;
        }
    }

    /**
     * 点击底部的导航栏切换图标和字体颜色
     *
     * @param postion 点击的哪一个下标
     */
    private void check(int postion) {
        iv_item1.setImageDrawable(ContextCompat.getDrawable(this, postion == 0 ? R.mipmap.ic_bar_home_pre : R.mipmap.ic_bar_home));
        iv_item2.setImageDrawable(ContextCompat.getDrawable(this, postion == 1 ? R.mipmap.ic_bar_shopping_pre : R.mipmap.ic_bar_shopping));
        iv_item3.setImageDrawable(ContextCompat.getDrawable(this, postion == 2 ? R.mipmap.ic_bar_live_pre : R.mipmap.ic_bar_live));
        iv_item4.setImageDrawable(ContextCompat.getDrawable(this, postion == 3 ? R.mipmap.ic_bar_circle_pre : R.mipmap.ic_bar_circle));
        iv_item5.setImageDrawable(ContextCompat.getDrawable(this, postion == 4 ? R.mipmap.ic_bar_mine_pre : R.mipmap.ic_bar_mine));
    }


    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e("wy", "run: " + "用户拒绝授权");
            } else {
                //获取权限成功提示，可以不要
                Log.e("wy", "run: " + "用户已授权");
//                initLocation();
                NavUtil.gaode(MainActivity.this);
            }
        }
    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //权限通过，可以做其他事情
            NavUtil.gaode(MainActivity.this);
        }

        @Override
        public void forbitPermissons() {
            ToastUtil.toastLongMessage("权限不通过");
        }
    };

    private void initLocation() {
        Location location = LocationUtils.getInstance(mContext).showLocation();
        if (location != null) {
            String address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude();
            SPUtils.saveConfigString(Parameter.LOCATION, location.getLongitude() + "," + location.getLatitude());
            Log.d("FLY.LocationUtils", address);
//            cityName = LocationUtils.getAddress(mContext, location.getLongitude(), location.getLatitude()).replace("市", "");
        }
    }


}