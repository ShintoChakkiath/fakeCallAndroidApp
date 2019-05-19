package com.GoldenApp.fakecall;

public class Charactor {
    private String Name;
    private String Number;
    private int imgId;

    public String getName() {
        return this.Name;
    }

    public String getNumber() {
        return this.Number;
    }

    public int getImgId() {
        return this.imgId;
    }

    public Charactor(String name, String number, int imgId) {
        this.Name = name;
        this.Number = number;
        this.imgId = imgId;
    }
}
