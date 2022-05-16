package com.tryine.sdgq.common.circle.bean;

import java.io.Serializable;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-21 10:16
 */
public class LabelBean implements Serializable {

    private String id;//标签id
    private String userId;//
    private String name;//标签名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
