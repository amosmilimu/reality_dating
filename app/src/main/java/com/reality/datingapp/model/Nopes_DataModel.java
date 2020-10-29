package com.reality.datingapp.model;

import java.util.Date;

public class Nopes_DataModel {

    String user_nopes;
    Date user_noped;

    public Nopes_DataModel() {
    }

    public Nopes_DataModel(String user_nopes, Date user_noped) {
        this.user_nopes = user_nopes;
        this.user_noped = user_noped;
    }

    public String getUser_nopes() {
        return user_nopes;
    }

    public void setUser_nopes(String user_nopes) {
        this.user_nopes = user_nopes;
    }

    public Date getUser_noped() {
        return user_noped;
    }

    public void setUser_noped(Date user_noped) {
        this.user_noped = user_noped;
    }
}
