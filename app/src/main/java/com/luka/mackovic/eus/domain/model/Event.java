package com.luka.mackovic.eus.domain.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class Event {

    private String id;

    private String image;

    private String title;

    private String description;

    private Long attendeesLimit;

    @ServerTimestamp
    private Date createdDate;

    @ServerTimestamp
    private Date startTime;

    @ServerTimestamp
    private Date registrationEndTime;

    private List<Attendee> attendees;

    private Event(String id,
                  String image,
                  String title,
                  String description,
                  Long attendeesLimit,
                  Date createdDate,
                  Date startTime,
                  Date registrationEndTime,
                  List<Attendee> attendees) {

        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.attendeesLimit = attendeesLimit;
        this.createdDate = createdDate;
        this.startTime = startTime;
        this.registrationEndTime = registrationEndTime;
        this.attendees = attendees;
    }

    public Event() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAttendeesLimit() {
        return attendeesLimit;
    }

    public void setAttendeesLimit(Long attendeesLimit) {
        this.attendeesLimit = attendeesLimit;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getRegistrationEndTime() {
        return registrationEndTime;
    }

    public void setRegistrationEndTime(Date registrationEndTime) {
        this.registrationEndTime = registrationEndTime;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    @Exclude
    public Long getRemainingSeats() {
        return attendees == null
                ? attendeesLimit
                : attendeesLimit - attendees.size();
    }

    public Attendee addAttendee(String userEmail) {
        if (attendees == null) {
            return null;
        }

        for (Attendee attendee : attendees) {
            if (userEmail.equals(attendee.getEmail())) {
                return attendee;
            }
        }

        return null;
    }
}