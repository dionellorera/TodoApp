package com.example.dione.todoapp.Model;

/**
 * Created by dione on 05/08/2016.
 */
public class Thing {
    long id;
    private String toDoDescription;
    public Thing(long id, String toDoDescription){
        this.toDoDescription = toDoDescription;
        this.id = id;
    }

    public String getToDoDescription() {
        return toDoDescription;
    }

    public long getId() {
        return id;
    }
}
