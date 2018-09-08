package com.example.ivan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivitySlovarbList extends Activity implements OnClickListener {
    final int DIALOG_EXIT2 = 2;
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_CHECKED = "checked";
    ExternalDbOpenHelper dbOpenHelper;
    SQLiteDatabase database;
    String[] dbname_name_otobrajenie_arr = new String[20];
    boolean[] dbname_checked_arr = new boolean[20];
    boolean[] checked;
    String[] texts;
    ListView lvSimple;
    SimpleAdapter simpleAdapter;
    CheckBox ggg;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slovarb_adapter);



        dbOpenHelper = new ExternalDbOpenHelper(this, "mydatabase.sqlite");
        database = dbOpenHelper.getWritableDatabase();
        Cursor c = database.query("tablename", null, null, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    int i = 0;
                    do {
                        //dbname_id_arr[i] = c.getInt(c.getColumnIndex("_id"));
                        dbname_checked_arr[i] = false;
                        dbname_name_otobrajenie_arr[i] = c.getString(c.getColumnIndex("name_otobrajenie"));
                        i++;
                    } while (c.moveToNext());
                }
            }
            c.close();
        }
        dbOpenHelper.close();
        texts =  dbname_name_otobrajenie_arr;
        checked = dbname_checked_arr;
        ArrayList<Map<String, Object>> data = new ArrayList<>(texts.length);
        Map<String, Object> m;
        for (int k = 0; k < texts.length; k++) {
            m = new HashMap<>();
            m.put(ATTRIBUTE_NAME_TEXT, texts[k]);
            m.put(ATTRIBUTE_NAME_CHECKED, checked[k]);
            data.add(m);
        }
        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_CHECKED};
        int[] to = {R.id.text_slovary_item, R.id.checkBox_slovary_item};
        simpleAdapter = new SimpleAdapter(this, data, R.layout.slovari_item, from, to);
        lvSimple =  findViewById(R.id.lvSimple);
        lvSimple.setAdapter(simpleAdapter);
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
            case R.id.ychitbcnachala:
                showDialog(DIALOG_EXIT2);
                break;
            case R.id.londonberlin:
                londonberlinfynk2();
                break;
            case R.id.ydalitb_slovarb:
                ydalitb_slovarb();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void londonberlinfynk2() {
        Intent intent = new Intent(this, ZagryzkaSpiskaSlovarey.class);
        startActivity(intent);
    }
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void ydalitb_slovarb() {
        for (int i=0;i<dbname_name_otobrajenie_arr.length;i++){
            Log.d(TAG, "Выводим сообщение о начале загрузки " +  simpleAdapter.getView(i,ggg,lvSimple));
        }
    }
    /*
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void londonberlinfynk2() {
        Intent intent = new Intent(this, ZagryzkaSpiskaSlovarey.class);
        startActivity(intent);
    }
    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void ydalitb_slovarb() {
        Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ychitbcnachala:
                showDialog(DIALOG_EXIT2);
                break;
            case R.id.londonberlin:
                londonberlinfynk2();
                break;
            case R.id.ydalitb_slovarb:
                ydalitb_slovarb();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_EXIT2) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.exit_ychitb_c_nachala);
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
                    //ychitbcnachalafynk();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, dbname_id_arr[v.getId()], 0, "del" + dbname_name_arr[v.getId()]);
        //R.string.del_slovar Integer.toString(v.getId())
    }
    @Override
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
                database.delete("tablename", "_id = " + dbname_id_arr[i], null);
                dbOpenHelper.close();
            }
        }
        // break;
        //}
        //return super.onContextItemSelected(item);
        //item.setText();
        return super.onContextItemSelected(item);
    }*/
    @Override
    public void onClick(View v) {
    }
}
