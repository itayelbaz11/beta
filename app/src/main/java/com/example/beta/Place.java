package com.example.beta;

public class Place {
    private int x,y;
    String name,photo,description;

    public Place() {
    }

    public Place(int x, int y, String name, String photo, String description) {
        this.x = x;
        this.y = y;
        this.name = name;
        if(photo!=null){
            this.photo = photo;
        }
        else{this.photo="";}
        if(description!=null){
        this.description = description;}
        else{
            this.description="";
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
