package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by psw10 on 2017-01-25.
 */
// Alarm이 등록되면 먼저 이 BoradcastReceiver를 깨운다. 다시 service를 동작시킨다.
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!intent.hasExtra(context.getString(R.string.intent_isStart)) && !intent.hasExtra(context.getString(R.string.intent_alarmMemo))){
            return;
        }
        Intent serviceIntent = new Intent(context,AlarmService.class);
        serviceIntent.putExtra(context.getString(R.string.intent_isStart), intent.getBooleanExtra(context.getString(R.string.intent_isStart), false));
        serviceIntent.putExtra(context.getString(R.string.intent_alarmMemo), intent.getStringExtra(context.getString(R.string.intent_alarmMemo)));
        if(!intent.getBooleanExtra(context.getString(R.string.intent_alarmRepeat), true)){
            serviceIntent.putExtra(context.getString(R.string.intent_alarmRepeat), false);
            int tempId = intent.getIntExtra(context.getString(R.string.intent_alarmId), -1);
            serviceIntent.putExtra(context.getString(R.string.intent_alarmId), tempId);
            int tempDayOfWeek = intent.getIntExtra(context.getString(R.string.intent_dayOfWeek), -1);
            serviceIntent.putExtra(context.getString(R.string.intent_dayOfWeek), tempDayOfWeek);
        }
        context.startService(serviceIntent);
    }
}
