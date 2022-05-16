package com.tryine.sdgq.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tryine.sdgq.R;
import com.tryine.sdgq.config.Parameter;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;

/**
 * 作者：qujingfeng
 * 创建时间：2020.08.04 11:30
 */

public class ShareUtils {


    public static String saveImageToGallery(Activity content, Bitmap bmp) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "";
        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpg";

        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            content.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));


            ToastUtil.toastShortMessage("图片保存成功");
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * view转bitmap
     */
    public static Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    /**
     * 该方式原理主要是：View组件显示的内容可以通过cache机制保存为bitmap
     */
    public static Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = null;
        //开启view缓存bitmap
        view.setDrawingCacheEnabled(true);
        //设置view缓存Bitmap质量
        view.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        //获取缓存的bitmap
        Bitmap cache = view.getDrawingCache();
        if (cache != null && !cache.isRecycled()) {
            bitmap = Bitmap.createBitmap(cache);
        }
        //销毁view缓存bitmap
        view.destroyDrawingCache();
        //关闭view缓存bitmap
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }


    private static UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.e("分享开始", platform + "");
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.e("分享成功", platform + "");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.e("分享失败", platform + "" + t.getMessage());
            ToastUtil.toastLongMessage(t.getMessage());
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.e("分享取消", platform + "");
        }
    };


    /**
     * 分享链接
     *
     * @param context
     * @param var1    分享枚举
     * @param url     分享的链接
     * @param logo    logo
     * @param title   标题
     * @param desc    描述
     */
    public static void shareLink(Activity context, SHARE_MEDIA var1, String url, String logo, String title, String desc) {
        UMImage image = new UMImage(context, logo);
        if(StringUtils.isEmpty(logo)){
            image = new UMImage(context, R.mipmap.ic_logo);
        }
        if(null==url){
            ToastUtil.toastLongMessage("分享链接为空");
            return;
        }
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(desc);//描述

        new ShareAction(context)
                .setPlatform(var1)
                .withMedia(web)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    /***
     * 分享图片
     * @param context
     * @param var1 分享枚举
     * @param bitmap 图片bitmap
     * @param url 图片url
     */
    public static void shareImage(Activity context, SHARE_MEDIA var1, Bitmap bitmap, String url) {
        UMImage image = null;
        if ("".equals(url) && null == bitmap) {
            ToastUtil.toastShortMessage("分享内容为空");
            return;
        }
        if (null != bitmap) {
            image = new UMImage(context, bitmap);
        }
        if (null != url && !"".equals(url)) {
            image = new UMImage(context, url);
        }
//        //缩略图
        image.setThumb(image);

        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享

        new ShareAction(context)
                .setPlatform(var1)
                .withText("")
                .withMedia(image)
                .setCallback(shareListener)//回调监听器
                .share();
    }

    /**
     * 分享小程序 微信好友
     * @param context
     * @param url 网页链接
     * @param title 标题
     * @param desc  描述
     * @param path 小程序路径
     * @param userName 小程序id
     */
    public static void shareMiniProgram(Activity context, String url, String title, String desc
            , String path, String userName) {

        //兼容低版本的网页链接
        UMMin umMin = new UMMin(url);
        // 小程序消息封面图片
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo);
        UMImage image = new UMImage(context, bmp);
        umMin.setThumb(image);
        // 小程序消息title
        umMin.setTitle(title);
        // 小程序消息描述
        umMin.setDescription(desc);
        //小程序页面路径
        umMin.setPath(path);
        // 小程序原始id,在微信平台查询
        umMin.setUserName(userName);
        new ShareAction(context)
                .withMedia(umMin)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();
    }

    /**
     * 分享小程序 微信好友 微信sdk
     * @param context
     * @param url 网页链接
     * @param bitmap 小程序封面图
     * @param title 标题
     * @param desc  描述
     * @param path 小程序路径
     * @param userName 小程序id
     */
    public static void shareMiniProgramBuyWechat(Activity context, String url, Bitmap bitmap , String title, String desc
            , String path, String userName) {


        IWXAPI api = WXAPIFactory.createWXAPI(context, Parameter.WX_APP_ID, true);

        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = url; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = userName;     // 小程序原始id
        miniProgramObj.path = path;            //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        bitmap.recycle();

        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title;                    // 小程序消息title
        msg.description = desc;               // 小程序消息desc
        msg.thumbData = bmpToByteArray(thumbBmp, true);// 小程序消息封面图片，小于128k

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("miniProgram");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
        api.sendReq(req);
    }


    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static void share(Activity context, int type, ShareInfoBean bean){
        if(bean==null){
            return;
        }
        //0 - 小程序， 1 - h5， 2 - 图片url
        if("1".equals(bean.getDataType())){
            //分享链接
            shareLink(context,getType(type),bean.getWebpageUrl(),bean.getThumImage(),bean.getTitle(),bean.getContent());
        } if("0".equals(bean.getDataType())){
            //分享小程序
            returnBitMap(context,bean);

        }
    }

    public static  SHARE_MEDIA getType(int type){
        if(type==Parameter.SHARE_WX){
            return SHARE_MEDIA.WEIXIN;
        }else if(type==Parameter.SHARE_PYQ){
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }else if(type==Parameter.SHARE_QQ){
            return SHARE_MEDIA.QQ;
        }else if(type==Parameter.SHARE_WB){
            return SHARE_MEDIA.SINA;
        }
        return null;
    }

    /**小程序封面url转bitMap**/
    public static void returnBitMap(Activity context, ShareInfoBean bean){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(bean.getImgUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    shareMiniProgramBuyWechat(context,bean.getWebpageUrl(),bitmap,bean.getTitle(),bean.getContent(),
                            bean.getPath(),bean.getUserName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
