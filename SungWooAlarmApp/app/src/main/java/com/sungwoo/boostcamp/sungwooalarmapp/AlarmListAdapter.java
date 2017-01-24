package com.sungwoo.boostcamp.sungwooalarmapp;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by psw10 on 2017-01-24.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmListViewHolder> {
    List<AlarmRepo> alarmRepos;
    Context mContext;
    static final int LIST_INDEX = 100;

    public AlarmListAdapter(List<AlarmRepo> alarmRepos) {
        this.alarmRepos = alarmRepos;
        Log.d(this.toString(),"adapter" + " " + alarmRepos.size());
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
        holder.alarmDayOfWeekTV.setText(alarmRepos.get(position).getDayOfWeekStr());
        if(alarmRepos.get(position).isActive()){
            holder.alarmActiveIV.setImageResource(android.R.drawable.presence_audio_away);
        }else {
            holder.alarmActiveIV.setImageResource(android.R.drawable.ic_lock_idle_alarm);
        }

        holder.alarmTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AlarmDetailActivity.class);
                intent.putExtra(mContext.getString(R.string.intent_isCreate), false);
                intent.putExtra(mContext.getString(R.string.intent_alarmIndex), (int)view.getTag());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmRepos.size();
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
}
