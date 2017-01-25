package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmIsRinging extends AppCompatActivity {
    @BindView(R.id.alarmRingingStop_TV)
    TextView alarmRingingStop_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_is_ringing);

        ButterKnife.bind(this);
        alarmRingingStop_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMusic();
                finish();
            }
        });
    }

    void stopMusic(){
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra(getString(R.string.intent_isStart), false);
        sendBroadcast(intent);
    }
}
