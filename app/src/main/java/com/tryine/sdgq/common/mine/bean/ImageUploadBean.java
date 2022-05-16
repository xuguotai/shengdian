package com.tryine.sdgq.common.mine.bean;

/**
 * 作者：qujingfeng
 * 创建时间：2020.06.16 11:04
 */

public class ImageUploadBean {
    private String localUrl;
    private String url;
    private int resourceId = 0;//上传的图片


    //上传后返回的数据
    private String checkResult;
    private String checkReultMsg;
    private String fileUrl;
    private String height;
    private String width;

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckReultMsg() {
        return checkReultMsg;
    }

    public void setCheckReultMsg(String checkReultMsg) {
        this.checkReultMsg = checkReultMsg;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
