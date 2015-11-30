package com.campusconnect.bean;

import java.io.Serializable;

/**
 * Created by canopus on 27/11/15.
 */
public class CampusFeedBean implements Serializable {

    /* "event_creator":"Anirudh",
                       "description":"SAMPLE PHOTO",
                       "views":"0",
                       "photo":"/9j/45c8k3aLbSnKKvzKT2glvt0bVz/9k=",
                       "club_id":"Institute of Engineers",
                       "pid":"5073076857339904",
                       "timestamp":"2017-12-12 11:11:11",
                       "title":"PHOTO OP",
                       "collegeId":"National Institute of Technology Karnataka",
                       "kind":"clubs#resourcesItem"*/
    String eventCreator;
    String description;
    String views;
    String photo ;
    String clubid ;
    String pid ;
    String timeStamp;
    String title ;
    String collegeId;
    String kind ;


    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getClubid() {
        return clubid;
    }

    public void setClubid(String clubid) {
        this.clubid = clubid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }




}
