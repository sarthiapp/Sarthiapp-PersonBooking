package com.example.sarthiperson;

public class PersonFetchData {
    String userName;
    String userAddress;
    Long userPhoneNumber;
    public PersonFetchData()
    {

    }
    public PersonFetchData(String userName, String userAddress, Long userPhoneNumber) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Long getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(Long userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
