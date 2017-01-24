package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlarmDetailActivity extends AppCompatActivity {
    int hour;
    int minute;
    String dayOfWeekStr = "XXXXXXX";

    Realm realm;

    boolean isCreate;

    @BindView(R.id.detailTBLeft_TV)
    TextView detailTBLeft_TV;
    @BindView(R.id.detailTBRight_TV)
    TextView detailTBRight_TV;
    @BindView(R.id.timePicker)
    TimePicker detailTimePicker;
    @BindViews({R.id.day_sun, R.id.day_mon, R.id.day_tue, R.id.day_wed, R.id.day_thu, R.id.day_fri, R.id.day_sat})
    List<TextView> day_TVs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);

        realm = Realm.getDefaultInstance();

        ButterKnife.bind(this);

        detailTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                whenTimeChanged(i, i1);
            }
        });
        for(int i = 0 ; i < day_TVs.size() ; i ++ ){
            TextView textView = day_TVs.get(i);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int)view.getTag();
                    if (dayOfWeekStr.charAt(index)=='X') {
                        dayOfWeekStr = dayOfWeekStr.substring(0, index) + "O" + dayOfWeekStr.substring(index +1);
                        changeDayColor(index, true);
                    }else {
                        dayOfWeekStr = dayOfWeekStr.substring(0, index) + "X" + dayOfWeekStr.substring(index +1);
                        changeDayColor(index, false);
                    }
                }
            });
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_isCreate))) {
            if (intent.getBooleanExtra(getString(R.string.intent_isCreate), false)) {
                isCreate = true;
                detailTBLeft_TV.setText(R.string.alarm_list_cancel);
                detailTBRight_TV.setText(R.string.alarm_list_confirm);

                detailTBLeft_TV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailCancelClicked();
                    }
                });

                detailTBRight_TV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(AlarmDetailActivity.this, "왜ㅑ 안대노", Toast.LENGTH_SHORT).show();
                        detailConfirmTVClicked();
                    }
                });

                initSettings();
            } else {
                isCreate = false;
                detailTBLeft_TV.setText(R.string.alarm_list_cancel);
                detailTBRight_TV.setText(R.string.alarm_list_change);

                if(intent.hasExtra(getString(R.string.intent_alarmIndex))){
                    int index = intent.getIntExtra(getString(R.string.intent_alarmIndex), -1);
                    if(index != -1){
                        List<AlarmRepo> alarmRepos = realm.where(AlarmRepo.class).findAll();
                        Log.d("alarmDetailActivity", "index : " + index + " num : " + alarmRepos.size());
                        AlarmRepo alarmRepo = alarmRepos.get(index);
                        changeSetting(alarmRepo);
                    }else{
                        Toast.makeText(this, "error!!!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        } else {
            detailTBLeft_TV.setText(R.string.expected_error);
            detailTBRight_TV.setText(R.string.expected_error);
        }

    }

    void whenTimeChanged(int h, int m) {
        hour = h;
        minute = m;
    }

    void detailCancelClicked() {
        finish();
    }

    void detailConfirmTVClicked() {
        insertDataBase();
        setAlarm();
        Toast.makeText(this, "데이터베이스 넣음", Toast.LENGTH_SHORT).show();
        finish();
    }

    void setAlarm() {

    }

    void insertDataBase() {
        AlarmRepo alarmRepo = makeAlarmRepo();

        realm.beginTransaction();
        realm.insert(alarmRepo);
        realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    AlarmRepo makeAlarmRepo() {
        AlarmRepo alarmRepo = new AlarmRepo(hour, minute, dayOfWeekStr, true);
        return alarmRepo;
    }

    void initSettings() {
        if (Build.VERSION.SDK_INT >= 23) {
            hour = detailTimePicker.getHour();
            minute = detailTimePicker.getMinute();
        } else {
            hour = detailTimePicker.getCurrentHour();
            minute = detailTimePicker.getCurrentMinute();
        }

        Calendar c = Calendar.getInstance();
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dayOfWeekStr = "OXXXXXX";
                changeDayColor(0, true);
                break;
            case Calendar.MONDAY:
                dayOfWeekStr = "XOXXXXX";
                changeDayColor(1, true);
                break;
            case Calendar.TUESDAY:
                dayOfWeekStr = "XXOXXXX";
                changeDayColor(2, true);
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekStr = "XXXOXXX";
                changeDayColor(3, true);
                break;
            case Calendar.THURSDAY:
                dayOfWeekStr = "XXXXOXX";
                changeDayColor(4, true);
                break;
            case Calendar.FRIDAY:
                dayOfWeekStr = "XXXXXOX";
                changeDayColor(5, true);
                break;
            case Calendar.SATURDAY:
                dayOfWeekStr = "XXXXXXO";
                changeDayColor(6, true);
                break;
        }
    }

    void changeDayColor(int index, boolean isActive) {
        if(isActive) {
            day_TVs.get(index).setBackgroundColor(Color.MAGENTA);
        }else{
            day_TVs.get(index).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    void changeSetting(AlarmRepo alarmRepo){
        Log.d("changeSetting", "changeSetting");
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("changeSetting", "changeSetting1");
            detailTimePicker.setHour(alarmRepo.getHour());
            detailTimePicker.setMinute(alarmRepo.getMinute());
        }
        dayOfWeekStr = alarmRepo.getDayOfWeekStr();

        for(int i = 0 ; i < day_TVs.size() ; i ++ ){
            if(dayOfWeekStr.charAt(i) == 'O'){
                changeDayColor(i, true);
            }
        }
    }
}
