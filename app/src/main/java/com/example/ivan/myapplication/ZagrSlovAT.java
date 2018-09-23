package com.example.ivan.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
//import android.widget.LinearLayout;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class ZagrSlovAT extends AsyncTask<Void, Void, Void> {
    ZagryzkaSpiskaSlovareyActivity context;
    private ExternalDbOpenHelper dbOpenHelper;
    private boolean[] dbname_checked_arr;
    private String[] name_lesson_arr;
    private String[] name_lesson_otobr_arr;
    private int[] name_lesson_id_arr;
    //LinearLayout slovary_activity_lnlname;
    //final int DIALOG_EXIT2 = 2;
    private String [] args;
    private String jsonStr;
    protected AlertDialog.Builder dialog;
    ZagrSlovAT (ZagryzkaSpiskaSlovareyActivity context,String[] name_lesson_arr,int[] name_lesson_id_arr,String[] name_lesson_otobr_arr){
        Log.d("ivan", "ZagrSlovAT constraction");
        this.context = context;
        this.name_lesson_arr = name_lesson_arr;
        this.name_lesson_id_arr = name_lesson_id_arr;
        this.name_lesson_otobr_arr = name_lesson_otobr_arr;
        //Intent intent = getIntent();
        jsonStr = "";
        dbname_checked_arr = new boolean[20];
        dbname_checked_arr[0]=false;
        dbOpenHelper = new ExternalDbOpenHelper(this.context, "mydatabase.sqlite");
        //name_lesson_arr = intent.getStringArrayExtra("name_lesson_arr");
        //name_lesson_id_arr = intent.getIntArrayExtra("name_lesson_id_arr");
        //name_lesson_otobr_arr = intent.getStringArrayExtra("name_lesson_otobr_arr");
//        int[] colors = new int[2];
//        colors[0] = Color.parseColor("#fffbfdff");
//        colors[1] = Color.parseColor("#fffbfdff");
        args = new String[3] ;
        args[0]="POST";
        args[1]="namelessons";
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("ivan", "ZagrSlovAT  onPreExecute()");
        Toast.makeText(this.context.getApplicationContext(), this.context.getResources().getString(R.string.zagryzka_slovarya_v_bazy), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        Log.d("ivan", "ZagrSlovAT  doInBackground()");
        for (int k = 0;k<dbname_checked_arr.length;k++){
            if (dbname_checked_arr[k]){
                args[2]= Integer.toString(name_lesson_id_arr[k]);
                jsonStr = URLConnectionExample.main(args);
                String namelessons = name_lesson_arr[k];
                String namelessons_otobr = name_lesson_otobr_arr[k];
                if (!jsonStr.equals("")) {
                    SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
                    Cursor cursor = database.rawQuery("SELECT sql FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", namelessons});
                    if (!cursor.moveToFirst()) {
                        Log.d("ivan", "Создаем новую таблицу если такой нет" );
                        database.execSQL("create table if not exists " + namelessons + " (_id integer primary key autoincrement,"
                                + "english text,"
                                + "russian text,"
                                + "transcription text,"
                                + "kolotv integer,"
                                + "vid integer );");
                        try {
                            JSONArray contacts = new JSONArray(jsonStr);
                            ContentValues cv1 = new ContentValues();
                            cv1.put("name", namelessons);
                            cv1.put("name_otobrajenie", namelessons_otobr);
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
                            Toast.makeText(this.context.getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        dbOpenHelper.close();
                        cursor.close();
                    } else {
                        try {
                            Log.d("ivan", "Если таблица уже есть удаляем ее и создаем снова" );
                            database.delete(namelessons, null, null);
                            JSONArray contacts = new JSONArray(jsonStr);
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

                            Toast.makeText(this.context.getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        dbOpenHelper.close();
                        cursor.close();
                        //this.context.perehodactivity();
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    Toast.makeText(this.context.getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        Log.d("ivan", "ZagrSlovAT  onPostExecute()");
        //this.context.perehodactivity();
    }
}
