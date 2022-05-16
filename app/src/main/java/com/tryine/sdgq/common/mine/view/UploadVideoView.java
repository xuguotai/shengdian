package com.tryine.sdgq.common.mine.view;

import com.tryine.sdgq.common.home.bean.HomeMenuBean;
import com.tryine.sdgq.common.home.bean.SheetMusicBean;
import com.tryine.sdgq.common.mine.bean.ImageUploadBean;
import com.tryine.sdgq.mvp.BaseView;

import java.util.List;

public interface UploadVideoView extends BaseView {

    void onGetHomeMenuBeanSuccess(List<HomeMenuBean> homeMenuBeanList);
    void onGetVideoHjSuccess(List<HomeMenuBean> homeMenuBeanList);
    void onGetSheetMusicBeanSuccess(List<SheetMusicBean> sheetMusicBeanList);
    void onUploadVideoSuccess();
    void onUploadFileSuccess(String fileUrl);
    void onGetsignatureSuccess(String signature);

    void onFailed(String message);
}
