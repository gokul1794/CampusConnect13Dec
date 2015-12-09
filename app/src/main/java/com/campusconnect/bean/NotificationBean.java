package com.campusconnect.bean;

/**
 * Created by RK on 22-09-2015.
 */
public class NotificationBean {
    protected String group_name;
    private String type;
    private String groupId;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
