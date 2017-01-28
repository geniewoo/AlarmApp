package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmDetailActivity.getNextDay;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.DAY1MILLIS;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.WEEK1MILLIS;

/**
 * Created by psw10 on 2017-01-28.
 */

public class AlarmUtil {
    private static final String TAG = AlarmUtil.class.toString();

    public static void registWithAlarmManager(Context context, String dayOfWeekStr, int id, int hour, int minute, String memoStr, boolean isRepeat) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(context.getString(R.string.intent_isStart), true);
        intent.putExtra(context.getString(R.string.intent_alarmMemo), memoStr);
        if (!isRepeat) {
            intent.putExtra(context.getString(R.string.intent_alarmRepeat), false);
            intent.putExtra(context.getString(R.string.intent_alarmId), id);
        }
        Log.d(TAG, "Week1Millis : " + WEEK1MILLIS + " Day1MIillis : " + DAY1MILLIS);
        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = id * 10 + i;
                Log.d("multi", "adapter regist pending requestCode : " + requestCode);
                if(!isRepeat) {
                    intent.putExtra(context.getString(R.string.intent_dayOfWeek), i);
                }

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
                if (isRepeat) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetMillis, WEEK1MILLIS, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, targetMillis, pendingIntent);
                }
                Log.d(TAG, "Alarmregisted");
            }
        }
    }

    public static void unregistWithAlarmManager(Context context, String dayOfWeekStr, int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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

    public static void registAllWithAlarmManager(Context context) {
        Log.d("BootReceiver", "registall");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<AlarmRepo> results = realm.where(AlarmRepo.class).findAll();
        for (AlarmRepo alarmRepo : results) {
            Log.d("BootReceiver", "regis~~");
            if (alarmRepo.isActive()) {
                registWithAlarmManager(context, alarmRepo.getDayOfWeekStr(), alarmRepo.getId(), alarmRepo.getHour(), alarmRepo.getMinute(), alarmRepo.getMemoStr(), alarmRepo.isRepeat());
            }
        }
        realm.close();
    }

    public static void getCurrentWeather(String lat, String lon, final TextView weatherView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.skplanetx.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AlarmWeatherApi alarmWeatherApi = retrofit.create(AlarmWeatherApi.class);
        Call<WeatherGson> weatherCall = alarmWeatherApi.getCurrentWeather(1, lat, lon);
        weatherCall.enqueue(new Callback<WeatherGson>() {
            @Override
            public void onResponse(Call<WeatherGson> call, Response<WeatherGson> response) {
                WeatherGson weatherGson = response.body();
                if (weatherGson.getResult().getCode() != 9200 || weatherGson == null) {
                    return;
                }
                String weatherStr = "";
                WeatherGson.Weather.Minutely minutely = weatherGson.getWeather().getMinutely().get(0);
                weatherStr += "지역 : " + minutely.getStation().getName();
                weatherStr += " / 날씨 : " + minutely.getSky().getName();
                weatherStr += " / 온도 : " + minutely.getTemperature().getTc() + "°C";
                weatherView.setText(weatherStr);
            }

            @Override
            public void onFailure(Call<WeatherGson> call, Throwable t) {
                Log.d("WeatherApi", "error");
            }
        });
    }

    public static void remove_A_DayOfWeekFromAlarmRepo(int id, int dayOfWeek) {
        Log.d("remove_A_DayOfWeekFrom" , "doo!!");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        AlarmRepo alarmRepo = realm.where(AlarmRepo.class).equalTo("id", id).findFirst();
        String dayOfWeekStr = alarmRepo.getDayOfWeekStr();
        String newDayOfWeekStr = dayOfWeekStr.substring(0, dayOfWeek) + "X" + dayOfWeekStr.substring(dayOfWeek + 1);
        if (newDayOfWeekStr.equals("XXXXXXX")) {
            alarmRepo.setActive(false);
        }
        alarmRepo.setDayOfWeekStr(newDayOfWeekStr);
        realm.commitTransaction();
        realm.close();
    }
}
