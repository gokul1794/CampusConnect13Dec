package com.campusconnect.bean;

import java.io.Serializable;

/**
 * Created by canopus on 27/11/15.
 */
public class GroupBean implements Serializable {

    private String description;
    private String admin;
    private String clubId;
    private String Abb;
    private String name;
    private String follow;

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getAbb() {
        return Abb;
    }

    public void setAbb(String abb) {
        Abb = abb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
