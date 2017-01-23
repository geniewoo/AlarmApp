package com.sungwoo.boostcamp.sungwooalarmapp;

import io.realm.RealmObject;

/**
 * Created by psw10 on 2017-01-23.
 */

public class AlarmRepo extends RealmObject {
    private int hour, minute;
    private String dayString;
    private boolean isActive;

    public AlarmRepo() {
    }

    public AlarmRepo(int hour, int minute, String dayString){
        this.hour = hour;
        this.minute = minute;
        this.dayString = dayString;
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

    public String getDayString() {
        return dayString;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
    }
}
