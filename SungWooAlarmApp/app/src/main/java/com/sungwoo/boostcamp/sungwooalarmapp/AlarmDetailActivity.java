package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.registWithAlarmManager;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.unregistWithAlarmManager;

public class AlarmDetailActivity extends AppCompatActivity {
    private final static String TAG = AlarmDetailActivity.class.toString();
    private int mId;
    private int hour;
    private int minute;
    private String dayOfWeekStr = "XXXXXXX";
    private List<AlarmRepo> mAlarmRepos = null;

    private Realm realm;

    private boolean isCreate;

    @BindView(R.id.detailMemo_ET)
    protected TextView detailMemo_ET;
    @BindView(R.id.detailTBLeft_TV)
    protected TextView detailTBLeft_TV;
    @BindView(R.id.detailTBRight_TV)
    protected TextView detailTBRight_TV;
    @BindView(R.id.timePicker)
    protected TimePicker detailTimePicker;
    @BindViews({R.id.day_sun, R.id.day_mon, R.id.day_tue, R.id.day_wed, R.id.day_thu, R.id.day_fri, R.id.day_sat})
    protected List<TextView> day_TVs;
    @BindView(R.id.detailRepeat_CB)
    protected CheckBox detailRepeat_CB;

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

    private void whenTimeChanged(int h, int m) {
        Log.d(TAG, "h : " + String.valueOf(h));
        hour = h;
        minute = m;
    }

    private void detailCancelClicked() {
        finish();
    }

    private void detailConfirmTVClicked() {
        insertDataBase();
        registWithAlarmManager(getApplicationContext(), dayOfWeekStr, mId, hour, minute, detailMemo_ET.getText().toString(), detailRepeat_CB.isChecked());
        Toast.makeText(this, "알람 등록", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void insertDataBase() {
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

    private AlarmRepo makeAlarmRepo() {
        int id = makeID();
        String memoStr = detailMemo_ET.getText().toString();
        boolean isRepeat = detailRepeat_CB.isChecked();
        AlarmRepo alarmRepo = new AlarmRepo(id, hour, minute, dayOfWeekStr, true, memoStr, isRepeat);
        return alarmRepo;
    }

    private void initSettings() {
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

    private void changeDayColor(int index, boolean isActive) {
        if (isActive) {
            day_TVs.get(index).setBackgroundColor(Color.MAGENTA);
        } else {
            day_TVs.get(index).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void changeSetting(AlarmRepo alarmRepo) {
        Log.d("changeSetting", "changeSetting");
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("changeSetting", "changeSetting1");
            detailTimePicker.setHour(alarmRepo.getHour());
            detailTimePicker.setMinute(alarmRepo.getMinute());
        }
        mId = alarmRepo.getId();
        dayOfWeekStr = alarmRepo.getDayOfWeekStr();
        detailMemo_ET.setText(alarmRepo.getMemoStr());
        detailRepeat_CB.setChecked(alarmRepo.isRepeat());

        for (int i = 0; i < day_TVs.size(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                changeDayColor(i, true);
            }
        }
    }

    private void detailChangeTVClicked(int index) {
        if (mAlarmRepos != null) {
            AlarmRepo alarmRepo = mAlarmRepos.get(index);
            if (realm != null) {
                realm.beginTransaction();
                alarmRepo.setHour(hour);
                alarmRepo.setMinute(minute);
                alarmRepo.setActive(true);
                alarmRepo.setDayOfWeekStr(dayOfWeekStr);
                alarmRepo.setMemoStr(detailMemo_ET.getText().toString());
                alarmRepo.setRepeat(detailRepeat_CB.isChecked());
                realm.commitTransaction();
            }
        }
        unregistWithAlarmManager(getApplicationContext(), "OOOOOOO", mId);
        registWithAlarmManager(getApplicationContext(), dayOfWeekStr, mId, hour, minute, detailMemo_ET.getText().toString(), detailRepeat_CB.isChecked());
    }

    private int makeID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int new_ID = sharedPreferences.getInt(getString(R.string.pref_ID), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        mId = ++new_ID;
        editor.putInt(getString(R.string.pref_ID), new_ID);
        Log.d("new_ID", String.valueOf(new_ID));
        editor.commit();
        return new_ID;
    }

    /*private void registWithAlarmManager() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra(getString(R.string.intent_isStart), true);

        Log.d(TAG, "Week1Millis : " + WEEK1MILLIS + " Day1MIillis : " + DAY1MILLIS);
        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = mId * 10 + i;

                Log.d("multi", "regist pending requestCode : " + requestCode);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar calendar = Calendar.getInstance();

                int nextDay = getNextDay(calendar, i);

                Log.d(TAG, "nextDay : " + nextDay);

                calendar.set(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE),
                        hour,
                        minute);

                long targetMillis = calendar.getTimeInMillis() + DAY1MILLIS * nextDay;
                long nowMillis = System.currentTimeMillis();

                Log.d(TAG, " targetMillies : " + targetMillis + " nowMillis : " + nowMillis);

                long delayMillis = targetMillis - nowMillis;

                if (delayMillis < 0) {
                    targetMillis += WEEK1MILLIS;
                }

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetMillis, WEEK1MILLIS, pendingIntent);
                Log.d(TAG, "Alarmregisted");
            }
        }
    }*/

    public static int getNextDay(Calendar calendar, int targetDayOfWeek) {
        int dayOfWeekInt;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dayOfWeekInt = 0;
                break;
            case Calendar.MONDAY:
                dayOfWeekInt = 1;
                break;
            case Calendar.TUESDAY:
                dayOfWeekInt = 2;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekInt = 3;
                break;
            case Calendar.THURSDAY:
                dayOfWeekInt = 4;
                break;
            case Calendar.FRIDAY:
                dayOfWeekInt = 5;
                break;
            case Calendar.SATURDAY:
                dayOfWeekInt = 6;
                break;
            default:
                dayOfWeekInt = -1;
        }
        Log.d(TAG, "dayOfWeekInt : " + dayOfWeekInt);
        return targetDayOfWeek - dayOfWeekInt;
    }
    /*private void unregistWithAlarmManager(String dayOfWeekStr, int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra(getString(R.string.intent_isStart), true);

        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = id * 10 + i;
                Log.d("multi", "adapter unregist pending requestCode : " + requestCode);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
                Log.d(TAG, "AlarmUnregisted");
            }
        }
    }*/
}