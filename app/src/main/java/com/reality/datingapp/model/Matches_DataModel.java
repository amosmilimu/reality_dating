package com.reality.datingapp.model;

import java.util.Date;

public class Matches_DataModel {

    String user_matches;
    Date user_matched;

    public Matches_DataModel() {
    }

    public Matches_DataModel(String user_matches, Date user_matched) {
        this.user_matches = user_matches;
        this.user_matched = user_matched;
    }

    public String getUser_matches() {
        return user_matches;
    }

    public void setUser_matches(String user_matches) {
        this.user_matches = user_matches;
    }

    public Date getUser_matched() {
        return user_matched;
    }

    public void setUser_matched(Date user_matched) {
        this.user_matched = user_matched;
    }
}
