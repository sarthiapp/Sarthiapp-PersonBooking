package com.example.sarthiperson;

public class DatabasePersonInformationStoreHolder {
    int age;
    String name,purl;
    int rating;

    public DatabasePersonInformationStoreHolder(int age, String name, String purl, int rating) {
        this.age = age;
        this.name = name;
        this.purl = purl;
        this.rating = rating;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
