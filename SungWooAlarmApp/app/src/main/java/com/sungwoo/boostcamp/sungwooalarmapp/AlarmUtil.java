package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmDetailActivity.getNextDay;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.DAY1MILLIS;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.WEEK1MILLIS;

/**
 * Created by psw10 on 2017-01-28.
 */

public class AlarmUtil {
    private static  final String TAG = AlarmUtil.class.toString();

    public static void registWithAlarmManager(Context context, String dayOfWeekStr, int id, int hour, int minute, String memoStr, boolean isRepeat) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(context.getString(R.string.intent_isStart), true);
        intent.putExtra(context.getString(R.string.intent_alarmMemo), memoStr);
        Log.d(TAG, "Week1Millis : " + WEEK1MILLIS + " Day1MIillis : " + DAY1MILLIS);
        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = id * 10 + i;
                Log.d("multi", "adapter regist pending requestCode : " + requestCode);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
                if(isRepeat) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetMillis, WEEK1MILLIS, pendingIntent);
                }else{
                    alarmManager.set(AlarmManager.RTC_WAKEUP, targetMillis, pendingIntent);
                }
                Log.d(TAG, "Alarmregisted");
            }
        }
    }
    public static void unregistWithAlarmManager(Context context, String dayOfWeekStr, int id) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(context.getString(R.string.intent_isStart), true);

        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = id * 10 + i;
                Log.d("multi", "adapter unregist pending requestCode : " + requestCode);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
            }
        }
    }
    public static void registAllWithAlarmManager(Context context){
        Log.d("BootReceiver", "registall");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<AlarmRepo> results = realm.where(AlarmRepo.class).findAll();
        for(AlarmRepo alarmRepo : results){
            Log.d("BootReceiver", "regis~~");
            if(alarmRepo.isActive()) {
                registWithAlarmManager(context, alarmRepo.getDayOfWeekStr(), alarmRepo.getId(), alarmRepo.getHour(), alarmRepo.getMinute(), alarmRepo.getMemoStr(), alarmRepo.isRepeat());
            }
        }
        realm.close();
    }
}
