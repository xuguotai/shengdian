package com.tryine.sdgq.jiguang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tryine.sdgq.common.circle.activity.CircleDetailActivity;
import com.tryine.sdgq.common.home.activity.H5WebViewJsActivity;
import com.tryine.sdgq.common.home.activity.SdVideoDetailActivity;
import com.tryine.sdgq.common.live.activity.LiveCourseDetailActivity;
import com.tryine.sdgq.common.mine.activity.MessageListActivity;
import com.tryine.sdgq.config.Parameter;
import com.tryine.sdgq.util.SPUtils;

import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;


public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        Intent intent = new Intent("com.jiguang.demo.message");
        intent.putExtra("msg", customMessage.message);
        context.sendBroadcast(intent);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
       try {
           String content = message.notificationExtras;
           JSONObject obj = new JSONObject(content);
           if(obj.optString("pageTag").equals("system_msg")){ //系统消息页面
               Intent intent = new Intent(context, MessageListActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
               context.startActivity(intent);
           }else if(obj.optString("pageTag").equals("video_detail")){ //短视频详情页 videoId
               Intent intent = new Intent(context, SdVideoDetailActivity.class);
               intent.putExtra("videoId",obj.optString("videoId"));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
               context.startActivity(intent);
           }else if(obj.optString("pageTag").equals("content_detail")){ //琴友圈详情页 contentId
               Intent intent = new Intent(context,CircleDetailActivity.class);
               intent.putExtra("id",obj.optString("contentId"));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
               context.startActivity(intent);
           }else if(obj.optString("pageTag").equals("record_detail")){  //活动作品详情页（带了活动作品详情id => recordId）
               Intent intent = new Intent(context,  H5WebViewJsActivity.class);
               intent.putExtra("url","http://ht.2007shengdian.cn/#/game/tree?userId=" + SPUtils.getString(Parameter.USER_ID));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
               context.startActivity(intent);
           }else if(obj.optString("pageTag").equals("live_course_detail")){    //直播课详情页（带了直播课详情id => courseId）
               Intent intent = new Intent(context, LiveCourseDetailActivity.class);
               intent.putExtra("id",obj.optString("courseId"));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
               context.startActivity(intent);
           }else if(obj.optString("pageTag").equals("course_detail")){     //活动作品详情页（带了活动作品详情id => recordId）

           }


//           if([[data jk_stringForKey:@"pageTag"]isEqualToString:@"system_msg"]){
//               //系统消息页面
//               SDGQMyMessageVC *vc = [[SDGQMyMessageVC alloc]init];
//            [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//           }else if ([[data jk_stringForKey:@"pageTag"]isEqualToString:@"video_detail"]){
//               //短视频详情页（带了短视频id => videoId ）
//               NSString *videoId = [data jk_stringForKey:@"videoId"];
//               NSInteger videoType = [data jk_integerForKey:@"videoType"];
//               if(videoType){
//                   SDGQPlayVideoCollectionDetailVC *vc = [[SDGQPlayVideoCollectionDetailVC alloc]init];
//                   vc.videoId = videoId;
//               [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//               }else{
//                   SDGQPlayVideoDetailVC *vc = [[SDGQPlayVideoDetailVC alloc]init];
//                   vc.videoId = videoId;
//               [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//               }
//           }else if ([[data jk_stringForKey:@"pageTag"]isEqualToString:@"content_detail"]){
//               //琴友圈详情页（带了琴友圈详情id => contentId）
//               NSString *contentId = [data jk_stringForKey:@"contentId"];
//               FriendCircleDetailVC *vc = [[FriendCircleDetailVC alloc]init];
//               vc.detail_id = contentId;
//           [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//           }else if ([[data jk_stringForKey:@"pageTag"]isEqualToString:@"record_detail"]){
//               //活动作品详情页（带了活动作品详情id => recordId）
//               NSString *recordId = [data jk_stringForKey:@"recordId"];
//               NSString *urlStr = [NSString stringWithFormat:@"http://ht.2007shengdian.cn/#/activitys?userId=%@",[SDGQHelper getUserModel].userId];
//               BaseWebViewVC *vc = [[BaseWebViewVC alloc]initWithUrl:urlStr];
//               vc.type = 0;
//           [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//           }else if ([[data jk_stringForKey:@"pageTag"]isEqualToString:@"live_course_detail"]){
//               //直播课详情页（带了直播课详情id => courseId）
//               NSString *courseId = [data jk_stringForKey:@"courseId"];
//               NSInteger courseType = [data jk_integerForKey:@"courseType"];
//               if(courseType){
//                   LiveCoachOneDetail *vc = [[LiveCoachOneDetail alloc]init];
//                   vc.live_id = courseId;
//               [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//               }else{
//                   LiveCoachDetailVC *vc = [[LiveCoachDetailVC alloc]init];
//                   vc.live_id = courseId;
//               [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//               }
//           }else if ([[data jk_stringForKey:@"pageTag"]isEqualToString:@"course_detail"]){
//               //活动作品详情页（带了活动作品详情id => recordId）
//               SDGQMyCurriculumAlreadyVC *vc = [[SDGQMyCurriculumAlreadyVC alloc]init];
//           [[self getCurrentVC].navigationController pushViewController:vc animated:YES];
//           }else{
//
//           }

       }catch (Exception e){

       }


        try{
//            //打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE,message.notificationTitle);
//            bundle.putString(JPushInterface.EXTRA_ALERT,message.notificationContent);
//            i.putExtras(bundle);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);
        }catch (Throwable throwable){

        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
        Intent intent = new Intent("com.jiguang.demo.register");
        context.sendBroadcast(intent);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

}
