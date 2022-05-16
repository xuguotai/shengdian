package com.tryine.sdgq.api;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.tryine.sdgq.config.Parameter;

import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Created by zhuhan on 18/4/11.
 * 请求回调-返回json
 */

public abstract class JsonCallBack extends BaseCallback<JSONObject> {


    /**
     * 处理返回结果
     *
     * @param response 接口返回
     * @param id       执行ID
     */
    @Override
    public void onResponse(JSONObject response, int id) {
        try {
            if (response == null) {
                onError(null, null, null);
                return;
            }
            int status = response.optInt("code");
            Log.e("返回数据", response.toString());
            switch (status) {
                case Parameter.SUCCESS_CODE:
                    //请求成功， status=0
                    onSuccess(response);
                    break;
                default:
                    onOtherStatus(response.optInt("code"), response);
                    break;
            }

        } catch (Exception e) {
            //当onSuccess或onOtherStatus中代码异常
            onError(null, e, response);
        }
    }

    /**
     * 数据转换 ,非UI线程
     *
     * @param body 响应体
     * @return
     */
    @Override
    public JSONObject convertData(ResponseBody body) {
        try {
            String bodyStr = body.string();
            //打印ResponseBody
            LogUtils.d(bodyStr);
            //映射到实体类
            JSONObject jsonObject = new JSONObject(bodyStr);
            return jsonObject;
        } catch (Exception e) {
            //打印onError
            LogUtils.d(e.getMessage());
            return null;
        }
    }

}
