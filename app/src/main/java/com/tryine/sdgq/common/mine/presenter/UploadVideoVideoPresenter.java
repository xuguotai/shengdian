package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.MainActivity;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.common.mine.view.UploadVideoView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;
import com.tryine.sdgq.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadVideoVideoPresenter extends BasePresenter {

    public UploadVideoVideoPresenter(Context context) {
        super(context);
    }

    UploadVideoView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (UploadVideoView) view;
    }

    /**
     * 获取视频首页类型列表
     */
    public void getVideoTypeList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getVideoTypeList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetHomeMenuBeanSuccess(homeMenuBeanList);
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取合集列表
     */
    public void getVideoHjList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.selecttitlelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetVideoHjSuccess(homeMenuBeanList);
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 关联琴谱列表
     */
    public void getSelectpiaonlist() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getSelectpiaonlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<SheetMusicBean>>() {
                    }.getType();
                    List<SheetMusicBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetSheetMusicBeanSuccess(homeMenuBeanList);
                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取上传视频签名
     */
    public void getSignature() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getSignature, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        mView.onGetsignatureSuccess(result.optString("signature"));
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 发布视频
     *
     * @param videoType
     * @param title
     * @param typeId
     * @param pianoScore
     * @param goldenBean
     * @param fileId
     * @param videoUrl
     * @param coverUrl
     */
    public void uploadVideo(int videoType, String title, String typeId, String pianoScore, String goldenBean, String titleId, String fileId, String videoUrl,
                            String coverUrl, int videoTime, String videoWidth, String videoHeight, int beanType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("videoType", videoType);//视频种类 0-单个 1-合集 2-关联合集
            obj.put("title", title);//视频标题
            obj.put("typeId", typeId);//视频类型id
            obj.put("pianoScore", pianoScore);//关联琴谱（多个逗号分隔）
            obj.put("goldenBean", goldenBean);//所需金豆
            obj.put("titleId", titleId);//关联合集视频标题id videoType为2，比传

            obj.put("fileId", fileId);//视频文件id
            obj.put("videoUrl", videoUrl);//视频路径
            obj.put("coverUrl", coverUrl);//封面路径
            obj.put("videoTime", videoTime);//视频时长（秒）
            obj.put("videoWidth", videoWidth);//视频宽度
            obj.put("videoLength", videoHeight);//视频高度
            obj.put("beanType", beanType);//豆子类型 0-金豆 1-银豆


            ApiHelper.generalApi(Constant.uploadVideo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onUploadVideoSuccess();
                    }

                }

                @Override
                public void onError(Call call, Exception e, JSONObject response) {
                    mView.onFailed(e.toString());
                }

                @Override
                public void onOtherStatus(int status, JSONObject response) {
                    mView.onFailed(response.optString("message"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void uploadFile(String file) {
        String suffix = file.substring(file.lastIndexOf(".") + 1);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), new File(file));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "temp." + suffix, fileBody)
                .addFormDataPart("type", "1")
                .build();
        Request request = new Request.Builder()
                .url(Constant.uploadFile)
                .addHeader("Authorization", SPUtils.getToken())
                .addHeader("Terminaltype", "Android")
                .addHeader("platform", "app")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(60000, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mView.onFailed(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    //{"code":"200","message":"操作成功!","result":{"url":"https://sdgq-1308358049.cos.ap-beijing.myqcloud.com
                    // /images/65d94752-d35e-48a9-8fb5-c39476e70529.jpg"}}
                    String str = response.body().string();
                    JSONObject data = new JSONObject(str);
                    JSONObject resultData = new JSONObject(data.optString("result"));
                    if ("200".equals(data.optString("code"))) {
                        mView.onUploadFileSuccess(resultData.optString("url"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
