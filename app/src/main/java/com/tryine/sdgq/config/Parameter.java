package com.tryine.sdgq.config;

public class Parameter {

    public static final int SUCCESS_CODE = 200;
    public static final int LOCATION_REQUESTCODE = 123;

    public static final String PAGE_SIZE = "10";//分页条数
    public static final String cache = "share_data";
    public static final String cache_config = "share_data_config";
    public static final String IS_LOGIN = "is_login";//是否登录

    public static final String LOCATION = "locationLonLat";//定位 经纬度

    public static final String TOKEN = "token";//token
    public static final String USER_ID = "userId";//userId
    public static final String USER_INFO = "UserBean";//UserBean

    public static final String QQ_APP_ID = "101988355";//官方获取的QQAPPID
    public static final String WX_APP_ID = "wx03fa7bc42215fb62";
    public static final String WX_APP_SECRET = "db2a5e4986ad7f988156f3a41a8f4c1a";

    public static String JPushRegistrationID = "";//极光推送Id

    /**分享类型**/
    public static final int SHARE_TYPE_NORMAL = 1;//分享
    public static final int SHARE_TYPE_REPORT = 2;//分享+举报拉黑
    public static final int SHARE_WX = 0;//微信
    public static final int SHARE_PYQ = 1;//朋友圈
    public static final int SHARE_QQ = 2;//qq
    public static final int SHARE_WB = 3;//微博
    public static final int SHARE_JB = 4;//举报


}
