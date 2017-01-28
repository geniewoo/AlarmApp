package com.sungwoo.boostcamp.sungwooalarmapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
// 알람이 울릴때 notification을 눌렀을 때 보이는 페이지 이다.
public class AlarmIsRinging extends AppCompatActivity {
    @BindView(R.id.alarmRingingStop_TV)
    TextView alarmRingingStop_TV;
    @BindView(R.id.alarmRingingMemo_TV)
    TextView alarmRingingMemo_TV;
    @BindView(R.id.alarmRingingWeather_TV)
    TextView alarmRingingWeather_TV;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
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
        Intent intent = getIntent();
        alarmRingingMemo_TV.setText(intent.getStringExtra(getString(R.string.intent_alarmMemo)));

        setWeatherTextView();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    void stopMusic() {
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra(getString(R.string.intent_isStart), false);
        sendBroadcast(intent);
    }

    void setWeatherTextView() {
        String[] latLon = getLatLon();
        if (latLon != null) {
            AlarmUtil.getCurrentWeather(latLon[0], latLon[1], alarmRingingWeather_TV);
        } else {
            AlarmUtil.getCurrentWeather("37.497942", "127.025427", alarmRingingWeather_TV);
        }
    }

    public String[] getLatLon() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, null);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            String[] returnStrArr={lat, lon};
            return returnStrArr;
        } else {
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isNetworkEnabled){
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, null);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                String[] returnStrArr={lat, lon};
                return returnStrArr;
            }else{
                Log.d("LatLon2", "unable");
            }
            return null;
        }
    }
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AlarmIsRinging Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
