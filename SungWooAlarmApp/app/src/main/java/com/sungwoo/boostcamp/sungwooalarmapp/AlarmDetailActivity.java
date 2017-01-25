package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
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
    final static String TAG = AlarmDetailActivity.class.toString();
    int mId;
    int hour;
    int minute;
    String dayOfWeekStr = "XXXXXXX";
    List<AlarmRepo> mAlarmRepos = null;

    Realm realm;

    boolean isCreate;

    @BindView(R.id.detailMemo_ET)
    TextView detailMemo_ET;
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
        Log.d(TAG, "setOnTimeChangedListener");
        detailTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                whenTimeChanged(i, i1);
                Log.d(TAG, "onTimeChangedListener");
            }
        });
        for (int i = 0; i < day_TVs.size(); i++) {
            TextView textView = day_TVs.get(i);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = (int) view.getTag();
                    if (dayOfWeekStr.charAt(index) == 'X') {
                        dayOfWeekStr = dayOfWeekStr.substring(0, index) + "O" + dayOfWeekStr.substring(index + 1);
                        changeDayColor(index, true);
                    } else {
                        dayOfWeekStr = dayOfWeekStr.substring(0, index) + "X" + dayOfWeekStr.substring(index + 1);

                        if (!dayOfWeekStr.contains("O")) {
                            dayOfWeekStr = dayOfWeekStr.substring(0, index) + "O" + dayOfWeekStr.substring(index + 1);
                            return;
                        }
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
                        detailConfirmTVClicked();
                    }
                });

                initSettings();
            } else {
                isCreate = false;
                detailTBLeft_TV.setText(R.string.alarm_list_cancel);
                detailTBRight_TV.setText(R.string.alarm_list_change);

                if (intent.hasExtra(getString(R.string.intent_alarmIndex))) {
                    final int index = intent.getIntExtra(getString(R.string.intent_alarmIndex), -1);
                    if (index != -1) {
                        mAlarmRepos = realm.where(AlarmRepo.class).findAll();
                        Log.d("alarmDetailActivity", "index : " + index + " num : " + mAlarmRepos.size());
                        AlarmRepo alarmRepo = mAlarmRepos.get(index);
                        changeSetting(alarmRepo);
                    } else {
                        Toast.makeText(this, "error!!!!", Toast.LENGTH_SHORT).show();
                    }
                    detailTBLeft_TV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            detailCancelClicked();
                        }
                    });

                    detailTBRight_TV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            detailChangeTVClicked(index);
                            finish();
                        }
                    });
                }

            }
        } else {
            detailTBLeft_TV.setText(R.string.expected_error);
            detailTBRight_TV.setText(R.string.expected_error);
        }

    }

    void whenTimeChanged(int h, int m) {
        Log.d(TAG, "h : " + String.valueOf(h));
        hour = h;
        minute = m;
    }

    void detailCancelClicked() {
        finish();
    }

    void detailConfirmTVClicked() {
        insertDataBase();
        registWithAlarmManager();
        Toast.makeText(this, "알람 등록", Toast.LENGTH_SHORT).show();
        finish();
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
        int id = makeID();
        String memoStr = detailMemo_ET.getText().toString();
        AlarmRepo alarmRepo = new AlarmRepo(id, hour, minute, dayOfWeekStr, true, memoStr);
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
        if (isActive) {
            day_TVs.get(index).setBackgroundColor(Color.MAGENTA);
        } else {
            day_TVs.get(index).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    void changeSetting(AlarmRepo alarmRepo) {
        Log.d("changeSetting", "changeSetting");
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("changeSetting", "changeSetting1");
            detailTimePicker.setHour(alarmRepo.getHour());
            detailTimePicker.setMinute(alarmRepo.getMinute());
        }
        mId = alarmRepo.getId();
        dayOfWeekStr = alarmRepo.getDayOfWeekStr();
        detailMemo_ET.setText(alarmRepo.getMemoStr());

        for (int i = 0; i < day_TVs.size(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                changeDayColor(i, true);
            }
        }
    }

    void detailChangeTVClicked(int index) {
        if(mAlarmRepos != null){
            AlarmRepo alarmRepo = mAlarmRepos.get(index);
            if(realm!=null){
                realm.beginTransaction();
                alarmRepo.setHour(hour);
                alarmRepo.setMinute(minute);
                alarmRepo.setActive(true);
                alarmRepo.setDayOfWeekStr(dayOfWeekStr);
                alarmRepo.setMemoStr(detailMemo_ET.getText().toString());
                realm.commitTransaction();
            }

        }
    }

    int makeID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int new_ID = sharedPreferences.getInt(getString(R.string.pref_ID), 0);
        mId = new_ID;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        new_ID++;
        editor.putInt(getString(R.string.pref_ID), new_ID);
        Log.d("new_ID", String.valueOf(new_ID));
        editor.commit();
        return new_ID;
    }

    void registWithAlarmManager(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra(getString(R.string.intent_isStart), true);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        //TODO 이따가 여기다가 요일 O개수만큼 for를 돌려서 등록할것임 intent에 어떤 값을 넣어서 구별할것인지 추후 생각해보기
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                hour,
                minute);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.d(TAG, "Alarmregisted");
    }
}
