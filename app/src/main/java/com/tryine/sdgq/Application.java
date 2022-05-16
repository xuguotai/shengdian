package com.tryine.sdgq;

import android.app.Activity;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.rtmp.TXLiveBase;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.helper.ConfigHelper;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xuexiang.xui.XUI;

import cn.jpush.android.api.JPushInterface;


public class Application extends MultiDexApplication {

    private static Application baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        /*LeakCanary.Config config = LeakCanary.getConfig().newBuilder()
                .retainedVisibleThreshold(3)

                .computeRetainedHeapSize(false)
                .build();
        LeakCanary.setConfig(config);*/
        baseApplication = this;

        XUI.init(baseApplication); //初始化UI框架


    }

    /**
     * 初始化SDK（在用户同意隐私政策协议后初始化）
     * @param mThis
     */
    public static void initSDK(Activity mThis){
        String licenceURL = "https://license.vod2.myqcloud.com/license/v2/1308358049_1/v_cube.license"; // 获取到的 licence url
        String licenceKey = "6990ae3c4ea383817919dea44cfe890a"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(mThis, licenceURL, licenceKey);

        //友盟
        UMConfigure.init(mThis, "61f0ad25e014255fcb06dd78", "", UMConfigure.DEVICE_TYPE_PHONE, null);
        PlatformConfig.setWeixin(Parameter.WX_APP_ID, Parameter.WX_APP_SECRET);
        PlatformConfig.setSinaWeibo("563795586", "b5f1e3c8a5dd67dd55fb0c00e0b51560", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("101988355", "b6cf3e26228d43cff18d57113b2e4897");
        PlatformConfig.setQQFileProvider("101988355");

        // 初始化MultiDex
        MultiDex.install(mThis);


        /**
         * TUIKit的初始化函数
         *
         * @param context  应用的上下文，一般为对应应用的ApplicationContext
         * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
         * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
         */
        TUIKit.init(mThis, 1400618314, new ConfigHelper().getConfigs());

        /**
         * 极光
         */
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mThis);

        //获取注册ID
        Parameter.JPushRegistrationID = JPushInterface.getRegistrationID(mThis);
    }


    public static Application getApplication() {
        return baseApplication;
    }


}
