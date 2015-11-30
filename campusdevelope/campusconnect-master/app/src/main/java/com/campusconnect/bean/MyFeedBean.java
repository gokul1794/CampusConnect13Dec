package com.campusconnect.bean;

import java.io.Serializable;

/**
 * Created by canopus on 30/11/15.
 */
public class MyFeedBean implements Serializable {

    String name;
    String image;
    String description;
    String views;
    String pid;
    String collegeid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    String title;
    String timestamp;
    String kind;
    String eventCreator;



/*
    event_creator: "Rohit Joseph"
    description: "Fuck you "
    views: "0"
    photo: "None"
    club_id: "Institute of Engineers"
    pid: "5206065687822336"
    timestamp: "2012-10-10 12:20:12"
    title: "Hey"
    collegeId: "National Institute of Technology Karnataka"
    kind: "clubs#resourcesItem*/

}
