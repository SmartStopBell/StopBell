package com.example.smartstopbellproject;

public class ListViewItem {
    //아이템에 출력될 데이터를 위한 클래스를 정의
    private String stopId;
    private String stopname;
    private int position;


    public ListViewItem(String stopId, String stopname, int position) {
        this.stopId = stopId;
        this.stopname = stopname;
        this.position = position;
    }


    public String getStopname() {
        return stopname;
    }

    public void setStopname(String stopname) {
        this.stopname = stopname;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStopId() { return stopId; }

    public void setStopId(String stopId) { this.stopId = stopId; }
}
