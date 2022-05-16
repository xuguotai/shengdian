package com.tryine.sdgq.config;

public class Constant {


    public static final String rootUrl = "http://49.233.0.106:7800";

    public static final String shareUrl = "http://49.233.0.106:9998/";


    /************************************** 首页  ****************************************************/
    /* 获取公告列表 */
    public static final String getAnnouncement = rootUrl + "/openapi/shop/announcement/list";
    /* 获取首页直播推荐列表 */
    public static final String getHomeLive = rootUrl + "/openapi/live/index/selectindexcourselist";
    /* 获取首页视频推荐列表 */
    public static final String getHomeVideo = rootUrl + "/openapi/video/video/selectindexvideolist";


    /************************************** 琴谱  ****************************************************/
    /* 琴谱类型列表 */
    public static final String getQpTypeList = rootUrl + "/openapi/video/piaon/selecttyplist";
    /* 琴谱列表 */
    public static final String getQpChildTypeList = rootUrl + "/openapi/video/piaon/selectpiaonscorelist";
    /* 购买琴谱 */
    public static final String buypiaonscore = rootUrl + "/openapi/video/piaon/buypiaonscore";
    /* 琴谱详情 */
    public static final String getpiaonscoredetail = rootUrl + "/openapi/video/piaon/getpiaonscoredetail";
    /* 搜索琴谱 */
    public static final String getSearchMusic = rootUrl + "/openapi/video/piaon/searchpiaonscorelist";
    /* 我的琴谱 */
    public static final String getMypiaonscorelist = rootUrl + "/openapi/video/piaon/mypiaonscorelist";


    /************************************** 登录注册  ****************************************************/
    /* 注册 */
    public static final String register = rootUrl + "/openapi/user/user/register";
    /* 密码登录 */
    public static final String loginByPassword = rootUrl + "/openapi/user/user/login";
    /* 第三方登录 */
    public static final String otherLogin = rootUrl + "/openapi/user/user/wxlogin";
    /* 绑定 */
    public static final String bindLogin = rootUrl + "/openapi/user/user/bindUserPhone";
    /* 验证码 */
    public static final String getCode = rootUrl + "/openapi/user/sms/send";
    /* 修改个人信息 */
    public static final String updateuserinfo = rootUrl + "/openapi/user/userinfo/update";
    /* 个人信息 */
    public static final String userdetail = rootUrl + "/openapi/user/userinfo/userdetail";
    /* 老师个人课程 */
    public static final String getTeacherList = rootUrl + "/openapi/shop/teacherCoure/techelist";
    /* 获取用户UserSign(IM) */
    public static final String getUsersign = rootUrl + "/openapi/live/room/getusersign";
    /* 获取最新用户校区 */
    public static final String usercampusid = rootUrl + "/openapi/shop/fication/usercampusid";
    /* 设置极光推送ID */
    public static final String setRegistrationId = rootUrl + "/openapi/user/user/setRegistrationId";


    /************************************** 砍价模块  ****************************************************/
    /* 首页砍价列表 */
    public static final String getHomeKjList = rootUrl + "/openapi/shop/bargain/indexlist";
    /* 砍价专区列表 */
    public static final String getBargainAreaList = rootUrl + "/openapi/shop/bargain/selectalllist";
    /* 砍价专区校区列表 */
    public static final String getBargainCampusList = rootUrl + "/openapi/shop/bargain/selectcampuslist";
    /* 去砍价 */
    public static final String saveorder = rootUrl + "/openapi/shop/bargainorder/saveorder";
    /* 砍价订单-订单详情 */
    public static final String getBargainOrderdetail = rootUrl + "/openapi/shop/bargainorder/getorderdetail";
    /* 砍价订单-去付款(立即购买) */
    public static final String goBargainBuy = rootUrl + "/openapi/shop/bargainorder/gobuy";
    /* 砍价订单-订单列表 */
    public static final String getBargainOrderlist = rootUrl + "/openapi/shop/bargainorder/list";
    /* 邀请砍价-获取H5页面砍价信息 */
    public static final String getbargainh5info = rootUrl + "/openapi/shop/bargain/getbargainh5info";
    /* 看一刀 */
    public static final String helpbargain = rootUrl + "/openapi/shop/bargain/helpbargain";


    /************************************** 圣典视频  ****************************************************/
    /* 视频类型列表 */
    public static final String getVideoTypeList = rootUrl + "/openapi/video/video/selecttypelist";
    /* 视频列表 */
    public static final String getVideoList = rootUrl + "/openapi/video/video/selectvideolist";
    /* 视频详情 */
    public static final String getvideodetail = rootUrl + "/openapi/video/video/getvideodetail";
    /* 解锁视频 */
    public static final String unlockvideo = rootUrl + "/openapi/video/video/unlockvideo";
    /* 上传视频-所有琴谱 */
    public static final String getSelectpiaonlist = rootUrl + "/openapi/video/piaon/selectpiaonlist";
    /* 发布视频 */
    public static final String uploadVideo = rootUrl + "/openapi/video/video/uploadvideo";
    /* 关联合集标题列表-下拉框所用 */
    public static final String selecttitlelist = rootUrl + "/openapi/video/video/selecttitlelist";
    /* 获取上传视频签名 */
    public static final String getSignature = rootUrl + "/openapi/video/video/getsignature";
    /* 搜索视频 */
    public static final String getSearchvideolist = rootUrl + "/openapi/video/video/searchvideolist";
    /* 我的视频 */
    public static final String getselectmyvideolist = rootUrl + "/openapi/video/video/selectmyvideolist";

    /************************************** 校区  ****************************************************/
    /* 校区列表 */
    public static final String getFicationList = rootUrl + "/openapi/shop/fication/list";

    /************************************** 直播  ****************************************************/
    /* 菜单 */
    public static final String getLiveMenuList = rootUrl + "/openapi/live/index/selecttypelist";
    /* 首页直播大课 */
    public static final String getLiveCourseList = rootUrl + "/openapi/live/index/selectlivelongcourselist";
    /* 首页一对一直播大课 */
    public static final String getLiveCourseList1 = rootUrl + "/openapi/live/index/selectonelivecourselist";
    /* 直播课列表 */
    public static final String getSearchlivecourse = rootUrl + "/openapi/live/index/searchlivecourse";
    /* 直播中列表 */
    public static final String getSelectindexallcourselist = rootUrl + "/openapi/live/index/selectindexallcourselist";
    /* 直播课详情 */
    public static final String getLivecoursedetail = rootUrl + "/openapi/live/index/getlivecoursedetail";
    /* 直播课购买 */
    public static final String buyLivecourse = rootUrl + "/openapi/live/index/buylivecourse";
    /* 开直播-直播课程列表-下拉框所用 */
    public static final String selectlivecourse = rootUrl + "/openapi/live/room/selectlivecourse";
    /* 开直播-直播章节列表-下拉框所用 */
    public static final String selectlivecoursedetail = rootUrl + "/openapi/live/room/selectlivecoursedetail";
    /* 开启直播 */
    public static final String addroom = rootUrl + "/openapi/live/room/addroom";
    /* 查询用户是否开启直播 */
    public static final String getislive = rootUrl + "/openapi/live/room/getislive";
    /*  */
    public static final String getaboutinfo = rootUrl + "/openapi/video/home/getaboutinfo";
    /* 关闭直播 */
    public static final String closeroom = rootUrl + "/openapi/live/room/closeroom";
    /* 琴友圈礼物列表 */
    public static final String getiGiftList = rootUrl + "/openapi/video/topiccontent/selectnewpresentlist";
    /* 直播礼物列表 */
    public static final String getLiveGiftList = rootUrl + "/openapi/live/index/selectpresentgiflist";
    /* 查询用户金豆，银豆 */
    public static final String getUserbean = rootUrl + "/openapi/live/room/getuserbean";
    /* 直播间送礼物 */
    public static final String sendPresent = rootUrl + "/openapi/live/index/sendpresent";
    /* 琴友圈详情送礼物 */
    public static final String sendPresent1 = rootUrl + "/openapi/video/topiccontent/sendpresent";
    /* 直播间详情 */
    public static final String getLiveroomdetail = rootUrl + "/openapi/live/index/getliveroomdetail";
    /* 获取连麦观众推/拉流地址 */
    public static final String getUsertrtcurl = rootUrl + "/openapi/live/room/getusertrtcurl";
    /* 老师详情-线上课程 */
    public static final String getlivecoursel = rootUrl + "/openapi/live/course/selectlivecourse";
    /* 埋点-统计直播间数据（观看人数/评论数/点赞 */
    public static final String countroominfo = rootUrl + "/openapi/live/room/countroominfo";

    /************************************** 商城  ****************************************************/
    /* 商城类型 */
    public static final String getGoodsTypeList = rootUrl + "/openapi/shop/goods/caseList";
    /* 商品列表 */
    public static final String getGoodsList = rootUrl + "/openapi/shop/goods/list";
    /* 新品专区商品列表 */
    public static final String getproductlist = rootUrl + "/openapi/shop/goods/productlist";
    /* 热销专区商品列表 */
    public static final String getcakeslist = rootUrl + "/openapi/shop/goods/cakeslist";
    /* 特惠专区商品列表 */
    public static final String getpreferentiallist = rootUrl + "/openapi/shop/goods/preferentiallist";
    /* 查询限量商品 */
    public static final String selectlimitgoodslist = rootUrl + "/openapi/shop/goods/selectlimitgoodslist";
    /* 查询商品列表 */
    public static final String selectgoodslist = rootUrl + "/openapi/shop/goods/selectgoodslist";
    /* 首页老师课程时间 */
    public static final String getTeacherCoureList = rootUrl + "/openapi/shop/teacherCoure/list";
    /* 商品详情 */
    public static final String getGoodsDetail = rootUrl + "/openapi/shop/goods/goodsDetail";
    /* 商品评价 */
    public static final String getcommentslist = rootUrl + "/openapi/shop/goods/commentslist";
    /* 添加购物车 */
    public static final String addCar = rootUrl + "/openapi/shop/goodsCar/addCar";
    /* 购物车列表 */
    public static final String getCarGoodsList = rootUrl + "/openapi/shop/goodsCar/list";
    /* 修改购物车 */
    public static final String updateCar = rootUrl + "/openapi/shop/goodsCar/updateCar";
    /* 删除购物车 */
    public static final String goodsCarDel = rootUrl + "/openapi/shop/goodsCar/goodsCarDel";
    /* 下单 */
    public static final String createOrder = rootUrl + "/openapi/shop/ordergoods/place";
    /* 订单支付 */
    public static final String payOrder = rootUrl + "/openapi/shop/ordergoods/pay";
    /* 订单列表 */
    public static final String getOrderList = rootUrl + "/openapi/shop/ordergoods/list";
    /* 确认收货 */
    public static final String determine = rootUrl + "/openapi/shop/ordergoods/determine";
    /* 取消订单 */
    public static final String cancelorder = rootUrl + "/openapi/shop/ordergoods/cancelorder";
    /* 申请退款 */
    public static final String refund = rootUrl + "/openapi/shop/ordergoods/refund";
    /* 评价 */
    public static final String comment = rootUrl + "/openapi/shop/ordergoods/evaluation";
    /* 搜索热词 */
    public static final String searchHot = rootUrl + "/openapi/video/piaon/selecthotsearchlist";

    /************************************** 琴友圈  ****************************************************/
    /* h5活动发布 */
    public static final String partakeactivity = rootUrl + "/openapi/video/vote/partakeactivity";
    /* 发布琴友圈 */
    public static final String addpublishtopic = rootUrl + "/openapi/video/topiccontent/publishtopic";
    /* 话题列表-下拉框使用 */
    public static final String getTopicList = rootUrl + "/openapi/video/topic/selecttopiclist";
    /* 活动列表-下拉框使用 */
    public static final String getHdList = rootUrl + "/openapi/video/topic/selectactivitylist";
    /* 琴友圈内容列表 */
    public static final String getCircleList = rootUrl + "/openapi/video/topiccontent/list";
    /* 琴友圈详情 */
    public static final String getCircleDetail = rootUrl + "/openapi/video/topiccontent/getdetail";
    /* 琴友圈详情评论 */
    public static final String getCirclecommentlist = rootUrl + "/openapi/video/topiccontent/selectcommentlist";
    /* 评论琴友圈内容 */
    public static final String setComment = rootUrl + "/openapi/video/topiccontent/comment";
    /* 话题列表 */
    public static final String getTopicList1 = rootUrl + "/openapi/video/topic/list";
    /* 话题详情 */
    public static final String getTopicDetail = rootUrl + "/openapi/video/topic/gettopicdetail";
    /* 话题详情-话题内容列表 */
    public static final String getCircleList1 = rootUrl + "/openapi/video/topic/selecttopiccontentlist";
    /* 我的收藏-琴友圈列表 */
    public static final String getCollectCircleList = rootUrl + "/openapi/video/collect/selecttopiclist";
    /* 我的收藏-视频列表 */
    public static final String getCollectVideoList = rootUrl + "/openapi/video/collect/selectvideolist";
    /* 我的收藏-商品列表 */
    public static final String getCollectGoodList = rootUrl + "/openapi/shop/collect/list";
    /* 搜索用户 */
    public static final String searchuserinfo = rootUrl + "/openapi/video/topiccontent/searchuserinfo";
    /* 琴友圈回复评论 */
    public static final String replycomment = rootUrl + "/openapi/video/topiccontent/replycomment";
    /* 琴友圈二级评论 */
    public static final String getSelecttwocommentlist = rootUrl + "/openapi/video/topiccontent/selecttwocommentlist";
    /* 删除评论 */
    public static final String deletecomment = rootUrl + "/openapi/video/topiccontent/deletecomment";



    /************************************** 个人主页  ****************************************************/
    /* 个人主页 */
    public static final String getUserHomeInfo = rootUrl + "/openapi/video/home/getuserhomeinfo";
    /* 设置-编辑用户信息 */
    public static final String updateUserInfo = rootUrl + "/openapi/video/home/updateuserinfo";
    /* 用户标签列表 */
    public static final String getLabelList = rootUrl + "/openapi/video/home/selectlabellist";
    /* 添加用户标签 */
    public static final String addLabel = rootUrl + "/openapi/video/home/addlabel";
    /* 删除用户标签 */
    public static final String deleteLabel = rootUrl + "/openapi/video/home/deleteLabel";
    /* 主页（琴友圈内容列表） */
    public static final String getPersonalCircleList = rootUrl + "/openapi/video/home/selecttopiccontentlist";
    /* 主页-视频列表 */
    public static final String getPersonalVideoList = rootUrl + "/openapi/video/home/selectvideolist";
    /* 主页-话题列表 */
    public static final String getPersonalTopicList = rootUrl + "/openapi/video/home/selecttopiclist";




    /************************************** 个人中心  ****************************************************/
    /* 成为老师 */
    public static final String addTeacher = rootUrl + "/openapi/shop/teacherCoure/insert";
    /* 校区详情-课程列表 */
    public static final String getCourseList = rootUrl + "/openapi/shop/course/list";
    /* 公告课程列表 */
    public static final String getCourinfoList = rootUrl + "/openapi/shop/course/courinfo";
    /* 课程报名 */
    public static final String addCourse = rootUrl + "/openapi/shop/course/insert";
    /* 我的课程 */
    public static final String getMyCourse = rootUrl + "/openapi/shop/offline/courselist";
    /* 任务列表 */
    public static final String getTaskList = rootUrl + "/openapi/video/task/selecttasklist";
    /* 领取奖励 */
    public static final String receive = rootUrl + "/openapi/video/task/receive";
    /* 签到 */
    public static final String signin = rootUrl + "/openapi/video/task/signin";
    /* 查询连续签到天数 */
    public static final String getcontinuesign = rootUrl + "/openapi/video/task/getcontinuesign";
    /* 获取提现比例 */
    public static final String proportion = rootUrl + "/openapi/user/wallet/proportion";
    /* 提现 */
    public static final String withdraw = rootUrl + "/openapi/user/wallet/withdraw";
    /* 充值下单 */
    public static final String recharge = rootUrl + "/openapi/shop/order/svae";
    /* 充值下单 */
    public static final String recharge1 = rootUrl + "/openapi/shop/order/pay";
    /* 预约课程 */
    public static final String appointment = rootUrl + "/openapi/shop/teacherCoure/appointment";
    /* 线下已预约课程 */
    public static final String getOfflinelist = rootUrl + "/openapi/shop/offline/list";
    /* 添加课堂资料 */
    public static final String addCourseData = rootUrl + "/openapi/shop/offline/input";
    /* 线下上课记录 */
    public static final String getHavelist = rootUrl + "/openapi/shop/offline/havelist";
    /* 课程评价 */
    public static final String evaluation = rootUrl + "/openapi/shop/offline/evaluation";
    /* 查看课堂资料 */
    public static final String detailinfo = rootUrl + "/openapi/shop/offline/detailinfo";
    /* 老师列表 */
    public static final String getTeacherlist = rootUrl + "/openapi/shop/fication/teacherlist";
    /* 评价列表 */
    public static final String getCommentlist = rootUrl + "/openapi/shop/teacherCoure/evaluationlist";
    /* 老师详情 */
    public static final String getteacherdetail = rootUrl + "/openapi/shop/fication/teacherdetail";
    /* 近期授课老师 */
    public static final String getrecentlylist = rootUrl + "/openapi/shop/teacherCoure/recentlylist";
    /* 我的打赏/我的礼物 */
    public static final String getGiftRecordList = rootUrl + "/openapi/live/record/selectpresentrecordlist";
    /* 支付记录 */
    public static final String getPayList = rootUrl + "/openapi/live/record/selectpaylist";
    /* 关注列表 */
    public static final String getFocusList = rootUrl + "/openapi/video/focus/selectfocuslist";
    /* 课堂资料列表 */
    public static final String getSelectclassroomdatalist = rootUrl + "/openapi/shop/offline/selectclassroomdatalist";
    /* 粉丝列表 */
    public static final String getFansList = rootUrl + "/openapi/video/focus/selectfanslist";
    /* 取消课程预约 */
    public static final String cancellation = rootUrl + "/openapi/shop/offline/cancellation";
    /* 获取本月可取消次数 */
    public static final String cancelled = rootUrl + "/openapi/shop/offline/cancelled";
    /* 已取消预约课程 */
    public static final String getCancelhavelist = rootUrl + "/openapi/shop/offline/cancelhavelist";
    /* 修改密码 */
    public static final String updatepassword = rootUrl + "/openapi/user/userinfo/updatepassword";
    /* 设置支付密码 */
    public static final String updatepaypassword = rootUrl + "/openapi/user/userinfo/updatepaypassword";
    /* 修改手机 */
    public static final String updatemobile = rootUrl + "/openapi/user/userinfo/updatemobile";
    /* 我的课程-线上课程列表 */
    public static final String getOnLineCourseList = rootUrl + "/openapi/video/course/selectmycourselist";
    /* 我的钱包 */
    public static final String getUserWallet = rootUrl + "/openapi/user/wallet/userwallet";
    /* 钱包明细 */
    public static final String getWalletList = rootUrl + "/openapi/user/wallet/list";
    /* 金豆转增 */
    public static final String turnadd = rootUrl + "/openapi/user/wallet/turnadd";
    /* 消息列表 */
    public static final String getMessageList = rootUrl + "/openapi/user/message/list";
    /* 消息列表 */
    public static final String updateMessage = rootUrl + "/openapi/user/message/update";
    /* 消息数量*/
    public static final String getMessagenumber = rootUrl + "/openapi/user/message/number";
    /* 消息一键已读*/
    public static final String messageread = rootUrl + "/openapi/user/message/read";
    /* 我的体验卡列表*/
    public static final String getMyCardList= rootUrl + "/openapi/shop/experience/list";
    /* 转送给好友*/
    public static final String getforwarding= rootUrl + "/openapi/shop/experience/forwarding";
    /* 转送详情*/
    public static final String getforwardingdetail= rootUrl + "/openapi/shop/experience/forwardingdetail";
    /* 举报类型*/
    public static final String selectreporttypelist= rootUrl + "/openapi/video/home/selectreporttypelist";
    /* 举报*/
    public static final String report= rootUrl + "/openapi/video/home/report";
    /* 投诉建议*/
    public static final String usercomplain= rootUrl + "/openapi/video/home/usercomplain";
    /* 老师分类课程 */
    public static final String gettecheCaslist = rootUrl + "/openapi/shop/teacherCoure/techeCaslist";
    /* 分类课程下的学生 */
    public static final String gettecheCasinfolist = rootUrl + "/openapi/shop/teacherCoure/techeCasinfolist";
    /* 查询老师分类课程下的学生的上课记录 */
    public static final String gettecheCasinfodetial = rootUrl + "/openapi/shop/teacherCoure/techeCasinfodetial";
    /* 课程分类 */
    public static final String getCourescatslist = rootUrl + "/openapi/shop/courescats/list";
    /* 体验卡预约记录 */
    public static final String getexperiencelist = rootUrl + "/openapi/shop/experience/experiencelist";
    /* 查询暂停卡使用次数 */
    public static final String selectsuspended = rootUrl + "/openapi/shop/offline/selectsuspended";
    /* 暂停卡 */
    public static final String suspended = rootUrl + "/openapi/shop/offline/suspended";
    /* 邀请好友合种 */
    public static final String invitefriends = rootUrl + "/openapi/shop/seedlings/information";
    /* 好友同意拒绝合种 */
    public static final String friendconsent = rootUrl + "/openapi/shop/seedlings/friendconsent";
    /* s删除朋友圈 */
    public static final String deletepyq = rootUrl + "/openapi/video/topiccontent/delete";




    /* 查询banner列表 */
    public static final String getBannerList = rootUrl + "/openapi/video/video/selectbannerlist";
    /* 文件上传 */
    public static final String uploadFile = rootUrl + "/general/os/cos/upload";
    /* 点赞 */
    public static final String setGive = rootUrl + "/openapi/video/give/operation";
    /* 关注/取消关注 */
    public static final String setFocus = rootUrl + "/openapi/video/focus/operation";
    /* 收藏/取消收藏 */
    public static final String setcollect = rootUrl + "/openapi/video/collect/operation";
    /* 查询协议 */
    public static final String getAgreement = rootUrl + "/openapi/live/index/getagreement";
    /* 分享 */
    public static final String sharing = rootUrl + "/openapi/video/share/sharing";


}
