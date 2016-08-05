package com.example.dione.todoapp.Entities;

import com.orm.SugarRecord;

/**
 * Created by dione on 05/08/2016.
 */
public class User extends SugarRecord {
    private String firstName;
    private String lastName;
    private String password;

    public User(){

    }

    public User(String firstName, String lastName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
