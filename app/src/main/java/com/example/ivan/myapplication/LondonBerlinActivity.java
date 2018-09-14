package com.example.ivan.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class LondonBerlinActivity extends Activity {
    private String TAG = LondonBerlinActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    ExternalDbOpenHelper dbOpenHelper;
    GetSlovarb getSlovarb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.londonberlin_activity_main);
        contactList = new ArrayList<>();
        lv = findViewById(R.id.londonberlin_list);
        new GetContacts().execute();
        String DB_NAME;
        getSlovarb = new GetSlovarb(this);
        DB_NAME = "mydatabase.sqlite";
        dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
    }
    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LondonBerlinActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://annarybakova.net/android";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
                Cursor cursor = database.rawQuery("SELECT sql FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", "animalsis"});
                if (!cursor.moveToFirst()) {
                    database.execSQL("create table if not exists animalsis (_id integer primary key autoincrement,"
                            + "english text,"
                            + "russian text,"
                            + "transcription text,"
                            + "kolotv integer,"
                            + "vid integer );");
                    try {
                        JSONArray contacts = new JSONArray(jsonStr);                            //3
                        ContentValues cv1 = new ContentValues();
                        cv1.put("name", "animalsis");
                        cv1.put("name_otobrajenie", "animalsis");
                        database.insert("tablename", null, cv1);
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            String english = c.getString("english");
                            String russian = c.getString("russian");
                            String transcription = c.getString("transcription");
                            ContentValues cv = new ContentValues();
                            cv.put("english", english);
                            cv.put("russian", russian);
                            cv.put("transcription", transcription);
                            cv.put("kolotv", 0);
                            cv.put("vid", 0);
                            database.insert("animalsis", null, cv);
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("english", english);
                            contact.put("russian", russian);
                            contactList.add(contact);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    dbOpenHelper.close();
                    cursor.close();
                }else {
                    dbOpenHelper.close();
                    cursor.close();
                    londonberlinfynk();
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    LondonBerlinActivity.this,
                    contactList,
                    R.layout.londonberlin_list_item,
                    new String[]{"english", "russian"},
                    new int[]{R.id.londonberlin_email, R.id.londonberlin_mobile});
            lv.setAdapter(adapter);
        }
    }
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void londonberlinfynk() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}