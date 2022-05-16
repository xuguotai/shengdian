package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.TeacherBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface CourseView extends BaseView {
    void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList,int pages);
    void onGetCancelCourseBeanListSuccess(List<CourseBean> courseBeanList,int pages);
    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList);
    void onGetTeacherBeanListSuccess(List<TeacherBean> teacherBeanList,int pages);
    void onGetcancelledSuccess(int count,int positions);
    void onAddCampusSuccess();
    void onAddCourseDataSuccess();
    void onCancellationSuccess();
    void onGetsignatureSuccess(String signature);
    void onUploadFileSuccess(String fileUrl);
    void onGetDetailinfoSuccess(String classContent,String problemContent,String homeworkContent,String nextContent,String attachmentUrl,String videoUrl);
    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);
    void onselectsuspendedSuccess(int selectsuspended,int positions);
    void onSuspendedSuccess();

    void onFailed(String message);
}
