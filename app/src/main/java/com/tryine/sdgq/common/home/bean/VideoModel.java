package com.tryine.sdgq.common.home.bean;


import com.tencent.liteav.demo.superplayer.model.VipWatchModel;

import java.util.List;

import static com.tencent.liteav.demo.superplayer.SuperPlayerModel.PLAY_ACTION_AUTO_PLAY;

/**
 * Created by yuejiaoli on 2018/7/4.
 */

public class VideoModel {

    /**
     * 视频标题
     */
    public String title;



    /**
     *  从服务器拉取的封面图片
     */
    public String placeholderImage;


    /**
     * 用户设置图片的接口 如果是本地图片前面加file://
     */
    public String coverPictureUrl;

    /**
     * 视频时长
     */
    public int duration;

    /**
     * appId
     */
    public int appid;

    /**
     * 视频的fileid
     */
    public String fileid;

    /**
     * 签名字串
     */
    public String pSign;



    public int playAction = PLAY_ACTION_AUTO_PLAY;


    /**
     * VIDEO 不同清晰度的URL链接
     */
    public List<VideoPlayerURL> multiVideoURLs;
    public int                  playDefaultIndex; // 指定多码率情况下，默认播放的连接Index
    public VipWatchModel vipWatchModel = null;

    //feed流视频描述信息
    public String videoDescription     = null;
    public String videoMoreDescription = null;

    public static class VideoPlayerURL {

        public VideoPlayerURL() {
        }

        public VideoPlayerURL(String title, String url) {
            this.title = title;
            this.url = url;
        }

        /**
         * 视频标题
         */
        public String title;

        /**
         * 视频URL
         */
        public String url;

        @Override
        public String toString() {
            return "SuperPlayerUrl{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }



    private String id;//视频id
    private String userId;
    private String teacherId;//老师id
    private String teacherName;//老师姓名
    private String teacherHeadImg;//老师头像
    private String fileId;//视频文件id
    private String videoUrl;//视频路径
    private String coverUrl;// 视频封面
    private String videoTime;// 视频时长（秒）
    private String videoTimeStr;// 视频时长 时分秒
    private String videoSize;// 视频大小
    private String videoLength;// 视频长度
    private String videoWidth;// 视频宽度
    private String videoNo;// 视频序列号，为合集时用来标明是同一个合集
    private int lookTime;// 免费观看时长(秒)
    private String typeId;// 视频类型id
    private String videoType;// 视频种类 0-单个 1-合集
    private String goldenBean;// 所需金豆
    private String payType;// 付费类型 0-免费 1-付费
    private String pianoScore;// 关联琴谱（多个逗号分隔）
    private String orderBy;//章节顺序
    private String showType;//显示类型 0-列表显示（合集的第一条视频） 1-列表不显示（合集的其他视频）
    private String auditStatus;//审核状态 0-未审核 1-审核通过 2-驳回
    private String auditDesc;//审核描述
    private String auditTime;//审核时间
    private String isDelete;//是否删除 0-否 1-是
    private String isRecommend;//是否推荐 0否 1-是
    private String buyCount;//购买人数
    private String playCount;//播放数量
    private String isUnLock;//是否解锁了视频 0-否 1-是
    private String isFocus;//是否关注 0-否 1-是
    private String isCollect;//是否收藏 0-否 1-是
    private int collectCount;//s收藏数量
    private int beanType;//解锁方式 0金豆 1银豆

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getIsUnLock() {
        return isUnLock;
    }

    public void setIsUnLock(String isUnLock) {
        this.isUnLock = isUnLock;
    }

    public String getTeacherHeadImg() {
        return teacherHeadImg;
    }

    public void setTeacherHeadImg(String teacherHeadImg) {
        this.teacherHeadImg = teacherHeadImg;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public String getVideoTimeStr() {
        return videoTimeStr;
    }

    public void setVideoTimeStr(String videoTimeStr) {
        this.videoTimeStr = videoTimeStr;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(String videoWidth) {
        this.videoWidth = videoWidth;
    }

    public String getVideoNo() {
        return videoNo;
    }

    public void setVideoNo(String videoNo) {
        this.videoNo = videoNo;
    }

    public int getLookTime() {
        return lookTime;
    }

    public void setLookTime(int lookTime) {
        this.lookTime = lookTime;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getGoldenBean() {
        return goldenBean;
    }

    public void setGoldenBean(String goldenBean) {
        this.goldenBean = goldenBean;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPianoScore() {
        return pianoScore;
    }

    public void setPianoScore(String pianoScore) {
        this.pianoScore = pianoScore;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }
}
