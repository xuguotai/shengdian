package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.common.home.bean.CourseBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface CampusView extends BaseView {
    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList,int pages);

    void onFailed(String message);
}
