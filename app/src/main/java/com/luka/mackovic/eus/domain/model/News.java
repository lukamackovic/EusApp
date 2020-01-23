package com.luka.mackovic.eus.domain.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class News {

    private String id;

    private String title;

    private String content;

    @ServerTimestamp
    private Date createdDate;

    public News() {}

    public News(String id, String title, String content, Date createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
