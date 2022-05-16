package com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.impl.base;

import com.tryine.sdgq.common.live.tencent.liteav.liveroom.model.TRTCLiveRoomDelegate;

import java.lang.ref.WeakReference;

public class TRTCLogger {
    private static WeakReference<TRTCLiveRoomDelegate> sDelegate;

    public static void setDelegate(TRTCLiveRoomDelegate delegate) {
        if (delegate != null) {
            sDelegate = new WeakReference<>(delegate);
        } else {
            sDelegate = null;
        }
    }

    public static void e(String tag, String message) {
        callback("e", tag, message);
    }

    public static void w(String tag, String message) {
        callback("w", tag, message);
    }

    public static void i(String tag, String message) {
        callback("i", tag, message);
    }

    public static void d(String tag, String message) {
        callback("d", tag, message);
    }

    private static void callback(String level, String tag, String message) {
        WeakReference<TRTCLiveRoomDelegate> wefDelegate = sDelegate;
        if (wefDelegate != null) {
            TRTCLiveRoomDelegate delegate = wefDelegate.get();
            if (delegate != null) {
                delegate.onDebugLog("[" + level + "][" + tag + " ] " + message);
            }
        }
    }
}
