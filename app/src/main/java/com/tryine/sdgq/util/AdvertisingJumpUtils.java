package com.tryine.sdgq.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.tryine.sdgq.common.circle.activity.TopicDetailActivity;
import com.tryine.sdgq.common.home.activity.H5WebViewActivity;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.home.activity.SheetMusicDetailActivity;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.live.activity.LiveCourseDetailActivity;
import com.tryine.sdgq.common.mall.activity.GoodsDetailActivity;

public class AdvertisingJumpUtils {

    private static Activity mActivity;
    private static BannerBean infoBean;

    /**
     * 广告跳转
     */
    public static void advertisingJump(Activity activity, BannerBean bean) {
        mActivity = activity;
        infoBean = bean;
        if (!TextUtils.isEmpty(bean.getContextId()) && !bean.getContextId().equals("0")) {
            onClick();
        } else if (!TextUtils.isEmpty(bean.getIconUrl())) {
            //外部链接
            //跳转类型判断
            mActivity.startActivity(new Intent(mActivity, H5WebViewActivity.class).putExtra("url", infoBean.getIconUrl()));
        }

    }

    private static void onClick() {
        if (infoBean.getContextType().equals("0")) {
            SdVideoDetailActivity.start(mActivity, infoBean.getContextId());
        } else if (infoBean.getContextType().equals("1")) {
            LiveCourseDetailActivity.start(mActivity, infoBean.getContextId());
        } else if (infoBean.getContextType().equals("2")) {
            GoodsDetailActivity.start(mActivity, infoBean.getContextId());
        } else if (infoBean.getContextType().equals("3")) {
            TopicDetailActivity.start(mActivity, infoBean.getContextId());
        } else if (infoBean.getContextType().equals("4")) {
            SheetMusicDetailActivity.start(mActivity, infoBean.getContextId());
        }
    }
}
