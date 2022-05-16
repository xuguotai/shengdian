package com.tryine.sdgq.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ToastUtils;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.view.dialog.MapDialog;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by qujingfeng   地图导航
 */

public class NavUtil {

    //定位
    public static boolean mPermissionGranted = false;

    public static void location(Activity content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(content, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(content, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mPermissionGranted = true;
            } else {
                content.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, Parameter.LOCATION_REQUESTCODE);

                return;
            }
        } else {
            mPermissionGranted = true;
        }

        if (!mPermissionGranted) return;
        gaode(content);
    }


    /**
     * 高德地图定位
     *
     * @param activity
     */
    public static void gaode(Activity activity) {
        AMapLocationClient mLocationClient = new AMapLocationClient(activity);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        SPUtils.saveConfigString(Parameter.LOCATION, aMapLocation.getLongitude() + "," + aMapLocation.getLatitude());
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });

        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    /**********************************************导航******************************************/
    /**
     * 导航数据封装
     **/
    public static JSONArray checkInstalledMap(Context context, double latitude, double longitude, String name) {
        JSONArray jsonArray = new JSONArray();
        try {
            if (isPackageInstalled(context, "com.baidu.BaiduMap")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "百度地图");
                jsonObject.put("packageName", "com.baidu.BaiduMap");
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("name", name);
                jsonArray.put(jsonObject);
            }
            if (isPackageInstalled(context, "com.autonavi.minimap")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "高德地图");
                jsonObject.put("packageName", "com.autonavi.minimap");
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("name", name);
                jsonArray.put(jsonObject);
            }
            if (isPackageInstalled(context, "com.google.android.apps.maps")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "谷歌地图");
                jsonObject.put("packageName", "com.google.android.apps.maps");
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("name", name);
                jsonArray.put(jsonObject);
            }
            if (isPackageInstalled(context, "com.tencent.map")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "腾讯地图");
                jsonObject.put("packageName", "com.tencent.map");
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("name", name);
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }


    /**
     * 导航
     *
     * @param context
     * @param lonLat  经纬度 ,拼接
     * @param name    目的地名称
     */
    public static void toNav(Activity context, String lonLat, String name) {
        if (!lonLat.contains(",")) {
            ToastUtils.showShort("坐标信息有误");
            return;
        }
        String[] array = lonLat.split(",");
        double longitude = Double.parseDouble(array[0]);//经度
        double latitude = Double.parseDouble(array[1]);//纬度
        JSONArray jsonArray = checkInstalledMap(context, longitude, latitude, name);
        if (jsonArray.length() > 0) {
            if (jsonArray.length() == 1) {
                startNav(context, jsonArray.optJSONObject(0));
            } else {
                //地图选择
                new MapDialog(context, jsonArray).show();
            }
        } else {
            Toast.makeText(context, "您的手机中未安装地图软件,安装后方可使用导航功能", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始导航
     **/
    public static void startNav(Context context, JSONObject jsonObject) {
        try {
            if (jsonObject.optString("packageName").equals("com.baidu.BaiduMap")) {
                openBaiduNav(context, jsonObject.optDouble("latitude"), jsonObject.optDouble("longitude"), jsonObject.optString("name"));
            } else if (jsonObject.optString("packageName").equals("com.autonavi.minimap")) {
                openAMapNav(context, jsonObject.optDouble("latitude"), jsonObject.optDouble("longitude"), jsonObject.optString("name"));
            } else if (jsonObject.optString("packageName").equals("com.google.android.apps.maps")) {
                openGoogleMapNav(context, jsonObject.optDouble("latitude"), jsonObject.optDouble("longitude"), jsonObject.optString("name"));
            } else if (jsonObject.optString("packageName").equals("com.tencent.map")) {
                openTencentMapNav(context, jsonObject.optDouble("latitude"), jsonObject.optDouble("longitude"), jsonObject.optString("name"));
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Activity未启动", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isPackageInstalled(Context mContext, String packagename) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
            return false;
        } finally {
            return packageInfo == null ? false : true;
        }
    }

    /**
     * 打开百度
     **/
    public static void openBaiduNav(Context context, double longitude, double latitude, String name) {
        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/direction?destination=name:" + name + "|latlng:" + latitude + "," + longitude + "&coord_type=bd09ll&src=appName"));
        context.startActivity(naviIntent);
    }

    /**
     * 打开高德
     **/
    public static void openAMapNav(Context context, double longitude, double latitude, String name) {
        if (null == name || "".equals(name)) {
            name = "";
        }
        // 高德地图
        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("amapuri://route/plan/?dname=" + name + "&dlat=" + latitude + "&dlon=" + longitude + "&dev=0&t=0"));
        naviIntent.setAction(Intent.ACTION_VIEW);
        naviIntent.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(naviIntent);
    }

    /**
     * 打开腾讯
     **/
    public static void openTencentMapNav(Context context, double longitude, double latitude, String name) {
        // 腾讯地图
        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=CurrentLocation&to=" + name + "&tocoord=" + latitude + "," + longitude + "&referer=appName"));
        context.startActivity(naviIntent);
    }

    /**
     * 打开谷歌
     **/
    public static void openGoogleMapNav(Context context, double longitude, double latitude, String name) {
        StringBuffer stringBuffer = new StringBuffer("google.navigation:q=").append(latitude).append(",").append(longitude).append("&mode=d");
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
        i.setPackage("com.google.android.apps.maps");
        context.startActivity(i);

    }

}
