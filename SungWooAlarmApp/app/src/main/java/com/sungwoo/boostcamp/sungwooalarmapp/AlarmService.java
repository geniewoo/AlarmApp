package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by psw10 on 2017-01-25.
 */

public class AlarmService extends Service {

    static final String TAG = AlarmService.class.toString();

    MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "service");

        if (!intent.getBooleanExtra(getString(R.string.intent_isStart), false)) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            stopForeground(true);
            stopSelf();
        } else {
            screenOn();

            mMediaPlayer = MediaPlayer.create(this, R.raw.alarm1);
            mMediaPlayer.start();

            NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmIsRinging.class), PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT > 15) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setTicker("SungWooAlarm").setWhen(System.currentTimeMillis())
                        .setNumber(1)
                        .setContentTitle("알람")
                        .setContentText("테스트")
                        .addAction(getStopNitifyAction())
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                //notificationmanager.notify(1, builder.build());
                startForeground(1, builder.build());
            } else {
                Notification notification = new Notification(android.R.drawable.ic_lock_idle_alarm, "SungWooAlarm", System.currentTimeMillis());
                notification.flags = Notification.FLAG_ONGOING_EVENT;
                Intent i = new Intent(this, AlarmIsRinging.class);

                try {
                    Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                    deprecatedMethod.invoke(notification, "알람", "테스트", pendingIntent);
                } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                }
                //notificationmanager.notify(1, notification);
                //notification.actions = {};
                startForeground(1, notification);
            }
        }

        return START_NOT_STICKY;
    }

    android.support.v4.app.NotificationCompat.Action getStopNitifyAction(){
        Intent intent= new Intent(this, AlarmService.class);
        intent.putExtra(getString(R.string.intent_isStart), false);
        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action alarmStopAction = new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm,
                "알람 끄기",
                incrementWaterPendingIntent);
        return alarmStopAction;
    }
    void screenOn() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isInteractive();
        } else{
            isScreenOn = pm.isScreenOn();
        }


        if (isScreenOn == false) {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

            wl_cpu.acquire(10000);

            wl.release();
            wl_cpu.release();
        }
    }
}
