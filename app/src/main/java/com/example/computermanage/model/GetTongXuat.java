package com.example.computermanage.model;

public class GetTongXuat {
    public String mshd;
    public int slXuat;
    public double dgXuat;
    public int giamgia;

    public GetTongXuat() {
    }

    public GetTongXuat(String mshd, int slXuat, double dgXuat, int giamgia) {
        this.mshd = mshd;
        this.slXuat = slXuat;
        this.dgXuat = dgXuat;
        this.giamgia = giamgia;
    }

    public String getMshd() {
        return mshd;
    }

    public void setMshd(String mshd) {
        this.mshd = mshd;
    }

    public int getSlXuat() {
        return slXuat;
    }

    public void setSlXuat(int slXuat) {
        this.slXuat = slXuat;
    }

    public double getDgXuat() {
        return dgXuat;
    }

    public void setDgXuat(double dgXuat) {
        this.dgXuat = dgXuat;
    }

    public int getGiamgia() {
        return giamgia;
    }

    public void setGiamgia(int giamgia) {
        this.giamgia = giamgia;
    }
}
