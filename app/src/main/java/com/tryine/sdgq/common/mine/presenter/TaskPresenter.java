package com.tryine.sdgq.common.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tryine.sdgq.api.ApiHelper;
import com.tryine.sdgq.api.JsonCallBack;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.mine.bean.FansBean;
import com.tryine.sdgq.common.mine.bean.KtzlBean;
import com.tryine.sdgq.common.mine.bean.TaskBean;
import com.tryine.sdgq.common.mine.view.TaskView;
import com.tryine.sdgq.common.mine.view.TeacherView;
import com.tryine.sdgq.config.Constant;
import com.tryine.sdgq.mvp.BasePresenter;
import com.tryine.sdgq.mvp.BaseView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import okhttp3.Call;

public class TaskPresenter extends BasePresenter {

    public TaskPresenter(Context context) {
        super(context);
    }

    TaskView mView;

    @Override
    public void attachView(BaseView view) {
        super.attachView(view);
        mView = (TaskView) view;
    }


    /**
     * 任务列表
     */
    public void getTaskList() {
        try {
            JSONObject obj = new JSONObject();

            ApiHelper.generalApi(Constant.getTaskList, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    Type collectionType = new TypeToken<Collection<TaskBean>>() {
                    }.getType();
                    List<TaskBean> taskBeanList = new Gson().fromJson(response.optJSONArray("result").toString(), collectionType);
                    mView.onGetTaskBeanListSuccess(taskBeanList);
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
     * 签到
     */
    public void signin() {
        try {
            JSONObject obj = new JSONObject();

            ApiHelper.generalApi(Constant.signin, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onSigninSuccess();
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
     * 领取奖励
     * @param id
     */
    public void receive(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            ApiHelper.generalApi(Constant.receive, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    if ("200".equals(response.optString("code"))) {
                        mView.onReceiveSuccess();
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
     * 查询连续签到天数
     */
    public void getContinuesign() {
        try {
            JSONObject obj = new JSONObject();

            ApiHelper.generalApi(Constant.getcontinuesign, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                   try {
                       JSONObject obj = new JSONObject(response.optString("result"));
                       mView.onGetContinuesignSuccess(obj.optInt("count"));
                   }catch (Exception e){
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
     * 课堂资料列表
     * @param pageNum
     * @param uploadStatus
     */
    public void getSelectclassroomdatalist(int pageNum, int uploadStatus) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("pageNum", pageNum);
            obj.put("pageSize", "10");
            obj.put("uploadStatus", uploadStatus);//资料状态 0-未上传 1-已上传
            ApiHelper.generalApi(Constant.getSelectclassroomdatalist, obj, new JsonCallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject result = new JSONObject(response.optString("result"));
                        Type collectionType = new TypeToken<Collection<KtzlBean>>() {
                        }.getType();
                        List<KtzlBean> ktzlBeans = new Gson().fromJson(result.optJSONArray("list").toString(), collectionType);
                        int pages = result.optInt("pages");
                        mView.onGetKtzlBeanSuccess(ktzlBeans, pages);
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
