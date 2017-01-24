package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AlarmListActivity extends AppCompatActivity {
    @BindView(R.id.listRepoDebug)
    TextView listRepoDebug;
    @BindView(R.id.alarmAddBtn)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.alarmListRecyclerView)
    RecyclerView alarmListRV;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        ButterKnife.bind(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddActivity(getApplicationContext());
            }
        });

        realm = Realm.getDefaultInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        alarmListRV.setLayoutManager(layoutManager);
        alarmListRV.setHasFixedSize(true);

        List<AlarmRepo> realmList = realm.where(AlarmRepo.class).findAll();
        AlarmListAdapter alarmListAdapter = new AlarmListAdapter(realmList);
        alarmListRV.setAdapter(alarmListAdapter);
    }

    public static void showAddActivity(Context context){
        Intent intent = new Intent(context, AlarmDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(context.getString(R.string.intent_isCreate), true);
        context.startActivity(intent);
    }

    void debug(){
        StringBuffer result = new StringBuffer();
        List<AlarmRepo> realmList = realm.where(AlarmRepo.class).findAll();
        for (AlarmRepo repo : realmList) {
            result.append("hour : " + repo.getHour() + "\n");
            result.append("minute : " + repo.getMinute() + "\n");
            result.append("day : " + repo.getDayOfWeekStr() + "\n\n");
        }
        listRepoDebug.setText(result.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm!=null){
            realm.close();
        }
    }
}
