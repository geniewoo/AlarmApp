package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmListActivity extends AppCompatActivity {
    @BindView(R.id.listRepoDebug)
    TextView listRepoDebug;
    @BindView(R.id.alarmAddBtn)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.alarmListRecyclerView)
    RecyclerView alarmListRV;
    Realm realm;
    RecyclerView.Adapter mAdapter;
    List<AlarmRepo> mRealmList;
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

        mRealmList = realm.where(AlarmRepo.class).findAll();
        mAdapter = new AlarmListAdapter(mRealmList, realm);
        alarmListRV.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int index = (int)viewHolder.itemView.getTag();
                Log.d("swipeDir", String.valueOf(swipeDir));
                if(realm!=null) {
                    realm.beginTransaction();
                    AlarmRepo delAlarmRepo = realm.where(AlarmRepo.class).equalTo("id", mRealmList.get(index).getId()).findFirst();
                    delAlarmRepo.deleteFromRealm();
                    realm.commitTransaction();
                }
                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(alarmListRV);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
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
