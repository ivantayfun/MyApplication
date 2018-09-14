package com.example.ivan.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class GetSlovarb extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String TAG;
    private ExternalDbOpenHelper dbOpenHelper;
    LinearLayout slovary_activity_lnlname;
    private ListView lv;
    LayoutInflater ltInflayter_bdname;
    private ArrayList<HashMap<String, String>> contactList;
    GetSlovarb(Context context) {
        //super(context);
        this.context = context;
        TAG = LondonBerlinActivity.class.getSimpleName();
        String DB_NAME;

        Log.d("ivan", "GetSlovarb constraction");
        //slovary_activity_lnlname = findViewById(R.id.slovary_activity_lnlname);
        //LayoutInflater ltInflayter_bdname = getLayoutInflater();
        View item = ltInflayter_bdname.inflate(R.layout.slovari_item, slovary_activity_lnlname, false);
        lv = item.findViewById(R.id.londonberlin_list);
        DB_NAME = "mydatabase.sqlite";
        dbOpenHelper = new ExternalDbOpenHelper(this.context, DB_NAME);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(this.context, "Json Data is downloading", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this.context.getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });*/
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
            Toast.makeText(this.context.getApplicationContext(),
                    "Couldn't get json from server. Check LogCat for possible errors!",
                    Toast.LENGTH_LONG).show();
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            });*/
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        ListAdapter adapter = new SimpleAdapter(
                this.context,
                contactList,
                R.layout.londonberlin_list_item,
                new String[]{"english", "russian"},
                new int[]{R.id.londonberlin_email, R.id.londonberlin_mobile});
        lv.setAdapter(adapter);
    }
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void londonberlinfynk() {
        Intent intent = new Intent(this.context, MainActivity.class);
        this.context.startActivity(intent);
    }
}
