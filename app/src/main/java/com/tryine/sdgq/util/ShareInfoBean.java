package com.tryine.sdgq.util;public class ShareInfoBean {    private String logo;//	图标（h5或小程序会返回）    private String title;//	标题（h5或小程序会返回）    private String imgUrl;//	分享图片url（moduleType 传 10 只返回此字段）    private String content;//	链接内容（h5返回）    private String webUrl;//	链接地址h5返回）    private String webpageUrl;//	低版本微信网页链接（小程序返回）    private String userName;//	小程序username（小程序返回）    private String path;//	小程序页面的路径（小程序返回）    private String dataType;//0 - 小程序， 1 - h5， 2 - 图片url    private String thumImage;    public String getThumImage() {        return thumImage;    }    public void setThumImage(String thumImage) {        this.thumImage = thumImage;    }    public String getLogo() {        return logo;    }    public void setLogo(String logo) {        this.logo = logo;    }    public String getTitle() {        return title;    }    public void setTitle(String title) {        this.title = title;    }    public String getImgUrl() {        return imgUrl;    }    public void setImgUrl(String imgUrl) {        this.imgUrl = imgUrl;    }    public String getContent() {        return content;    }    public void setContent(String content) {        this.content = content;    }    public String getWebUrl() {        return webUrl;    }    public void setWebUrl(String webUrl) {        this.webUrl = webUrl;    }    public String getWebpageUrl() {        return webpageUrl;    }    public void setWebpageUrl(String webpageUrl) {        this.webpageUrl = webpageUrl;    }    public String getUserName() {        return userName;    }    public void setUserName(String userName) {        this.userName = userName;    }    public String getPath() {        return path;    }    public void setPath(String path) {        this.path = path;    }    public String getDataType() {        return dataType;    }    public void setDataType(String dataType) {        this.dataType = dataType;    }}