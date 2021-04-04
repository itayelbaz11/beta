package com.example.beta;

import java.util.ArrayList;

public class Map {
    private String uid,mapname,uidcreator;
    private String path;
    private ArrayList<Place> places;
    boolean publicc;
    private int size;

    public Map(){}
    public Map(String uid,String mapname,String uidcreator,String path,ArrayList<Place> places,boolean publicc){
        this.uid=uid;
        this.mapname=mapname;
        this.uidcreator=uidcreator;
        this.path=path;
        this.places=places;
        this.publicc=publicc;

    }

    public String getUid() {
        return uid;
    }

    public String getMapname() {
        return mapname;
    }

    public String getUidcreator() {
        return uidcreator;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public boolean isPublicc() {
        return publicc;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public void setUidcreator(String uidcreator) {
        this.uidcreator = uidcreator;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public void setPublicc(boolean publicc) {
        this.publicc = publicc;
    }
}
