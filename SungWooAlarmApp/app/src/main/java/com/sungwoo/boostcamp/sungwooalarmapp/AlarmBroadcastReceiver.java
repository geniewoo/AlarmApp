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

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    //MediaPlayer mMediaPlayer;
    static final String TAG = AlarmBroadcastReceiver.class.toString();
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!intent.hasExtra(context.getString(R.string.intent_isStart))){
            return;
        }
        Intent serviceIntent = new Intent(context,AlarmService.class);
        serviceIntent.putExtra(context.getString(R.string.intent_isStart), intent.getBooleanExtra(context.getString(R.string.intent_isStart), false));
        Log.d(TAG, "BroadcastReceiver");
        context.startService(serviceIntent);
        /*
        if(intent.getBooleanExtra(context.getResources().getString(R.string.intent_isStart), false)){
            mMediaPlayer = MediaPlayer.create(context, R.raw.alarm1);
            mMediaPlayer.start();

            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, AlarmIsRinging.class), PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT > 15) {
                Notification.Builder builder = new Notification.Builder(context);
                builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setTicker("SungWooAlarm").setWhen(System.currentTimeMillis())
                        .setNumber(1).setContentTitle("알람").setContentText("테스트")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent)
                        .setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                notificationmanager.notify(1, builder.build());
            } else {
                Notification notification = new Notification(android.R.drawable.ic_lock_idle_alarm, "SungWooAlarm", System.currentTimeMillis());
                notification.flags = Notification.FLAG_ONGOING_EVENT;
                Intent i = new Intent(context, AlarmIsRinging.class);

                try {
                    Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                    deprecatedMethod.invoke(notification, "my title", "my text", pendingIntent);
                } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                }
                notificationmanager.notify(1, notification);
            }
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }*/
    }
}
