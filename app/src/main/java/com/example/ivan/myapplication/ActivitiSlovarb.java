package com.example.ivan.myapplication;


import com.example.ivan.myapplication.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.*;

public class ActivitiSlovarb extends AppCompatActivity implements OnClickListener {
    ExternalDbOpenHelper dbOpenHelper;
    SQLiteDatabase database;
    SharedPreferences sp;
    ArrayList<Integer> dbname_id_arr;
    ArrayList<String> dbname_name_arr;
    ArrayList<String> dbname_name_otobrajenie_arr;
    ArrayList<Boolean> dbname_checked_arr;
    int[] textview_arr_id;
    int[] colors = new int[1];
    //boolean[] dbname_checked_arr;
    LinearLayout slovary_activity_lnlname;
    String namelessons;
    final int DIALOG_EXIT2 = 2;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_slovarb);
        Intent intent = getIntent();
        namelessons = intent.getStringExtra("name_lesson");
        //Если нужно менять цвет задаем програмно
        //colors[0] = Color.parseColor("#ffffffff");

        dbname_id_arr = new ArrayList<>();
        dbname_name_arr = new ArrayList<>();
        dbname_name_otobrajenie_arr = new ArrayList<>();
        dbname_checked_arr = new ArrayList<>();
        slovary_activity_lnlname = (LinearLayout) findViewById(R.id.slovary_activity_lnlname);
        sozdlistslovarb ();

    }
//public abstract void hhh();
public  void sozdlistslovarb (){
    slovary_activity_lnlname.removeAllViews();
    dbOpenHelper = new ExternalDbOpenHelper(this, "mydatabase.sqlite");
    database = dbOpenHelper.getWritableDatabase();
    Cursor c = database.query("tablename", null, null, null, null, null, null);
    if (c != null) {
        if (c.getCount() > 0) {
            LayoutInflater ltInflayter_bdname = getLayoutInflater();
            slovary_activity_lnlname.removeAllViews();
            if (c.moveToFirst()) {
                int i = 0;
                final int jjj = c.getCount();
                do {

                    dbname_id_arr.add(c.getInt(c.getColumnIndex("_id")));
                    dbname_name_arr.add(c.getString(c.getColumnIndex("name")));
                    dbname_name_otobrajenie_arr.add(c.getString(c.getColumnIndex("name_otobrajenie")));
                    dbname_checked_arr.add(false);
                    View item = ltInflayter_bdname.inflate(R.layout.slovari_item, slovary_activity_lnlname, false);
                    TextView text_slovary_item = (TextView) item.findViewById(R.id.text_slovary_item);
                    CheckBox checkBox_slovary_item = (CheckBox) item.findViewById(R.id.checkBox_slovary_item);
                    text_slovary_item.setText(dbname_name_otobrajenie_arr.get(i));
                    item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
                    text_slovary_item.setId(i);
                    checkBox_slovary_item.setId(i);
                    //Если нужно менять цвет заливки задаем програмно
                    //item.setBackgroundColor(colors[0]);
                    checkBox_slovary_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                           dbname_checked_arr.set(compoundButton.getId(),compoundButton.isChecked());

                        }
                    });
                    text_slovary_item.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mainvozvrat(dbname_name_arr.get(v.getId()));

                        }
                    });
                    //textview_arr[i] = text_slovary_item;
                    registerForContextMenu(text_slovary_item);
                    slovary_activity_lnlname.addView(item);
                    i++;
                } while (c.moveToNext());
            }
        }
        c.close();
    }
 dbOpenHelper.close();
}



    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void londonberlinfynk2() {
        Intent intent = new Intent(this, ZagryzkaSpiskaSlovarey.class);
        startActivity(intent);
        finish();
    }

    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void mainvozvrat(String fff) {
        //saveText(TABLE_NAME_PARSER, "animals");
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("TABLE_NAME_PARSER", fff);
        ed.commit();
        finish();
    }

    //ФУНКЦИЯ УДАЛЕНИЯ СЛОВАРЯ
    private void ydalitb_slovarb() {
        dbOpenHelper = new ExternalDbOpenHelper(this, "mydatabase.sqlite");
        database = dbOpenHelper.getWritableDatabase();
        Cursor cursor = database.query("tablename", null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    int i = 0;
                    //final int jjj = c.getCount();
                    do {
                        if (dbname_checked_arr.get(i)){
                            database.execSQL("drop table if exists " + dbname_name_arr.get(i) +";");
                            //database.delete(dbname_name_arr[i], null, null);
                            ContentValues cv = new ContentValues();
                            cv.put("name", "");
                            cv.put("name_otobrajenie", "");
                            database.delete("tablename","_id = " + dbname_id_arr.get(i), null);
                        }
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }


        dbOpenHelper.close();
        sozdlistslovarb ();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activiti_slovarb, menu);
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
            case R.id.londonberlin:
                londonberlinfynk2();
                break;
            case R.id.ydalitb_slovarb:
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
        /*Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);*/
        sozdlistslovarb ();
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
            adb.setTitle(R.string.dialog_ydalitb_slovarb);
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
    DialogInterface.OnClickListener myClickListener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    ydalitb_slovarb();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {


        menu.add(0, dbname_id_arr[v.getId()], 0, "del" + dbname_name_arr[v.getId()]);
        //R.string.del_slovar Integer.toString(v.getId())
    }*/
    /*@Override
    public boolean onContextItemSelected(MenuItem item) {
       // switch (item.getItemId()) {
            //tvColor

               // case 1:
        for (int i = 0; i < dbname_name_arr.length; i++) {
            if(item.getItemId()== dbname_id_arr[i]) {
                dbOpenHelper = new ExternalDbOpenHelper(this, "mydatabase.sqlite");
                database = dbOpenHelper.getWritableDatabase();
                database.delete(dbname_name_arr[i], null, null);
                //database.execSQL("DROP TABLE " + "animals");
                //database.delete("tablename", "_id = " + dbname_id_arr[i], null);

                dbOpenHelper.close();
            }
        }
                   // break;



        //}
        //return super.onContextItemSelected(item);
        //item.setText();
        return super.onContextItemSelected(item);
    }
*/

    @Override
    public void onClick(View v) {

    }
}
