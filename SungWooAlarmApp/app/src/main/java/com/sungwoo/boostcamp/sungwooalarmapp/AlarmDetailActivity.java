package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String dayStr;
    int dayIndex;

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

        ButterKnife.bind(this);

        detailTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                whenTimeChanged(i, i1);
            }
        });

        for (TextView textView : day_TVs) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.day_sun:
                            dayStr = getString(R.string.day_sunday);
                            changeDayColor(0);
                            break;
                        case R.id.day_mon:
                            dayStr = getString(R.string.day_monday);
                            changeDayColor(1);
                            break;
                        case R.id.day_tue:
                            dayStr = getString(R.string.day_tuesday);
                            changeDayColor(2);
                            break;
                        case R.id.day_wed:
                            dayStr = getString(R.string.day_wednesday);
                            changeDayColor(3);
                            break;
                        case R.id.day_thu:
                            dayStr = getString(R.string.day_thursday);
                            changeDayColor(4);
                            break;
                        case R.id.day_fri:
                            dayStr = getString(R.string.day_friday);
                            changeDayColor(5);
                            break;
                        case R.id.day_sat:
                            dayStr = getString(R.string.day_saturday);
                            changeDayColor(6);
                            break;
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
            }
        } else {
            detailTBLeft_TV.setText(R.string.expected_error);
            detailTBRight_TV.setText(R.string.expected_error);
        }

        realm = Realm.getDefaultInstance();
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
        AlarmRepo alarmRepo = new AlarmRepo(hour, minute, dayStr);
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
        dayIndex = 0;
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dayStr = getString(R.string.day_sunday);
                changeDayColor(0);
                break;
            case Calendar.MONDAY:
                dayStr = getString(R.string.day_monday);
                changeDayColor(1);
                break;
            case Calendar.TUESDAY:
                dayStr = getString(R.string.day_tuesday);
                changeDayColor(2);
                break;
            case Calendar.WEDNESDAY:
                dayStr = getString(R.string.day_wednesday);
                changeDayColor(3);
                break;
            case Calendar.THURSDAY:
                dayStr = getString(R.string.day_thursday);
                changeDayColor(4);
                break;
            case Calendar.FRIDAY:
                dayStr = getString(R.string.day_friday);
                changeDayColor(5);
                break;
            case Calendar.SATURDAY:
                dayStr = getString(R.string.day_saturday);
                changeDayColor(6);
                break;
        }
    }

    void changeDayColor(int index){
        day_TVs.get(dayIndex).setBackgroundColor(Color.TRANSPARENT);
        dayIndex = index;
        day_TVs.get(dayIndex).setBackgroundColor(Color.MAGENTA);
    }
}
