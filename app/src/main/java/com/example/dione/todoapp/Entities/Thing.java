package com.example.dione.todoapp.Entities;

import com.orm.SugarRecord;

/**
 * Created by dione on 05/08/2016.
 */
public class Thing extends SugarRecord {
    private String description;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int pendingIntentId;
    public Thing(){}
    public Thing(String description, int year, int month, int date, int hour, int minute, int pendingIntentId){
        this.description = description;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.pendingIntentId = pendingIntentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
