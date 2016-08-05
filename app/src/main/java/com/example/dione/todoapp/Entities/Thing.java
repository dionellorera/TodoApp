package com.example.dione.todoapp.Entities;

import com.orm.SugarRecord;

/**
 * Created by dione on 05/08/2016.
 */
public class Thing extends SugarRecord {
    private String description;
    public Thing(){}
    public Thing(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
