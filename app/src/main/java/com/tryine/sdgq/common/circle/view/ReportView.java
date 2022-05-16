package com.tryine.sdgq.common.circle.view;

import com.tryine.sdgq.common.circle.bean.ReportTypeBean;
import com.tryine.sdgq.common.home.bean.CampusBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface ReportView extends BaseView {
    void onGetReportTypeListSuccess(List<ReportTypeBean> reportTypeBeans);

    void onUploadFileSuccess(String fileUrl, int type);

    void onReportSuccess();

    void onUserComplainSuccess();

    void onGetCampusBeanListSuccess(List<CampusBean> campusBeanList);

    void onFailed(String message);
}
