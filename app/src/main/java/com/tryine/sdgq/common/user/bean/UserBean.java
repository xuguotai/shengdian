package com.tryine.sdgq.common.user.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-09 17:17
 */
public class UserBean {

    private String id;
    private String sex; // 0：未知、1：男、2：女
    private String avatar;//用户头像
    private String groupId;//会员组ID
    private String userName;//用户名
    private String nickName;//用户名

    private String mobile;//手机号码
    private String email;//电子邮箱
    private String type;//用户类型1普通用户2、学生3、老师
    private String card;//身份证号码
    private String infoImg;//身份证信息面图片地址
    private String emblemImg;//身份证国徽面图片地址
    private String isCertification;//是否实名认证，0、未认证1、已认证
    private String realName;//真实姓名
    private String identityUrl;//身份证图片url
    private String silverBean;//银豆
    private String goldenBean;//金豆
    private String createTime;
    private String payPassword;
    private String inviteCode;

    private String trueName; //真实姓名
    private String idCard;   //身份证号码
    private String faceUrl;  //人脸照片
    private String trueStatus; //是否实名认证 是1
    private int isVip;//是否vip
    private int level;//等级

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getTrueStatus() {
        return trueStatus;
    }

    public void setTrueStatus(String trueStatus) {
        this.trueStatus = trueStatus;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getInfoImg() {
        return infoImg;
    }

    public void setInfoImg(String infoImg) {
        this.infoImg = infoImg;
    }

    public String getEmblemImg() {
        return emblemImg;
    }

    public void setEmblemImg(String emblemImg) {
        this.emblemImg = emblemImg;
    }

    public String getIsCertification() {
        return isCertification;
    }

    public void setIsCertification(String isCertification) {
        this.isCertification = isCertification;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityUrl() {
        return identityUrl;
    }

    public void setIdentityUrl(String identityUrl) {
        this.identityUrl = identityUrl;
    }

    public String getSilverBean() {
        return silverBean;
    }

    public void setSilverBean(String silverBean) {
        this.silverBean = silverBean;
    }

    public String getGoldenBean() {
        return goldenBean;
    }

    public void setGoldenBean(String goldenBean) {
        this.goldenBean = goldenBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
