package com.example.ivan.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAssink extends AsyncTask<Void, Void, Void> {
    private String TAG = AndroidActivity.class.getSimpleName();
    private ListView lv;
    private ArrayList<HashMap<String, String>> contactList;
    ExternalDbOpenHelper dbOpenHelper;
    private String jsonStr;
    private String[] args;
    LinearLayout slovary_activity_lnlname;
    private AndroidActivity androidActivity;
    MyAssink(AndroidActivity androidActivity){
        this.androidActivity = androidActivity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(androidActivity, "Json Data is downloading", Toast.LENGTH_LONG).show();
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        jsonStr = URLConnectionExample.main(args);
        Log.e("Assert", "Response from url: " + jsonStr);
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
                    JSONArray contacts = new JSONArray(jsonStr);
                    ContentValues cv1 = new ContentValues();
                    cv1.put("name", "animalsis");
                    cv1.put("name_otobrajenie", "animalsis");
                    database.insert("tablename", null, cv1);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String english = c.getString("english");
                        String russian = c.getString("russian");
                        String transcription = c.getString("transcription");
                        //String kolotv = c.getString("kolotv");
                        //String vid = c.getString("vid");
                        ContentValues cv = new ContentValues();
                        cv.put("english", english);
                        cv.put("russian", russian);
                        cv.put("transcription", transcription);
                        cv.put("kolotv", 0);
                        cv.put("vid", 0);
                        database.insert("animalsis", null, cv);
                        //db.insert("animals", null, cv);                           //3
                        //long rowID = database.insert("animals", null, cv);
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();
                        // adding each child node to HashMap key => value
                        contact.put("english", english);
                        contact.put("russian", russian);
                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    androidActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(androidActivity.getApplicationContext(),
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
            androidActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(androidActivity.getApplicationContext(),
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
                androidActivity,
                contactList,
                R.layout.slovari_item,
                new String[]{"english", "russian"},
                new int[]{R.id.londonberlin_email, R.id.londonberlin_mobile});
        lv.setAdapter(adapter);
    }
    private void londonberlinfynk() {
        Intent intent = new Intent(androidActivity, MainActivity.class);
        androidActivity.startActivity(intent);
    }
}
