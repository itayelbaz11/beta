package com.example.beta;

public class User {
    private String name, email, uid;
    private Boolean active;

    public User (){}
    public User (String name, String email, String uid, Boolean active) {
        this.name=name;
        this.email=email;
        this.uid=uid;
        this.active=active;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email=email;
    }


    public String getUid() { return uid; }
    public void setUid(String uid) {
        this.uid=uid;
    }


    public Boolean getActive() { return active; }
    public void setActive(Boolean active) {
        this.active=active;
    }
}
