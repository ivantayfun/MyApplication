package com.example.ivan.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ExternalDbOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    public Context context;
    ExternalDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.context = context;
               }
    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
