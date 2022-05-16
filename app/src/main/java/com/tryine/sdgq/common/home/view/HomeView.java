package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.home.bean.AnnouncementBean;
import com.tryine.sdgq.common.home.bean.BannerBean;
import com.tryine.sdgq.common.home.bean.BargainBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.common.home.bean.CourseTimeBean;
import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.common.live.bean.LiveBean;
import com.tryine.sdgq.common.user.bean.UserBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface HomeView extends BaseView {
    void onGetBannerBeanListSuccess(List<BannerBean> bannerBeanList);

    void onGetVideoListSuccess(List<BannerBean> bannerBeanList);

    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);

    void onGetBargainBeanListSuccess(List<BargainBean> bargainBeanList);

    void onGetCourseBeanListSuccess(List<CourseTimeBean> courseTimeBeanList);

    void onGetCourseTimeBeanListSuccess(List<CourseTimeBean> courseTimeBeanList, String selectDate, String sysDate, int position);

    void onGetJqCourseBeanListSuccess(List<CourseBean> courseTimeBeanList);

    void onGetLiveBeanListSuccess(List<LiveBean> liveBeanList);

    void onGetRefreshLiveBeanListSuccess(List<LiveBean> liveBeanList);

    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList);

    void onGetAnnouncementBeanListSuccess(List<AnnouncementBean> announcementBeanList, int pages);

    void onAppointmentSuccess();

    void onSaveorderSuccess(String orderId);

    void onGetCourseBeanListSuccess(List<CourseBean> courseBeanList, int pages);

    void onGetInvitefriendsSuccess(String title, String userId,String avatar);

    void onFailed(String message);

    void onGetUserdetailSuccess(UserBean userBean);

    void onBuyCourseSuccess();
}
