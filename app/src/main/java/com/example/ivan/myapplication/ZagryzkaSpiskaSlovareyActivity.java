package com.example.ivan.myapplication;

//import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
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

import static android.content.ContentValues.TAG;

//import com.example.myapplication.R;

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
    final int DIALOG_EXIT2 = 2;
    String [] args;
    private String jsonStr;
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
                //item.setBackgroundColor(colors[0]);
                //int k = (3+i)%2;
                item.setBackgroundColor(colors[i % 2]);
                checkBox_slovary_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        dbname_checked_arr[compoundButton.getId()] = compoundButton.isChecked();
                                /*for (int k =0; k < jjj;k++) {
                                    Log.d(TAG, "Выводим сообщение о начале загрузки " + dbname_checked_arr[k]);
                                }*/

                        //compoundButton.isChecked();
                    }
                });
                /*text_slovary_item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        londonberlinfynk(v.getId());
                    }
                });*/
                slovary_activity_lnlname.addView(item);
            }
        }
    }

    public class ZagrSlovAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.zagryzka_slovarya_v_bazy), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            for (int k = 0;k<dbname_checked_arr.length;k++){
                if (dbname_checked_arr[k]){
                    args[2]= Integer.toString(name_lesson_id_arr[k]);
                    //Log.d(TAG, "Запускаем актив синк" );
                    jsonStr = URLConnectionExample.main(args);
                    //Log.e(TAG, "Response from url: " + jsonStr);
                    namelessons = name_lesson_arr[k];
                    namelessons_otobr = name_lesson_otobr_arr[k];
                    //namelessons = Integer.toString(name_lesson_id_arr[k]);
                    //(name_lesson_arr[k],Integer.toString(name_lesson_id_arr[k]));


                    if (!jsonStr.equals("")) {

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
                            perehodactivity();
                        }
                    } else {
                        Log.e(TAG, "Couldn't get json from server.");

                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();


                    }




                }
            }









            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            perehodactivity();

        }
    }
    //?????? ???????? ????????????
    private void perehodactivity() {
        //ActivitiSlovarb.hhh();
        Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);
        finish();

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
                nastroikifynk();
                break;
            case R.id.gramatika:
                gramatikafunk();
                break;
            case R.id.slovari:
                slovarifynk();
                break;
            case R.id.proiznoshenie:
                proiznosheniefunk();
                break;
            case R.id.skachatb_vbIbrannbIe:
                showDialog(DIALOG_EXIT2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //ЗАПУСК АКТИВИТИ НАСТРОЙКИ
    private void nastroikifynk() {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
        finish();
    }
    //ЗАПУСК АКТИВИТИ ГРАМАТИКА
    private void gramatikafunk() {
        Intent intent = new Intent(this, GramatikaActivity.class);
        startActivity(intent);
        finish();
    }
    //ЗАПУСК АКТИВИТИ СЛОВАРИ
    private void slovarifynk() {
        Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);
        finish();

    }
    //ФУНКЦИИ ЗАПУСТКА АКТИВИТИ ПРОИЗНОШЕНИЕ
    private void proiznosheniefunk() {
        Intent intent = new Intent(this, ProiznoshenieActivity.class);
        startActivity(intent);
        finish();
    }
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_EXIT2) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.dialog_zagryzitb_slovarb);
            // сообщение
            //adb.setMessage(R.string.save_data);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.ok, myClickListener2);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.cencel, myClickListener2);
            // делаем незакрываемым по кнопке назад
            //adb.setCancelable(false);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }
    public void zagryzitb_slovarb(){
        new ZagrSlovAsyncTask().execute();
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












}