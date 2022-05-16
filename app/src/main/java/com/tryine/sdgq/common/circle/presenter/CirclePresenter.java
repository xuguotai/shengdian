package com.tryine.sdgq.common.circle.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.circle.view.CircleView;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.common.user.view.LoginView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CirclePresenter extends BasePresenter {
    public CirclePresenter(Context context) {
        super(context);
    }

    CircleView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (CircleView) view;
    }

    /**
     * 发布
     *
     * @param obj
     */
    public void addpublishtopic(JSONObject obj) {
        try {
            ApiHelper.generalApi(Constant.addpublishtopic, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onAddCircleSuccess();
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
     * h5活动发布
     * @param obj
     */
    public void partakeactivity(JSONObject obj) {
        try {
            ApiHelper.generalApi(Constant.partakeactivity, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onAddCircleSuccess();
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
     * 获取话题列表
     */
    public void getTopicList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getTopicList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetTopicListSuccess(homeMenuBeanList);
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
     * 获取活动列表
     */
    public void getHdList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getHdList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                    }.getType();
                    List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetHdListSuccess(homeMenuBeanList);
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


    public void uploadFile(String file, int type) {
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
                        mView.onUploadFileSuccess(resultData.optString("url"), type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 获取琴友圈
     *
     * @param moduleType
     * @param searchType
     * @param latLong
     * @param pageNum
     */
    public void getCircleList(String moduleType, String searchType, String latLong, int pageNum, String title) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("moduleType", moduleType);
            if (searchType.equals("2")) {
                obj.put("searchType", "3");
            } else if (searchType.equals("3")) {
                obj.put("searchType", "4");
            } else {
                obj.put("searchType", searchType);
            }

            obj.put("latLong", latLong);
            obj.put("title", title);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getCircleList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CircleBean>>() {
                        }.getType();
                        List<CircleBean> goodsBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCircleBeanListSuccess(goodsBeanList, pages);
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
     * 搜索用户
     *
     * @param mobile
     * @param pageNum
     */
    public void getsearchuserinfo(String mobile, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("mobile", mobile);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.searchuserinfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<FansBean>>() {
                        }.getType();
                        List<FansBean> fansBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetFansBeanSuccess(fansBeanList, pages);
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
     * 关注/取消关注
     *
     * @param focusUserId
     * @param type
     */
    public void setFocus(String focusUserId, String type) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("focusUserId", focusUserId);//  用户id
            obj.put("type", type);// 操作类型 0-关注 1-取消关注
            ApiHelper.generalApi(Constant.setFocus, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onFocusSuccess("0".equals(type) ? "1" : "0");
                        }
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
     * 点赞
     *
     * @param id
     * @param type
     */
    public void setGive(String id, String type,int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("resourceId", id);//点赞资源id
            obj.put("resourceType", "1");//(0:短视频 1:琴友圈)
            obj.put("type", type);// 0-点赞 1-取消点赞
            ApiHelper.generalApi(Constant.setGive, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onGiveSuccess(type.equals("0") ? "1" : "0",position);
                        }
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

}
