package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
// 서비스 등록, 해제, 등 여러 클래스에 쓰일 함수를 포함한다.
public class AlarmUtil {

    public static void registWithAlarmManager(Context context, String dayOfWeekStr, int id, int hour, int minute, String memoStr, boolean isRepeat) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(context.getString(R.string.intent_isStart), true);
        intent.putExtra(context.getString(R.string.intent_alarmMemo), memoStr);
        if (!isRepeat) {
            intent.putExtra(context.getString(R.string.intent_alarmRepeat), false);
            intent.putExtra(context.getString(R.string.intent_alarmId), id);
        }
        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = id * 10 + i;
                if(!isRepeat) {
                    intent.putExtra(context.getString(R.string.intent_dayOfWeek), i);
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar calendar = Calendar.getInstance();

                int nextDay = getNextDay(calendar, i);

                calendar.set(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DATE),
                        hour,
                        minute);

                long targetMillis = calendar.getTimeInMillis() + DAY1MILLIS * nextDay;
                long nowMillis = System.currentTimeMillis();

                long delayMillis = targetMillis - nowMillis;

                if (delayMillis < 0) {
                    targetMillis += WEEK1MILLIS;
                }
                if (isRepeat) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetMillis, WEEK1MILLIS, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, targetMillis, pendingIntent);
                }
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

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
            }
        }
    }

    public static void registAllWithAlarmManager(Context context) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<AlarmRepo> results = realm.where(AlarmRepo.class).findAll();
        for (AlarmRepo alarmRepo : results) {
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
            }
        });
    }

    public static void remove_A_DayOfWeekFromAlarmRepo(int id, int dayOfWeek) {
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
