package com.example.smartstopbellproject;

import java.util.HashMap;
import java.util.Map;

public class Quit {

    private String bId;
    private int count;
    private String sName;
    private Map<String, Boolean> stars = new HashMap<>();

    public Quit() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Quit(String bId, int count, String sName) {
        this.bId = bId;
        this.count = count;
        this.sName = sName;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}
