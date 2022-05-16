package com.tryine.sdgq.api;



import com.tryine.sdgq.common.mine.bean.ImageUploadBean;

import org.json.JSONObject;

import java.util.List;

public class ApiHelper extends ApiBase {

    /**
     * 通用api
     * @param url 地址
     * @param params 参数
     * @param callback 回调
     */
    public static void generalApi(String url, JSONObject params, BaseCallback callback){
        _postByParams(url, params, callback);
    }

    /**
     * 文件上传
     * @param url
     * @param filePaths
     * @param callback
     */
    public static void uploadFile(String url, List<ImageUploadBean> filePaths, BaseCallback callback){
        _uploadFile(url, filePaths, callback);
    }

}
