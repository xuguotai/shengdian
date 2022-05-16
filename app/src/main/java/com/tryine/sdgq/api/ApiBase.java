package com.tryine.sdgq.api;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by zhuhan on 17/12/27.
 */

public class ApiBase {
    //请求头map
    public static Map<String, String> header;
    private static final int CONNECT_TIME = 30000;


    /**
     * 获取请求头
     */
    private static Map<String, String> getHeader() {
        if (header == null) {
            header = new HashMap<>();
        }
        if (header != null && !StringUtils.isEmpty(SPUtils.getToken())) {
            header.put("Authorization", SPUtils.getToken());
        }
        header.put("terminalType", "Android");
        header.put("platform", "app");
        header.put("userId", SPUtils.getString(Parameter.USER_ID));
        return header;
    }

    /**
     * http post请求 带回调
     *
     * @param url      请求路径
     * @param callback 请求结果回调类
     */
    final protected static void _postByParams(String url, JSONObject json,
                                              BaseCallback callback) {
        Log.e(url + "请求数据", json.toString());
        OkHttpUtils
                .postString()
                .headers(getHeader())
                .url(url)
                .content(json.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .connTimeOut(CONNECT_TIME)
                .writeTimeOut(CONNECT_TIME)
                .readTimeOut(CONNECT_TIME)
                .execute(callback);
        //打印 url
        LogUtils.d(url + "| params : " + json.toString());
    }


    /**
     * http get请求 带回调
     *
     * @param url      请求路径
     * @param data     请求参数
     * @param callback 请求结果回调类
     */
    final protected static void _getByParams(String url, Map<String, String> data,
                                             BaseCallback callback) {
        OkHttpUtils
                .get()
                .headers(getHeader())
                .url(url)
                .params(data)
                .build()
                .connTimeOut(CONNECT_TIME)
                .writeTimeOut(CONNECT_TIME)
                .readTimeOut(CONNECT_TIME)
                .execute(callback);
        //打印 url
        LogUtils.d(url + "| params : " + data);
    }


    /**
     * 文件上传 多文件
     * @param url
     * @param filePaths
     * @param callback
     */
    final protected static void _uploadFile(String url, List<ImageUploadBean> filePaths,
                                            BaseCallback callback) {
        String sendStr = "";
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("type", "1");
            sendObj.put("timestamp", new Date().getTime());
            sendStr = sendObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Map<String, String> uploadHeader = getHeader();
        uploadHeader.put("type", "1");
        uploadHeader.put("terminalType", "Android");
        uploadHeader.put("platform", "app");
        uploadHeader.put("userId", "2");
        uploadHeader.put("Content-Type", "multipart/form-data;");
        Map<String, File> files = new HashMap<>();
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i).getLocalUrl());
            files.put(file.getName(), file);
        }
        OkHttpUtils.post()//
                .files("files", files)
//                .addFile("file", new Date().getTime() + "." + MimeTypeMap.getFileExtensionFromUrl(file), new File(file))//
                .headers(uploadHeader)
                .url(url)
                .build()//
                .execute(callback);
    }


}
