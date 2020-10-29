package com.reality.datingapp.model;

import java.util.Date;

public class Visitors_DataModel {

    public Date user_visited;
    String user_visitor;

    public Visitors_DataModel() {

    }

    public Visitors_DataModel(String user_visitor, Date user_visited) {
        this.user_visitor = user_visitor;
        this.user_visited = user_visited;
    }

    public String getUser_visitor() {
        return user_visitor;
    }

    public void setUser_visitor(String user_visitor) {
        this.user_visitor = user_visitor;
    }

    public Date getUser_visited() {
        return user_visited;
    }

    public void setUser_visited(Date user_visited) {
        this.user_visited = user_visited;
    }
}
