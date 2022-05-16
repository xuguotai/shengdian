package com.tryine.sdgq.common.mine.bean;

/**
 * @author: zhangshuaijun
 * @time: 2021-12-22 15:01
 */
public class TaskBean {

    private String id;//任务id
    private String name;//任务名称
    private String ruleRemark;//规则说明
    private String beanCount;//奖励银豆数
    private String taskStatus;//任务状态 0-未完成 1-领取奖励 2-已领取

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleRemark() {
        return ruleRemark;
    }

    public void setRuleRemark(String ruleRemark) {
        this.ruleRemark = ruleRemark;
    }

    public String getBeanCount() {
        return beanCount;
    }

    public void setBeanCount(String beanCount) {
        this.beanCount = beanCount;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
