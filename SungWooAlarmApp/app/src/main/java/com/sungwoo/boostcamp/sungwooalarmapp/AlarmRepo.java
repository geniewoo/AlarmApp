package com.sungwoo.boostcamp.sungwooalarmapp;

import io.realm.RealmObject;

/**
 * Created by psw10 on 2017-01-23.
 */

public class AlarmRepo extends RealmObject {
    private int hour, minute;
    private String dayOfWeekStr;
    private boolean isActive;


    public AlarmRepo() {
    }

    public AlarmRepo(int hour, int minute, String dayOfWeekStr, boolean isActive) {
        this.hour = hour;
        this.minute = minute;
        this.dayOfWeekStr = dayOfWeekStr;
        this.isActive = isActive;
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
}
