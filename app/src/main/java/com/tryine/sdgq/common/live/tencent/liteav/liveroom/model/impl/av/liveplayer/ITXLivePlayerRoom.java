package com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.av.liveplayer;

import android.content.Context;

import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base.TXCallback;

public interface ITXLivePlayerRoom {
    void init(Context context);

    void startPlay(String playURL, TXCloudVideoView view, TXCallback callback);

    void stopPlay(String playURL, TXCallback callback);

    void stopAllPlay();

    void muteRemoteAudio(String playURL, boolean mute);

    void muteAllRemoteAudio(boolean mute);

    void showVideoDebugLog(boolean isShow);
}