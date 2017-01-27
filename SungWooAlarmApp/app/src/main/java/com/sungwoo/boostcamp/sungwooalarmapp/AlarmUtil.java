package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmDetailActivity.getNextDay;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.DAY1MILLIS;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.WEEK1MILLIS;

/**
 * Created by psw10 on 2017-01-28.
 */

public class AlarmUtil {
    private static  final String TAG = AlarmUtil.class.toString();

    public static void registWithAlarmManager(Context context, String dayOfWeekStr, int mId, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(context.getString(R.string.intent_isStart), true);
        Log.d(TAG, "Week1Millis : " + WEEK1MILLIS + " Day1MIillis : " + DAY1MILLIS);
        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = mId * 10 + i;
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

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetMillis, WEEK1MILLIS, pendingIntent);
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
}
