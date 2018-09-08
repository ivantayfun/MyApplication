package com.example.ivan.myapplication;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Color;
import android.content.DialogInterface.OnClickListener;

/*import android.content.Context;

import java.util.Locale;*/
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int DIALOG_EXIT = 1;
    final int DIALOG_EXIT2 = 2;
    TextView my_text;
    TextView my_text1;
    TextView my_text2;
    TextView col_slov;
    TextView vuy4_slov;
    String v_text;
    String sr_text2;
    String perv_zap;
    //переменная по которой определяем загружалось ли приложение или загружается в первый раз
    String perv_zap_marker;
    String Parser_DATA;
    //переменная для хранения имени последнего используемого словаря в преференснастройках
    String TABLE_NAME_PARSER;
    //String TABLE_NAME;
    String id_vbazezapicb;
    String kolvo_pravnow;
    String vgetid;
    String flagydal;
    String selection;
    String DB_NAME;
    String MY_TABLE;
    String TAG;
    //переменная локали языка сохроняемая в настройках
    String pref_language;
    //SharedPreferences spref;
    SharedPreferences sp;
    String[] english_arr;
    String[] russian_arr;
    String[] transcription_arr;
    String[] selectionArgs2 = null;
    String[] dbname_name_arr;
    //String[] pref_locale;
    int[] dbname_id_arr;
    SQLiteDatabase database;
    int[] id_vbaze;
    int[] kolotv_arr;
    int[] vid_arr;
    int[] mark;
    int[] colors = new int[2];
    int kol_variantu;
    int kol_otvetu;
    int chislo_spiska;
    int mark_osn;
    int metka2;
    int my_rand;
    int na_random1;
    int na_random2;
    boolean chereda;
    boolean dopic;
    String[] locale_lang_arr;
    //String lang1;
    Resources res;
    //Context context;
    //locales locale;
    //Locale localeg;
    //Locale hhh;
    ExternalDbOpenHelper dbOpenHelper;
    LinearLayout linLayout;
    OnClickListener myClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    ychitbcnachalafynk();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    slovarifynk();
                    break;
            }
        }
    };
    OnClickListener myClickListener2 = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    ychitbcnachalafynk();
                    break;
                // негаитвная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        // ;
        res = getResources();
        setContentView(R.layout.activity_main);
        v_text = "";
        sr_text2 = "word";
        perv_zap = "perv_zap";
        TABLE_NAME_PARSER = "TABLE_NAME_PARSER";
        TAG = MainActivity.class.getSimpleName();
        my_text =  findViewById(R.id.my_text);
        my_text1 =  findViewById(R.id.my_text1);
        my_text2 =  findViewById(R.id.my_text2);
        col_slov =  findViewById(R.id.col_slov);
        vuy4_slov = findViewById(R.id.vuy4_slov);
        DB_NAME = "mydatabase.sqlite";
        dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        na_random2 = 5;
        na_random1 = 15;
        id_vbazezapicb = "";
        vgetid = "";
        kolvo_pravnow = "";
        flagydal = "0";
        MY_TABLE = "enimals";
        dopic = false;
        chereda = false;
        colors[0] = Color.parseColor("#fffbfdff");
        colors[1] = Color.parseColor("#ffb8d8ff");
        //Создаем обьект для данных
        linLayout =  findViewById(R.id.linLayout);
        english_arr = new String[100];
        russian_arr = new String[100];
        transcription_arr = new String[100];
        kolotv_arr = new int[100];
        vid_arr = new int[100];
        id_vbaze = new int[100];
        dbname_name_arr = new String[20];
        dbname_id_arr = new int[20];
        mark = new int[20];
        locale_lang_arr = new String[2];
        //Intent intent = getIntent();

        //если приложение уже загружалось в преференснастройках perv_zap будет равно 0 а не NULL
        perv_zap_marker = loadText(perv_zap);
        //определяе загружалось ли приложение
        if (perv_zap_marker.equals("")) {
            //если приложение не загружалось производим предварительную настройку в преференснастройках
            zagryzkavbazy();
        } else {
            loadPars();
            //загружаем имя последнего используемого словаря из преференснастроек
            MY_TABLE = loadText(TABLE_NAME_PARSER);
            cozdallict();
        }
    }

    private void zagryzkavbazy() {
        //SQLiteDatabase database = dbOpenHelper.openDataBase();
        saveText(perv_zap, "0");
        saveText(TABLE_NAME_PARSER, "animals");
        saveText("otvetu", "6");
        saveText("variantu", "5");
        saveText("pref_language", getResources().getConfiguration().locale.getLanguage());
        //переменная которая хранит имя последнего изучаемого словаря
        MY_TABLE = "animals";
        loadPars();
        cozdallict();
        dbOpenHelper.close();
    }

    private void saveText(String nidname, String nidname_text) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Editor ed = sp.edit();
        ed.putString(nidname, nidname_text);
        ed.apply();
    }

    private String loadText(String nidname) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Parser_DATA = sp.getString(nidname, "");
        return Parser_DATA;
    }

    private void loadPars() {
        kol_variantu = Integer.valueOf(loadText("variantu"));
        kol_otvetu = Integer.valueOf(loadText("otvetu"));
        pref_language = loadText("pref_language");
        //СМЕНА ЯЗЫКА ПОКА ЗАМОРОЖЕНА НЕ УДАЛЯТЬ
        /*if (pref_language.equals(getResources().getConfiguration().locale.getLanguage())) {

        } else {
            Configuration conf = getResources().getConfiguration();
            localeg = new Locale(pref_language);
            conf.setLocale(localeg);
            getBaseContext().getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

        }*/


    }

    //ЗАПУСК ДРУГИХ АКТИВИТИ

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ychitbcnachala:
                showDialog(DIALOG_EXIT2);
                break;
            case R.id.nastroiki:
                nastroikifynk();
                break;
            case R.id.slovari:
                slovarifynk();
                break;
            case R.id.gramatika:
                gramatikafunk();
                break;
            case R.id.proiznoshenie:
                proiznosheniefunk();
                break;
            case R.id.londonberlin:
                londonberlinfynk2();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //ФУНКЦИИ ЗАПУСТКА АКТИВИТИ ПРОИЗНОШЕНИЕ
    private void proiznosheniefunk() {
        Intent intent = new Intent(this, ProiznoshenieActivity.class);
        startActivity(intent);
        //finish();
    }

    //ЗАПУСК АКТИВИТИ ГРАМАТИКА
    private void gramatikafunk() {
        Intent intent = new Intent(this, GramatikaActivity.class);
        startActivity(intent);
    }

    //ЗАПУСК АКТИВИТИ НАСТРОЙКИ
    private void nastroikifynk() {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }

    //ЗАПУСК АКТИВИТИ СЛОВАРИ
    private void slovarifynk() {
        Intent intent = new Intent(this, ActivitiSlovarb.class);
        startActivity(intent);

    }

    //ЗАПУСК АКТИВИТИ СЛОВАРИ
//    private void slovarifynk2() {
//        Intent intent = new Intent(this, ActivitySlovarbList.class);
//        startActivity(intent);
//        finish();
//    }

    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    /*private void londonberlinfynk() {
        Intent intent = new Intent(this, LondonBerlinActivity.class);
        startActivity(intent);

    }*/

    //ЗАПУСК АКТИВИТИ ЛОНДОНБЕРЛИН
    private void londonberlinfynk2() {
        Intent intent = new Intent(this, ZagryzkaSpiskaSlovarey.class);
        startActivity(intent);

    }


    //ПЕРЕОПРЕДЕЛЕНИЕ МЕТОДОВ ЖИЗНЕННОГО ЦИКЛА
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onRrsume()");
        id_vbazezapicb = "";
        vgetid = "";
        loadPars();
        MY_TABLE = loadText(TABLE_NAME_PARSER);





            cozdallict();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (data == null) {
            return;
        }
        MY_TABLE = data.getStringExtra("slovarb_name");
        Log.d(TAG, "получаем активити резалт" + MY_TABLE );
        saveText(TABLE_NAME_PARSER, MY_TABLE);
        cozdallict();*/
    }

    //ФУНКЦИЯ ОБНУЛЕНИЯ ПРАВИЛЬНЫХ ОТВЕТОВ В СЛОВАРЕ
    private void ychitbcnachalafynk() {
        linLayout.removeAllViews();
        //SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("kolotv", "0");
        cv.put("vid", "0");
        //int clearCount = database.update(MY_TABLE, cv, null, null);
        id_vbazezapicb = "";
        vgetid = "";
        MY_TABLE = loadText(TABLE_NAME_PARSER);
        dbOpenHelper.close();
        loadPars();
        cozdallict();

    }

    //ФУНКЦИЯ СОЗДАНИЯ ЛИСТА ИЗУЧЕНИЯ СЛОВ
    private void cozdallict() {
        my_text.setText(res.getString(R.string.exit));
        my_text1.setText("");
        my_text2.setText("");
        col_slov.setText("");
        vuy4_slov.setText("");
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();


        Cursor cursor2 = database.rawQuery("SELECT sql FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", MY_TABLE});
        if (!cursor2.moveToFirst()) {
            dbOpenHelper.close();
            slovarifynk();
            cursor2.close();
        } else {
            if (!(id_vbazezapicb.equalsIgnoreCase("") & vgetid.equalsIgnoreCase(""))) {

                ContentValues cv = new ContentValues();
                cv.put("kolotv", id_vbazezapicb);
                cv.put("vid", flagydal);

                database.update(MY_TABLE, cv, "_id = ?", new String[]{vgetid});
            }
            Cursor cursor = database.query(MY_TABLE, null, null, null, null, null, null);
            int colslov = cursor.getCount();
            String concat = res.getString(R.string.vsego_slov) + Integer.toString(colslov);
            col_slov.setText(concat);
            cursor.close();
            selectionArgs2 = new String[]{"0"};
            selection = "vid = ?";


            Cursor c = database.query(MY_TABLE, null, selection, selectionArgs2, null, null, null);
            int vuy4slov = colslov - c.getCount();

            String foconcat = res.getString(R.string.vbIy4eno) + Integer.toString(vuy4slov);
            vuy4_slov.setText(foconcat);
            if (c.getCount() > 0) {
                chislo_spiska = 0;
                if (c.moveToFirst()) {
                    int idColIndex = c.getColumnIndex("_id");
                    int englishColIndex = c.getColumnIndex("english");
                    int russianColIndex = c.getColumnIndex("russian");
                    int transcriptionColIndex = c.getColumnIndex("transcription");
                    int kolotvColIndex = c.getColumnIndex("kolotv");
                    int vidColIndex = c.getColumnIndex("vid");
                    int i = 0;
                    do {
                        if (i >= 15) {
                            break;
                        } else {
                            id_vbaze[i] = c.getInt(idColIndex);
                            english_arr[i] = "" + c.getString(englishColIndex) + "";
                            russian_arr[i] = "" + c.getString(russianColIndex) + "";
                            transcription_arr[i] = "" + c.getString(transcriptionColIndex) + "";
                            kolotv_arr[i] = c.getInt(kolotvColIndex);
                            vid_arr[i] = c.getInt(vidColIndex);
                            chislo_spiska++;
                            i++;
                        }
                    } while (c.moveToNext());
                }
                if (chislo_spiska < 15) {
                    na_random1 = chislo_spiska;
                } else {
                    na_random1 = 15;
                }
                if (chislo_spiska < kol_variantu) {
                    na_random2 = chislo_spiska;
                    mark[na_random2] = 90 + na_random2;

                } else {
                    na_random2 = kol_variantu;
                    mark[na_random2] = 90 + na_random2;
                }
                LayoutInflater ltInflayter = getLayoutInflater();
                linLayout.removeAllViews();
                Random rand2 = new Random();
                my_rand = rand2.nextInt(na_random2);
                for (int w = 0; w < na_random2; w++) {
                    mark[w] = 33 + w;
                }
                for (int k = 0; k < na_random2; k++) {
                    metka2 = 0;
                    do {
                        int metka = 0;
                        Random rand = new Random();
                        mark_osn = rand.nextInt(na_random1);
                        for (int j = 0; j < na_random2; j++) {
                            if (mark_osn == mark[j]) {
                                metka++;
                            }
                        }
                        if (metka == 0) {
                            mark[k] = mark_osn;
                            metka2++;
                        }
                    } while (metka2 == 0);
                    View item = ltInflayter.inflate(R.layout.item, linLayout, false);
                    TextView tvName =  item.findViewById(R.id.tvName);
                    TextView tvName2 = item.findViewById(R.id.tvName2);
                    if (chereda) {
                        if (k == my_rand) {

                            my_text.setText(russian_arr[mark[my_rand]]);
                            my_text1.setText("");

                        }
                        tvName.setText(english_arr[mark[k]]);
                        tvName2.setText(transcription_arr[mark[k]]);

                    } else {
                        if (k == my_rand) {
                            my_text.setText(english_arr[mark[my_rand]]);
                            my_text1.setText(transcription_arr[mark[my_rand]]);

                        }
                        tvName.setText(russian_arr[mark[k]]);
                        tvName2.setText("");
                    }
                    item.setId(mark[k]);
                    item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
                    kolvo_pravnow = Integer.toString(kolotv_arr[mark[my_rand]]);
                    my_text2.setText(kolvo_pravnow);
                    item.setBackgroundColor(colors[k % 2]);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mark[my_rand] == v.getId()) {
                                vgetid = Integer.toString(id_vbaze[v.getId()]);
                                kolotv_arr[v.getId()] = kolotv_arr[v.getId()] + 1;
                                id_vbazezapicb = Integer.toString(kolotv_arr[v.getId()]);
                                if (chereda) {
                                    chereda = false;
                                } else {
                                    chereda = true;
                                }
                                if (kolotv_arr[v.getId()] < kol_otvetu) {
                                    flagydal = "0";
                                } else {
                                    flagydal = "1";
                                }
                                cozdallict();
                            }
                        }
                    });
                    linLayout.addView(item);
                }
                c.close();
            } else {
                linLayout.removeAllViews();
                c.close();
                showDialog(DIALOG_EXIT);
            }
        }
        dbOpenHelper.close();

    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.exit);
            // сообщение
            adb.setMessage(R.string.save_data);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.ychitb_zanovo, myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.vubratb_drygoi_slovarb, myClickListener);
            // делаем незакрываемым по кнопке назад
            //adb.setCancelable(false);
            // создаем диалог
            return adb.create();
        }
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

    @Override
    public void onClick(View v) {
    }

}