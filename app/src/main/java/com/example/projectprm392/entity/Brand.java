package com.example.projectprm392.entity;

public class Brand {
    private int BrandID;
    private String BrandName;

    public Brand(){

    }

    public Brand(int brandID, String brandName) {
        BrandID = brandID;
        BrandName = brandName;
    }

    public int getBrandID() {
        return BrandID;
    }

    public void setBrandID(int brandID) {
        BrandID = brandID;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }
}
