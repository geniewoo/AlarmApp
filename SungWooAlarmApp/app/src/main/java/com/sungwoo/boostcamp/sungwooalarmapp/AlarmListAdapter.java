package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.registWithAlarmManager;
import static com.sungwoo.boostcamp.sungwooalarmapp.AlarmUtil.unregistWithAlarmManager;

/**
 * Created by psw10 on 2017-01-24.
 */
//메인페이지의 RecyclerView에 사용되는 Adapter이다
public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmListViewHolder> {
    private List<AlarmRepo> alarmRepos;
    private Context mContext;
    private Realm realm;
    private static final int LIST_INDEX = 100;


    public AlarmListAdapter(List<AlarmRepo> alarmRepos, Realm realm) {
        this.realm = realm;
        this.alarmRepos = alarmRepos;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public AlarmListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.alarmlistview, parent, false);

        AlarmListViewHolder holder = new AlarmListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmListViewHolder holder, int position) {
        holder.alarmTimeTV.setText(add_0_(alarmRepos.get(position).getHour()) + " : " + add_0_(alarmRepos.get(position).getMinute()));
        holder.alarmTimeTV.setTag(position);
        holder.alarmTimeTV.setTag(position);
        holder.alarmActiveIV.setTag(position);
        holder.itemView.setTag(position);
        holder.alarmDayOfWeekTV.setText(alarmRepos.get(position).getDayOfWeekStr());
        if (alarmRepos.get(position).isActive()) {
            holder.alarmActiveIV.setImageResource(android.R.drawable.presence_away);
        } else {
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

    void changeActive(View view) {
        ImageView imageView = (ImageView) view;
        int index = (int) view.getTag();
        if (alarmRepos.get(index).isActive()) {

            imageView.setImageResource(android.R.drawable.ic_lock_idle_alarm);
            if (realm != null) {
                realm.beginTransaction();
                alarmRepos.get(index).setActive(false);
                realm.commitTransaction();
                Toast.makeText(mContext, mContext.getString(R.string.alarm_inactive), Toast.LENGTH_SHORT).show();
                unregistWithAlarmManager(mContext, alarmRepos.get(index).getDayOfWeekStr(), alarmRepos.get(index).getId());
            }
        } else {
            if(alarmRepos.get(index).getDayOfWeekStr().equals("XXXXXXX")){
                Toast.makeText(mContext, mContext.getString(R.string.alarm_activeIm), Toast.LENGTH_SHORT).show();
                return;
            }
            imageView.setImageResource(android.R.drawable.presence_away);
            if (realm != null) {
                realm.beginTransaction();
                alarmRepos.get(index).setActive(true);
                realm.commitTransaction();
                Toast.makeText(mContext, mContext.getString(R.string.alarm_active), Toast.LENGTH_SHORT).show();
                registWithAlarmManager(mContext, alarmRepos.get(index).getDayOfWeekStr(), alarmRepos.get(index).getId(), alarmRepos.get(index).getHour(), alarmRepos.get(index).getMinute(), alarmRepos.get(index).getMemoStr(), alarmRepos.get(index).isRepeat());
            }
        }
    }

    void startChangeActivity(View view) {
        Intent intent = new Intent(mContext, AlarmDetailActivity.class);
        intent.putExtra(mContext.getString(R.string.intent_isCreate), false);
        intent.putExtra(mContext.getString(R.string.intent_alarmIndex), (int) view.getTag());
        mContext.startActivity(intent);
    }

    class AlarmListViewHolder extends RecyclerView.ViewHolder {
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

    private String add_0_(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return String.valueOf(time);
    }
}