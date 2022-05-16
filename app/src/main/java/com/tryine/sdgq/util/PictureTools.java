package com.tryine.sdgq.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * 相册选择工具类
 * 作者：qujingfeng
 * 创建时间：2020.06.16 10:44
 */

public class PictureTools {
    /**
     * 文件读写权限
     *
     * @param activity
     * @return
     */
    public static boolean checkREADExternalStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //检查没有打开照相机或文件读写权限就弹框请求打开这三个权限
                activity.requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * 相机权限
     *
     * @param activity
     * @return
     */
    public static boolean checkCamearPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                //检查没有打开照相机或文件读写权限就弹框请求打开这三个权限
                activity.requestPermissions(new String[]{
                        Manifest.permission.CAMERA}, 0);
                return false;
            }
            return true;
        }
        return true;
    }

    //去图库选择
    public static void gallery(Activity mContext) {
        if (!checkREADExternalStoragePermission(mContext)) {
            return;
        }
        if (!checkCamearPermission(mContext)) {
            return;
        }
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isCamera(true)
                .circleDimmedLayer(true)
                .showCropFrame(false)
                .hideBottomControls(true)
                .maxVideoSelectNum(1)
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                .isCamera(true)// 是否显示拍照按钮
                .maxSelectNum(1)// 最大图片选择数量
                .isCompress(true)// 是否压缩 true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    //去图库选择
    public static void gallery(Activity mContext,int result) {
        if (!checkREADExternalStoragePermission(mContext)) {
            return;
        }
        if (!checkCamearPermission(mContext)) {
            return;
        }
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isCamera(false)
                .maxVideoSelectNum(1)
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                .isCamera(true)// 是否显示拍照按钮
                .maxSelectNum(1)// 最大图片选择数量
                .isCompress(true)// 是否压缩 true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(result);
    }

    //去图库选择
    public static void galleryNum(Activity mContext, int maxNum) {
        if (!checkREADExternalStoragePermission(mContext)) {
            return;
        }
        if (!checkCamearPermission(mContext)) {
            return;
        }
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .maxVideoSelectNum(1)
                .isPreviewImage(false)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isPreviewVideo(false)// 是否可预览视频
                .maxSelectNum(maxNum)// 最大图片选择数量
                .isCompress(true)// 是否压缩 true or false
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.REQUEST_CAMERA);
    }
}
