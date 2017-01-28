package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.remove_A_DayOfWeekFromAlarmRepo;

/**
 * Created by psw10 on 2017-01-25.
 */
// 소리, 진동, Notification을 띄우기 위한 Service클래스이다.
public class AlarmService extends Service {

    private static final long[] PATTERN = {0, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000, 1000};

    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String memoStr = intent.getStringExtra(getString(R.string.intent_alarmMemo));


        if (!intent.getBooleanExtra(getString(R.string.intent_isStart), false)) {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            stopForeground(true);
            stopSelf();
        } else {
            screenOn();
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                try {
                    mMediaPlayer.setDataSource(this, Uri.parse("android.resource://com.sungwoo.boostcamp.sungwooalarmapp/" + R.raw.alarm1));
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();
            } else if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                try {
                    mMediaPlayer.setDataSource(this, Uri.parse("android.resource://com.sungwoo.boostcamp.sungwooalarmapp/" + R.raw.alarm1));
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();
            } else {
                mMediaPlayer.start();
            }
            if (mVibrator == null) {
                mVibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            if (mVibrator.hasVibrator()) {
                mVibrator.cancel();
                mVibrator.vibrate(PATTERN, -1);
            }

            Intent ringingIntent = new Intent(this, AlarmIsRinging.class);
            ringingIntent.putExtra(getString(R.string.intent_alarmMemo), memoStr);
            if (!intent.getBooleanExtra(getString(R.string.intent_alarmRepeat), true)) {
                int tempId = intent.getIntExtra(getString(R.string.intent_alarmId), -1);
                int tempDayOfWeek = intent.getIntExtra(getString(R.string.intent_dayOfWeek), -1);
                remove_A_DayOfWeekFromAlarmRepo(tempId, tempDayOfWeek);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ringingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT > 15) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setTicker("SungWooAlarm").setWhen(System.currentTimeMillis())
                        .setNumber(1)
                        .setContentTitle("알람")
                        .setContentText(memoStr)
                        .addAction(getStopNotifyAction())
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setFullScreenIntent(pendingIntent, true)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                //notificationmanager.notify(1, builder.build());
                startForeground(1, builder.build());
            } else {
                Notification notification = new Notification(android.R.drawable.ic_lock_idle_alarm, "SungWooAlarm", System.currentTimeMillis());
                notification.flags = Notification.FLAG_ONGOING_EVENT;

                try {
                    Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                    deprecatedMethod.invoke(notification, "알람", memoStr, pendingIntent);
                } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                }
                //notificationmanager.notify(1, notification);
                //notification.actions = {};
                startForeground(1, notification);
            }
        }

        return START_NOT_STICKY;
    }

    private android.support.v4.app.NotificationCompat.Action getStopNotifyAction() {
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra(getString(R.string.intent_isStart), false);
        PendingIntent pendingIntent = PendingIntent.getService(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action alarmStopAction = new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm, "알람 끄기", pendingIntent);

        return alarmStopAction;
    }

    private void screenOn() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isInteractive();
        } else {
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
