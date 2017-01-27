package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmDetailActivity.getNextDay;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.DAY1MILLIS;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUnit.WEEK1MILLIS;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.registWithAlarmManager;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.unregistWithAlarmManager;

/**
 * Created by psw10 on 2017-01-24.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmListViewHolder> {
    private final String TAG = AlarmListActivity.class.toString();
    private List<AlarmRepo> alarmRepos;
    private Context mContext;
    private Realm realm;
    private static final int LIST_INDEX = 100;



    public AlarmListAdapter(List<AlarmRepo> alarmRepos, Realm realm) {
        this.realm = realm;
        this.alarmRepos = alarmRepos;
        Log.d(this.toString(),"adapter" + " " + alarmRepos.size());
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public AlarmListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(this.toString(),"adapter onCreateViewHolder");
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.alarmlistview, parent, false);

        AlarmListViewHolder holder = new AlarmListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmListViewHolder holder, int position) {
        holder.alarmTimeTV.setText(alarmRepos.get(position).getHour() + " : " + alarmRepos.get(position).getMinute());
        holder.alarmTimeTV.setTag(position);
        holder.alarmTimeTV.setTag(position);
        holder.alarmActiveIV.setTag(position);
        holder.itemView.setTag(position);
        holder.alarmDayOfWeekTV.setText(alarmRepos.get(position).getDayOfWeekStr());
        if(alarmRepos.get(position).isActive()){
            holder.alarmActiveIV.setImageResource(android.R.drawable.presence_away);
        }else {
            holder.alarmActiveIV.setImageResource(android.R.drawable.ic_lock_idle_alarm);
        }

        holder.alarmTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChangeActivity(view);
            }
        });

        holder.alarmTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChangeActivity(view);
            }
        });

        holder.alarmActiveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActive(view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmRepos.size();
    }

    void changeActive(View view){
        ImageView imageView = (ImageView)view;
        int index = (int)view.getTag();
        if(alarmRepos.get(index).isActive()){
            imageView.setImageResource(android.R.drawable.ic_lock_idle_alarm);
            if(realm!=null){
                realm.beginTransaction();
                alarmRepos.get(index).setActive(false);
                realm.commitTransaction();
                Toast.makeText(mContext, mContext.getString(R.string.alarm_inactive), Toast.LENGTH_SHORT).show();
                unregistWithAlarmManager(mContext, alarmRepos.get(index).getDayOfWeekStr(), alarmRepos.get(index).getId());
            }
        }else {
            imageView.setImageResource(android.R.drawable.presence_away);
            if(realm!=null){
                realm.beginTransaction();
                alarmRepos.get(index).setActive(true);
                realm.commitTransaction();
                Toast.makeText(mContext, mContext.getString(R.string.alarm_active), Toast.LENGTH_SHORT).show();
                registWithAlarmManager(mContext, alarmRepos.get(index).getDayOfWeekStr(), alarmRepos.get(index).getId(), alarmRepos.get(index).getHour(), alarmRepos.get(index).getMinute());
            }
        }
    }

    void startChangeActivity(View view){
        Intent intent = new Intent(mContext, AlarmDetailActivity.class);
        intent.putExtra(mContext.getString(R.string.intent_isCreate), false);
        intent.putExtra(mContext.getString(R.string.intent_alarmIndex), (int)view.getTag());
        mContext.startActivity(intent);
    }

    class AlarmListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.alarmListTime_TV)
        TextView alarmTimeTV;
        @BindView(R.id.alarmListDayOfWeek_TV)
        TextView alarmDayOfWeekTV;
        @BindView(R.id.alarmActivie_IV)
        ImageView alarmActiveIV;

        public AlarmListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

   /* public void unregistWithAlarmManager(String dayOfWeekStr, int id) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
        intent.putExtra(mContext.getString(R.string.intent_isStart), true);

        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = id * 10 + i;
                Log.d("multi", "adapter unregist pending requestCode : " + requestCode);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
                Log.d(TAG, "AlarmUnregisted");
            }
        }
    }*/

   /* public void registWithAlarmManager(String dayOfWeekStr, int mId, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmBroadcastReceiver.class);
        intent.putExtra(mContext.getString(R.string.intent_isStart), true);
        Log.d(TAG, "Week1Millis : " + WEEK1MILLIS + " Day1MIillis : " + DAY1MILLIS);
        for (int i = 0; i < dayOfWeekStr.length(); i++) {
            if (dayOfWeekStr.charAt(i) == 'O') {
                int requestCode = mId * 10 + i;
                Log.d("multi", "adapter regist pending requestCode : " + requestCode);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetMillis, WEEK1MILLIS, pendingIntent);
                Log.d(TAG, "Alarmregisted");
            }
        }
    }*/
}