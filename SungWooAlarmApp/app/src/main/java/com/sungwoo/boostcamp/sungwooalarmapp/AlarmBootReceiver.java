package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by psw10 on 2017-01-28.
 */
//새로 휴대폰을 켰을 때 실행되는 BroadcastReceiver이다.
public class AlarmBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {  //부팅되면
            AlarmUtil.registAllWithAlarmManager(context);   //데이터베이스에 있는 데이터 전부 등록하는 함수 실행
        }
    }
}
