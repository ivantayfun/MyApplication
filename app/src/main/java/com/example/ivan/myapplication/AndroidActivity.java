package com.example.ivan.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.ivan.myapplication.URLConnectionExample;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
import java.net.*;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ivan.myapplication.R;


public class AndroidActivity extends Activity {
    private String TAG = AndroidActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    ExternalDbOpenHelper dbOpenHelper;                                                  //2
    SQLiteDatabase db;
    SQLiteDatabase db2;
    String jsonStr;
    String namelessons;
    int[] dbname_id_arr;
    int[] colors = new int[1];
    String[] dbname_name_arr;
    String[] dbname_name_otobrajenie_arr;
    String[] args;
    LinearLayout slovary_activity_lnlname;
    //DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.londonberlin_activity_main);
        dbname_id_arr = new int[20];
        dbname_name_arr = new String[20];
        args = new String[3];
        dbname_name_otobrajenie_arr = new String[20];
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.londonberlin_list);
        args[0]="POST";
        args[1]="namelessons";
        args[2]="";
        new GetContacts().execute();
        String DB_NAME;
        DB_NAME = "mydatabase.sqlite";
        dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);           //2
        //dbh = new DBHelper(this);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AndroidActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {



            jsonStr = URLConnectionExample.main(args);


            // HttpHandler sh = new HttpHandler();
            //Making a request to url and getting response
            // String url = "http://annarybakova.net/android";
            // String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                // SQLiteDatabase database1 = dbh.getWritableDatabase();
                // dbh.close();










                SQLiteDatabase database = dbOpenHelper.getWritableDatabase();


//                    if (tableName == null || database == null || !database.isOpen())
//                    {
//                        return false;
//                    }
                Cursor cursor = database.rawQuery("SELECT sql FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", "animalsis"});
                if (!cursor.moveToFirst()) {
                    database.execSQL("create table if not exists animalsis (_id integer primary key autoincrement,"
                            + "english text,"
                            + "russian text,"
                            + "transcription text,"
                            + "kolotv integer,"
                            + "vid integer );");
                    try {
                        // JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray contacts = new JSONArray(jsonStr);
                        // JSONArray contacts = jsonObj.getJSONArray("word");
                        //String id = jsonObj.getString("country");
                        // looping through All Contacts
                        //SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
                        //db = dbh.getWritableDatabase();                                //3
                        ContentValues cv1 = new ContentValues();
                        cv1.put("name", "animalsis");
                        cv1.put("name_otobrajenie", "animalsis");
                        database.insert("tablename", null, cv1);
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            String english = c.getString("english");
                            String russian = c.getString("russian");
                            String transcription = c.getString("transcription");
                            String kolotv = c.getString("kolotv");
                            String vid = c.getString("vid");

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

                        //dbh.close();                                                  //3
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
                    AndroidActivity.this,
                    contactList,

                    R.layout.slovari_item,


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
//    class DBHelper extends SQLiteOpenHelper {
//
//        public DBHelper(Context context) {
//            //
//            super(context, "mydatabase.sqlite", null, 1);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            //Log.d(LOG_TAG, "--- onCreate database ---");
//            db.execSQL("create table animalsis ("
//                    + "id integer primary key autoincrement,"
//                    + "english text,"
//                    + "russian text,"
//                    + "transcription text,"
//                    + "kolotv integer,"
//                    + "vid integer" + ");");//
//
//        }
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        }
//    }
}
