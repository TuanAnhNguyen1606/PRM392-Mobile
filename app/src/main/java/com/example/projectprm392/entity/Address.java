package com.example.projectprm392.entity;

public class Address {
    private int Id;
    private int UserId;
    private String FirstName;
    private String MiddleName;
    private String LastName ;
    private String Phone ;
    private String Province ;
    private String District ;
    private String Ward ;
    private String Detail;

    public Address() {
    }

    public Address(int id, int userId, String firstName, String middleName, String lastName, String phone, String province, String district, String ward, String detail) {
        Id = id;
        UserId = userId;
        FirstName = firstName;
        MiddleName = middleName;
        LastName = lastName;
        Phone = phone;
        Province = province;
        District = district;
        Ward = ward;
        Detail = detail;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
}
