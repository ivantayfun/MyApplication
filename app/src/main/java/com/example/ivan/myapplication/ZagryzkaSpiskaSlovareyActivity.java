package com.example.ivan.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.content.ContentValues.TAG;

public class ZagryzkaSpiskaSlovareyActivity extends AppCompatActivity implements OnClickListener {
    int[] colors = new int[2];
    ExternalDbOpenHelper dbOpenHelper;
    boolean[] dbname_checked_arr;
    String[] name_lesson_arr;
    String[] name_lesson_otobr_arr;
    int[] name_lesson_id_arr;
    LinearLayout slovary_activity_lnlname;
    String namelessons;
    String namelessons_otobr;
    //final int DIALOG_EXIT2 = 2;
    String [] args;
    private String jsonStr;
    protected AlertDialog.Builder dialog;
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    CountDownLatch countDownLatch = new CountDownLatch(1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_slovarb);
        Intent intent = getIntent();
        jsonStr = "";
        dbname_checked_arr = new boolean[20];
        dbOpenHelper = new ExternalDbOpenHelper(this, "mydatabase.sqlite");
        name_lesson_arr = intent.getStringArrayExtra("name_lesson_arr");
        name_lesson_id_arr = intent.getIntArrayExtra("name_lesson_id_arr");
        name_lesson_otobr_arr = intent.getStringArrayExtra("name_lesson_otobr_arr");
        colors[0] = Color.parseColor("#fffbfdff");
        colors[1] = Color.parseColor("#fffbfdff");
        args = new String[3] ;
        args[0]="POST";
        args[1]="namelessons";
        dialog = new AlertDialog.Builder(ZagryzkaSpiskaSlovareyActivity.this);
        dialog.setTitle(R.string.dialog_zagryzitb_slovarb);
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        dialog.setMessage(R.string.save_data);
        dialog.setPositiveButton(R.string.ok, myClickListener2);
        dialog.setNegativeButton(R.string.cencel, myClickListener2);
        slovary_activity_lnlname =  findViewById(R.id.slovary_activity_lnlname);
        LayoutInflater ltInflayter_bdname = getLayoutInflater();
        slovary_activity_lnlname.removeAllViews();
        for ( int i = 0; i < name_lesson_arr.length; i++) {
            if (name_lesson_arr[i] != null) {
                View item = ltInflayter_bdname.inflate(R.layout.zagryzkaspiskaslovarey_item, slovary_activity_lnlname, false);
                TextView text_slovary_item = item.findViewById(R.id.text_zagryzkaspiskaslovarey_item);
                CheckBox checkBox_slovary_item = item.findViewById(R.id.checkBox_zagryzkaspiskaslovarey_item);
                text_slovary_item.setText(name_lesson_otobr_arr[i]);
                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                text_slovary_item.setId(i);
                checkBox_slovary_item.setId(i);
                item.setBackgroundColor(colors[i % 2]);
                checkBox_slovary_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        dbname_checked_arr[compoundButton.getId()] = compoundButton.isChecked();
                    }
                });
                slovary_activity_lnlname.addView(item);
            }
        }
    }

    @Override
    public void onClick(View v) {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zagr_spis_slov, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nastroiki:
                //ЗАПУСК АКТИВИТИ НАСТРОЙКИ
                smenaactivityfynk(PrefActivity.class);
                break;
            case R.id.gramatika:
                //ЗАПУСК АКТИВИТИ ГРАМАТИКА
                smenaactivityfynk(GramatikaActivity.class);
                break;
            case R.id.slovari:
                //ЗАПУСК АКТИВИТИ СЛОВАРИ
                smenaactivityfynk(ActivitiSlovarb.class);
                break;
            case R.id.proiznoshenie:
                //ЗАПУСТК АКТИВИТИ ПРОИЗНОШЕНИЕ
                smenaactivityfynk(ProiznoshenieActivity.class);
                break;
            case R.id.skachatb_vbIbrannbIe:
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //МЕТОД СМЕНЫ АКТИВИТИ
    private void smenaactivityfynk(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
        finish();
    }
    public void zagryzitb_slovarb(){
        //new ZagrSlovAsyncTask().execute();
        new ThreadZagrSlovComplit(countDownLatch);
        new ThreadZagrSlov(countDownLatch);
    }
    DialogInterface.OnClickListener myClickListener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    zagryzitb_slovarb();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    private class ThreadZagrSlovComplit extends  Thread{
        CountDownLatch countDownLatch;
        ThreadZagrSlovComplit(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
            start();
        }
        @Override
        public void run() {

            try {
                Log.d("ivan","ThreadZagrSlovComplit do await");
                countDownLatch.await();
                Log.d("ivan","ThreadZagrSlovComplit posle await");
                smenaactivityfynk(ActivitiSlovarb.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    private class ThreadZagrSlov extends Thread{
        CountDownLatch countDownLatch;
        ThreadZagrSlov(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
            start();
        }
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.zagryzka_slovarya_v_bazy),
                            Toast.LENGTH_SHORT).show();
                }
            });
            for (int k = 0;k<dbname_checked_arr.length;k++){
                if (dbname_checked_arr[k]){
                    args[2]= Integer.toString(name_lesson_id_arr[k]);
                    jsonStr = URLConnectionExample.main(args);
                    namelessons = name_lesson_arr[k];
                    namelessons_otobr = name_lesson_otobr_arr[k];
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
                                    //assert i>0;
                                }
                            } catch (final JSONException e) {
                                Log.e(TAG, getString(R.string.Json_parsing_error) + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                getString(R.string.Json_parsing_error),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

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
                                Log.d("ivan", getString(R.string.Json_parsing_error) + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                getString(R.string.Json_parsing_error),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            dbOpenHelper.close();
                            cursor.close();
                            smenaactivityfynk(ActivitiSlovarb.class);
                        }
                    } else {
                        Log.d("ivan", getString(R.string.internet));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.internet),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
            Log.d("ivan","ThreadZagrSlov zapyskay signal");
            countDownLatch.countDown();
        }
    }
}