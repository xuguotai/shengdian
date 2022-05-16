package com.tryine.sdgq.api;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by zhuhan on 18/4/11.
 */

public abstract class BaseCallback<T> extends Callback<T> {
    protected final int ERROR = -10;
    protected final int FAILD = -20;
    protected final int EXCEPTION = -30;

    /**
     * 请求错误回调
     * UI主线程
     *
     * @param call 原始call
     * @param e    异常信息
     * @param id   请求执行id
     */
    @Override
    public void onError(Call call, Exception e, int id) {

        //打印onError
        LogUtils.d(e.getMessage());
        if(e instanceof ConnectException || e instanceof SocketTimeoutException){
//            ToastUtil.toastLongMessage("网络异常");
        }else{
            onError(call, e, null);
        }
    }


    /**
     * 响应体数据转换 ,非UI线程
     *
     * @param response 响应体
     * @param id       请求ID
     * @return
     * @throws Exception
     */
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        //请求是否成功
        if (!response.isSuccessful())
            return null;
        ResponseBody body = response.body();
        String token = response.header("Authorization");
        if (!StringUtils.isEmpty(token)) {
            SPUtils.saveString(Parameter.TOKEN, token);
        }
        if (body == null)
            return null;

        try {
            //映射到实体类
            T t = convertData(body);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 请求成功且 数据状态status==0
     *
     * @param response 响应体
     */
    public abstract void onSuccess(T response);

    /**
     * 请求异常回调
     *
     * @param call
     * @param e
     */
    public abstract void onError(Call call, Exception e, T response);

    /**
     * 请求成功  数据状态status!=0
     *
     * @param status
     */
    public abstract void onOtherStatus(int status, T response);

    /**
     * 数据转换
     *
     * @param body 响应体
     * @return
     */
    public abstract T convertData(ResponseBody body);
}
