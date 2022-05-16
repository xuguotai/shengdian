package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.mine.bean.TecheCasBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoBean;
import com.tryine.sdgq.common.mine.bean.TecheCasinfoRecordBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface MineCourseView extends BaseView {
    void onGetTecheCasBeanListSuccess(List<TecheCasBean> techeCasBeans, int pages);
    void onGetTecheCasinfoBeanListSuccess(List<TecheCasinfoBean> techeCasinfoBeans, int pages);
    void onGetRecordListSuccess(List<TecheCasinfoRecordBean> techeCasinfoRecordBeans, int pages);

    void onFailed(String message);
}
