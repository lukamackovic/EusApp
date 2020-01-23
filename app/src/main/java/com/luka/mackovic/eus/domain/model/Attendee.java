package com.luka.mackovic.eus.domain.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Attendee {

    private String name;

    private String email;

    @ServerTimestamp
    private Date assignedDate;

    public Attendee(String name, String email, Date assignedDate) {
        this.name = name;
        this.email = email;
        this.assignedDate = assignedDate;
    }

    public Attendee() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }
}
