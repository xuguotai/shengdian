package com.tryine.sdgq.common.home.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.view.CourseView;
import com.tryine.sdgq.common.home.view.HomeView;
import com.tryine.sdgq.common.mall.bean.GoodsBean;
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

public class CoursePresenter extends BasePresenter {

    public CoursePresenter(Context context) {
        super(context);
    }

    CourseView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (CourseView) view;
    }

    /**
     * 课程列表
     */
    public void getCourseList(String catsId, String id, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("catsId", catsId);
            obj.put("id", id);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getCourseList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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
     * 校区详情课程列表
     *
     * @param id
     * @param pageNum
     */
    public void getCampusDetailCourseList(String id, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "4");
            ApiHelper.generalApi(Constant.getCourseList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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
     * 获取校区
     */
    public void getFicationList() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getFicationList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CampusBean>>() {
                        }.getType();
                        List<CampusBean> campusBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetCampusBeanListSuccess(campusBeanList);
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
     * 我的课程列表
     */
    public void getMyCourse() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getMyCourse, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        mView.onGetCourseBeanListSuccess(courseBeanList, 0);
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
     * 报名咨询
     *
     * @param campusId
     * @param couresId
     * @param couresName
     * @param name
     * @param age
     * @param phone
     * @param remake
     */
    public void addCourse(String campusId, String couresId, String couresName, String name, String age, String phone, String sex, String remake) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("campusId", campusId);//校区id
            obj.put("couresId", couresId);//课程id
            obj.put("couresName", couresName);//课程名称
            obj.put("sex", sex);// 性别男女0男1女
            obj.put("name", name);// 姓名
            obj.put("age", age);// 年龄
            obj.put("phone", phone);// 联系方式
            obj.put("remake", remake);// 备注
            ApiHelper.generalApi(Constant.addCourse, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onAddCampusSuccess();
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
     * 已预约线下课程列表
     */
    public void getOfflinelist(String courseId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", courseId);
            ApiHelper.generalApi(Constant.getOfflinelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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
     * 线下课程列表
     *
     * @param courseId
     */
    public void getHavelist(String courseId, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", courseId);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getHavelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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
     * 添加课堂资料
     */
    public void addCourseData(JSONObject obj) {
        try {
            ApiHelper.generalApi(Constant.addCourseData, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onAddCourseDataSuccess();
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


    public void uploadFile(String file) {
        String suffix = file.substring(file.lastIndexOf(".") + 1);
        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), new File(file));
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


    /**
     * 评价
     *
     * @param offioneId
     * @param content
     * @param star
     */
    public void evaluation(String offioneId, String content, String star, String topStatus) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("offioneId", offioneId);
            obj.put("content", content);
            obj.put("star", star);
            obj.put("topStatus", topStatus);
            ApiHelper.generalApi(Constant.evaluation, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onAddCampusSuccess();
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
     * 课堂资料
     *
     * @param offioneId
     */
    public void detailinfo(String offioneId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("offioneId", offioneId);
            ApiHelper.generalApi(Constant.detailinfo, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject obj = new JSONObject(response.optString("result"));
                        mView.onGetDetailinfoSuccess(
                                obj.optString("classContent")
                                , obj.optString("problemContent")
                                , obj.optString("homeworkContent")
                                , obj.optString("nextContent")
                                , obj.optString("attachmentUrl")
                                , obj.optString("videoUrl"));
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
     * 校区-老师列表
     *
     * @param id
     */
    public void getTeacherlist(String id, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getTeacherlist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<TeacherBean>>() {
                        }.getType();
                        List<TeacherBean> teacherBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetTeacherBeanListSuccess(teacherBeanList, pages);
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
     * 课程取消预约
     *
     * @param id
     */
    public void cancellation(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.cancellation, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onCancellationSuccess();
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
     * 获取本月可取消次数
     *
     * @param courseId
     */
    public void getCancelled(String courseId, int positions) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", courseId);
            ApiHelper.generalApi(Constant.cancelled, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onGetcancelledSuccess(response.getInt("result"), positions);
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
     * 已取消预约课程
     *
     * @param courseId
     */
    public void getCancelhavelist(String courseId, int pageNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courseId", courseId);
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            ApiHelper.generalApi(Constant.getCancelhavelist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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
     * 我的线上课程列表
     *
     * @param pageNum
     */
    public void getOnLineCourseList(int pageNum, int searchType) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("searchType", searchType);
            ApiHelper.generalApi(Constant.getOnLineCourseList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<CourseBean>>() {
                        }.getType();
                        List<CourseBean> courseBeanList = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetCourseBeanListSuccess(courseBeanList, pages);
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
     * 课程分类
     */
    public void getCourescatslist() {
        try {
            JSONObject obj = new JSONObject();
            ApiHelper.generalApi(Constant.getCourescatslist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        Type collectionType = new TypeToken<Collection<HomeMenuBean>>() {
                        }.getType();
                        List<HomeMenuBean> homeMenuBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                        mView.onGetHomeMenuBeanSuccess(homeMenuBeanList);

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


    public void getselectsuspended(String courseId, int positions) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", courseId);
            ApiHelper.generalApi(Constant.selectsuspended, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        mView.onselectsuspendedSuccess(response.optInt("result"), positions);
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
     * 暂停卡
     *
     * @param id
     * @param beanType
     * @param suspendedNum
     * @param beanNumber
     */
    public void suspended(String id, String beanType, String beanNumber, String suspendedNum) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);//卡id
            obj.put("beanType", beanType);//  类型：0金豆，1银豆
            obj.put("suspendedNum", suspendedNum);//  暂停天数
            obj.put("beanNumber", beanNumber);//  输入豆子数量
            ApiHelper.generalApi(Constant.suspended, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        if ("200".equals(response.optString("code"))) {
                            mView.onSuspendedSuccess();
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
