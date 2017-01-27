package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by psw10 on 2017-01-28.
 */

public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootReceiver", "booted");
        Toast.makeText(context, "booted", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            AlarmUtil.registAllWithAlarmManager(context);
        }
    }
}
