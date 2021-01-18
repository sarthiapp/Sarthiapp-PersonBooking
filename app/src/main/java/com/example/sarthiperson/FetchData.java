package com.example.sarthiperson;

import java.util.Comparator;

public class FetchData {
    String id;
    String name;
    String purl;
    int rating;
    int age;
    public FetchData()
    {

    }

    public FetchData(String id, String name, int rating,String purl,int age) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.purl=purl;
        this.age=age;
    }


    @Override
    public String toString() {
        return "FetchData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", purl='" + purl + '\'' +
                ", rating=" + rating +
                '}';
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
