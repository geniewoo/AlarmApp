package com.sungwoo.boostcamp.sungwooalarmapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by psw10 on 2017-01-27.
 */

public class AlarmProvider extends ContentProvider {
    private Realm realm;
    RealmQuery<AlarmRepo> query;
    List<AlarmRepo> mAlarmRepos;
    private static final String[] columns = new String[]{"id", "hour", "minute", "dayOfWeekStr", "isActive", "memoStr"};
    private static final String PATH = "com.sungwoo.boostcamp.sungwooalarmapp/";

    private static final int ALARM_FINDALL = 100;
    private static final int ALARM_INSERT = 200;
    private static final int ALARM_DELETE = 300;
    private static final int ALARM_UPDATE = 400;

    private static final UriMatcher sUriMatcher = makeUriMatcher();

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        MatrixCursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case ALARM_FINDALL:
                realm = Realm.getDefaultInstance();
                query = realm.where(AlarmRepo.class);
                mAlarmRepos = query.findAll();
                cursor = new MatrixCursor(columns);
                for (AlarmRepo repo : mAlarmRepos) {
                    Object[] rowData = new Object[]{repo.getId(), repo.getHour(), repo.getMinute(), repo.getDayOfWeekStr(), repo.isActive(), repo.getMemoStr()};
                    cursor.addRow(rowData);
                }
                realm.close();
                break;
            default:
                return null;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case ALARM_INSERT:
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                AlarmRepo alarmRepo = realm.createObject(AlarmRepo.class);
                alarmRepo.setId(contentValues.getAsInteger(columns[0]));
                alarmRepo.setHour(contentValues.getAsInteger(columns[1]));
                alarmRepo.setMinute(contentValues.getAsInteger(columns[2]));
                alarmRepo.setDayOfWeekStr(contentValues.getAsString(columns[3]));
                alarmRepo.setActive(contentValues.getAsBoolean(columns[4]));
                alarmRepo.setMemoStr(contentValues.getAsString(columns[5]));
                realm.commitTransaction();
                realm.close();

                if(contentValues.getAsBoolean(columns[4])){
                    AlarmUtil.registWithAlarmManager(getContext(), contentValues.getAsString(columns[3]), contentValues.getAsInteger(columns[0]), contentValues.getAsInteger(columns[1]), contentValues.getAsInteger(columns[2]));
                }

                return Uri.withAppendedPath(uri, String.valueOf(alarmRepo.getId()));
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int id;
        try {
            id = Integer.parseInt(s);

        } catch (Exception e) {
            return -1;
        }

        switch (sUriMatcher.match(uri)) {
            case ALARM_DELETE:
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                AlarmRepo delAlarmRepo = realm.where(AlarmRepo.class).equalTo("id", id).findFirst();
                AlarmUtil.unregistWithAlarmManager(getContext(), delAlarmRepo.getDayOfWeekStr(), delAlarmRepo.getId());
                delAlarmRepo.deleteFromRealm();
                realm.commitTransaction();
                realm.close();
                break;
            default:
                return -1;
        }

        return id;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int id;
        id = contentValues.getAsInteger(columns[0]);

        switch (sUriMatcher.match(uri)) {
            case ALARM_UPDATE:
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                AlarmRepo updateAlarmRepo = realm.where(AlarmRepo.class).equalTo("id", id).findFirst();
                AlarmUtil.unregistWithAlarmManager(getContext(), updateAlarmRepo.getDayOfWeekStr(), updateAlarmRepo.getId());

                updateAlarmRepo.setHour(contentValues.getAsInteger(columns[1]));
                updateAlarmRepo.setMinute(contentValues.getAsInteger(columns[2]));
                updateAlarmRepo.setDayOfWeekStr(contentValues.getAsString(columns[3]));
                updateAlarmRepo.setActive(contentValues.getAsBoolean(columns[4]));
                updateAlarmRepo.setMemoStr(contentValues.getAsString(columns[5]));
                realm.commitTransaction();
                realm.close();
                AlarmUtil.registWithAlarmManager(getContext(), contentValues.getAsString(columns[3]), contentValues.getAsInteger(columns[0]), contentValues.getAsInteger(columns[1]), contentValues.getAsInteger(columns[2]));
                break;
            default:
                return -1;
        }

        return id;
    }

    private static UriMatcher makeUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("content://", PATH + "/findall", ALARM_FINDALL);
        uriMatcher.addURI("content://", PATH + "/insert/#/#/#/*/*/*", ALARM_INSERT);
        uriMatcher.addURI("content://", PATH + "/delete/#", ALARM_DELETE);
        uriMatcher.addURI("content://", PATH + "/update/#", ALARM_UPDATE);
        return uriMatcher;
    }
}
