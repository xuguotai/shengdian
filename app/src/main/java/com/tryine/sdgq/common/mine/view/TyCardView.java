package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.mine.bean.CardBean;
import com.tryine.sdgq.common.mine.bean.ExperienceBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface TyCardView extends BaseView {
    void onGetCardBeanListSuccess(List<CardBean> cardBeanList, int pages);
    void onGetExperienceBeanSuccess(List<ExperienceBean> experienceBeanLists, int pages);
    void onGetCardBeanListSuccess();
    void onForwardingSuccess();

    void onFailed(String message);
}
