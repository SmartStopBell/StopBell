package com.example.smartstopbellproject;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    //아이템에 출력될 데이터를 위한 클래스를 정의

    private Drawable img;
    private String stopName;

    public Drawable getImg() {
        return img;
    }

    public String getStopName() {
        return stopName;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
