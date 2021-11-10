package com.example.smartstopbellproject;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    //아이템에 출력될 데이터를 위한 클래스를 정의

    private int img;
    private String stopName;


    public int getImg() {
        return img;
    }

    public String getStopName() {
        return stopName;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
