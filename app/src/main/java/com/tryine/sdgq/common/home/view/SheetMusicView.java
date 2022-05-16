package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface SheetMusicView extends BaseView {
    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);
    void onGetChildMenuSuccess(List<HomeMenuBean> homeMenuBeanList, int pages);
    void onBuypiaonscoreSuccess();
    void onGetpiaonscoredetailSuccess(SheetMusicBean sheetMusicBean, List<VideoModel> videoModelList);
    void onFailed(String message);
}
