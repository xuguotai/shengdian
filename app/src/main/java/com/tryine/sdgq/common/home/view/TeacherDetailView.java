package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.circle.bean.CircleBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CommentBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface TeacherDetailView extends BaseView {
    void onGetCommentSuccess(List<CommentBean> commentBeanList,int pages);
    void onGetVideoModelListSuccess(List<VideoModel> videoModelLists, int pages);
    void onGetCircleBeanListSuccess(List<CircleBean> videoModelLists, int pages);
    void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList,String selectDate,String sysDate,int position);
    void onGetTeacherBeanSuccess(TeacherBean teacherBean);
    void onGetTeacherBeanSuccess();

    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList);
    void onAppointmentSuccess();


    void onFailed(String message);
}
