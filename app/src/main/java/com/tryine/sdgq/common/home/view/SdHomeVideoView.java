package com.tryine.sdgq.common.home.view;

import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.home.bean.VideoModel;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface SdHomeVideoView extends BaseView {
    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);

    void onGetVideoListSuccess(List<VideoModel> videoModelList);

    void onGetVideoListSuccess(List<VideoModel> videoModelList, int pages);

    void onGetZhVideoListSuccess(List<VideoModel> videoModelList);

    void onGetVideoDetailSuccess(VideoModel videoModel, List<VideoModel> videoModelList, List<SheetMusicBean> sheetMusicBeans);

    void onUnlockVideoSuccess();

    void onFocusSuccess(String isFocus);

    void onCollectSuccess(String isCollect);

    void onBuypiaonscoreSuccess();

    void onFailed(String message);
}
