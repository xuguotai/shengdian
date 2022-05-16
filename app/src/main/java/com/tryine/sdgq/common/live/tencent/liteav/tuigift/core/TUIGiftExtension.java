package com.tryine.sdgq.common.live.tencent.liteav.tuigift.core;

import android.content.Context;


import com.tencent.qcloud.tuicore.interfaces.ITUIExtension;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIGiftButton;
import com.tryine.sdgq.common.live.tencent.liteav.tuigift.view.TUIGiftPlayView;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼物组件注册TUICore后,获取TUICore传入的用户组ID(房间ID),并绑定自己的布局文件;
 * 通过礼物面板点击礼物进行发送,礼物动画播放面板可以播放所接收到礼物的动画。
 */
public class TUIGiftExtension implements ITUIExtension {

    public static final String OBJECT_TUI_GIFT    = TUIGiftExtension.class.getName();
    public static final String KEY_EXTENSION_VIEW = "TUIExtensionView";
    public static final String KEY_PLAY_VIEW      = "TUIGiftPlayView";

    @Override
    public Map<String, Object> onGetExtensionInfo(String key, Map<String, Object> param) {
        //这个HashMap需携带返回给TUICore的View数据
        HashMap<String, Object> hashMap = new HashMap<>();

        if (OBJECT_TUI_GIFT.equals(key)) {
            Context context = (Context) param.get("context");
            String groupId = (String) param.get("groupId");
            TUIGiftButton giftButton = new TUIGiftButton(context, groupId);
            TUIGiftPlayView playView = new TUIGiftPlayView(context, groupId);
            hashMap.put(KEY_EXTENSION_VIEW, giftButton);
            hashMap.put(KEY_PLAY_VIEW, playView);
            return hashMap;
        }
        return null;
    }
}
