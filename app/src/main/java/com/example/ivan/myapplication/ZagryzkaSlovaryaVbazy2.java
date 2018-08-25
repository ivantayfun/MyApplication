package com.example.ivan.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ZagryzkaSlovaryaVbazy2 extends Application {
    private String TAG = ZagryzkaSlovaryaVbazy.class.getSimpleName();
    ExternalDbOpenHelper dbOpenHelper;
    String jsonStr;
    String namelessons;
    String namelessons_id;
    //String namelessons_id_string;
    //int[] dbname_id_arr;
    Context context;
    String[] name_lesson_arr;
    //String[] dbname_name_otobrajenie_arr;
    String [] args;
    public ZagryzkaSlovaryaVbazy2(Context context, String name_lesson, String name_lesson_id){
        //dbname_id_arr = new int[20];
        //name_lesson_arr = new String[20];
        //dbname_name_otobrajenie_arr = new String[20];
        String DB_NAME;
        this.context = context;
        DB_NAME = "mydatabase.sqlite";
        dbOpenHelper = new ExternalDbOpenHelper(context, DB_NAME);           //2
        args = new String[3] ;
        this.namelessons = name_lesson;
        this.namelessons_id = name_lesson_id;
        new GetContacts().execute();
        args[0]="POST";
        args[1]="namelessons";
        args[2]=namelessons_id;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
         super.onPreExecute();
            Toast.makeText(context, context.getResources().getString(R.string.zagryzka_slovarya_v_bazy), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Запускаем актив синк" );
            jsonStr = URLConnectionExample.main(args);
            Log.e(TAG, "Response from url: " + jsonStr);





            if (jsonStr != null) {

                SQLiteDatabase database = dbOpenHelper.getWritableDatabase();

                Cursor cursor = database.rawQuery("SELECT sql FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", namelessons});
                if (!cursor.moveToFirst()) {
                    Log.d(TAG, "Создаем новую таблицу если такой нет" );
                    database.execSQL("create table if not exists " + namelessons + " (_id integer primary key autoincrement,"
                            + "english text,"
                            + "russian text,"
                            + "transcription text,"
                            + "kolotv integer,"
                            + "vid integer );");
                    try {
                        //contacts = new JSONObject(jsonStr);
                        //JSONObject dataJsonObj = new JSONObject(jsonStr);
                        //JSONArray contacts = dataJsonObj.getJSONArray("");
                        JSONArray contacts = new JSONArray(jsonStr);
                        ContentValues cv1 = new ContentValues();
                        cv1.put("name", namelessons);
                        cv1.put("name_otobrajenie", namelessons);
                        database.insert("tablename", null, cv1);
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            int id = c.getInt("id");
                            String english = c.getString("english");
                            String russian = c.getString("russian");
                            String transcription = c.getString("transcription");
                            ContentValues cv = new ContentValues();
                            cv.put("_id", id);
                            cv.put("english", english);
                            cv.put("russian", russian);
                            cv.put("transcription", transcription);
                            cv.put("kolotv", 0);
                            cv.put("vid", 0);
                            database.insert(namelessons, null, cv);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());

                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();


                    }
                    dbOpenHelper.close();
                    cursor.close();
                } else {
                    try {
                        Log.d(TAG, "Если таблица уже есть удаляем ее и создаем снова" );
                        database.delete(namelessons, null, null);
                        //database.delete("tablename","name = " + namelessons, null);
                        JSONArray contacts = new JSONArray(jsonStr);
                        //ContentValues cv1 = new ContentValues();
                        //cv1.put("name", namelessons);
                        //cv1.put("name_otobrajenie", namelessons);
                        //database.insert("tablename", null, cv1);
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            int id = c.getInt("id");
                            String english = c.getString("english");
                            String russian = c.getString("russian");
                            String transcription = c.getString("transcription");
                            ContentValues cv = new ContentValues();
                            cv.put("_id", id);
                            cv.put("english", english);
                            cv.put("russian", russian);
                            cv.put("transcription", transcription);
                            cv.put("kolotv", 0);
                            cv.put("vid", 0);
                            database.insert(namelessons, null, cv);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());

                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();


                    }
                    dbOpenHelper.close();
                    cursor.close();
                    londonberlinfynk();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            londonberlinfynk();

        }
    }

    //?????? ???????? ????????????
    private void londonberlinfynk() {
        //ActivitiSlovarb.hhh();
        /*Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);*/

    }
}
