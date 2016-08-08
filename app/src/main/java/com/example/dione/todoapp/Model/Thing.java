package com.example.dione.todoapp.Model;

import java.sql.Time;

/**
 * Created by dione on 05/08/2016.
 */
public class Thing {
    long id;
    private String toDoDescription;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int pendingIntentId;
    public Thing(long id, String toDoDescription, int year, int month, int date, int hour, int minute, int pendingIntentId){
        this.toDoDescription = toDoDescription;
        this.id = id;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.pendingIntentId = pendingIntentId;
    }

    public String getToDoDescription() {
        return toDoDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setToDoDescription(String toDoDescription) {
        this.toDoDescription = toDoDescription;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getPendingIntentId() {
        return pendingIntentId;
    }

    public void setPendingIntentId(int pendingIntentId) {
        this.pendingIntentId = pendingIntentId;
    }
}
