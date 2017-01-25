package com.sungwoo.boostcamp.sungwooalarmapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by psw10 on 2017-01-23.
 */

public class AlarmRepo extends RealmObject {
    @PrimaryKey
    private int id;
    private int hour, minute;
    private String dayOfWeekStr;
    private boolean isActive;
    private String memoStr;


    public AlarmRepo() {
    }

    public AlarmRepo(int id, int hour, int minute, String dayOfWeekStr, boolean isActive, String memoStr) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.dayOfWeekStr = dayOfWeekStr;
        this.isActive = isActive;
        this.memoStr = memoStr;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDayOfWeekStr() {
        return dayOfWeekStr;
    }

    public void setDayOfWeekStr(String dayOfWeekStr) {
        this.dayOfWeekStr = dayOfWeekStr;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemoStr() {
        return memoStr;
    }

    public void setMemoStr(String memoStr) {
        this.memoStr = memoStr;
    }
}
