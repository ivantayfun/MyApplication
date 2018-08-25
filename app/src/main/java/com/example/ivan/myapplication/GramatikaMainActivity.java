package com.example.ivan.myapplication;

//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//import com.example.myapplication.ExternalDbOpenHelper;
//import com.example.myapplication.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.graphics.Color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

public class GramatikaMainActivity extends Activity implements View.OnClickListener {
    LinearLayout gramatikamain_activity_lnlname;
    TextView text_russian;
    TextView text_english;
    TextView textenglish_pravelbnblu_otvet;
    String perv_zap;
    String perv_zap_marker;
    String Parser_DATA;
    String GRAMATIKA_ENGLISH_TIME;
    String GRAMATIKA_TIME_VBAZE;
    ArrayList<String> english_text_otobrajenie = new ArrayList<String>();
    ArrayList<String> english_ArrayList = new ArrayList<String>();
    ArrayList<String> russian_ArrayList = new ArrayList<String>();
    SharedPreferences sp;
    String tmp;
    String tmp2;
    public String tmp_otobrajenie;
    public String english_predlogenie;
    public String[] tmp_otobrajenie_arr;
    int[] id_vbaze;
    int[] mark;
    int id_predlojeniu;
    int id_slov_predlojeniu;
    boolean id_eshe_ne_otvetil;
    int kol_slov;
    int metka2;
    int metka;
    int mark_osn;
    int[] colors = new int[4];
    Button btn_steretb;
    Button btn_otvetitb;
    Button btn_dalshe;

    ExternalDbOpenHelper dbOpenHelper;
    LinearLayout linLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gramatika_main);
        btn_steretb = (Button) findViewById(R.id.button_steretb);
        btn_otvetitb = (Button) findViewById(R.id.button_otvetitb);
        btn_dalshe = (Button) findViewById(R.id.button_dalshe);
        btn_steretb.setOnClickListener(this);
        btn_otvetitb.setOnClickListener(this);
        btn_dalshe.setOnClickListener(this);
        perv_zap = "mark_english_time";
        GRAMATIKA_ENGLISH_TIME = "GRAMATIKA_ENGLISH_TIME";
        dbOpenHelper = new ExternalDbOpenHelper(this, "mydatabase.sqlite");
        id_vbaze = new int[100];
        mark = new int[20];
        tmp_otobrajenie_arr = new String[20];
        id_predlojeniu = 0;
        id_slov_predlojeniu = 0;
        id_eshe_ne_otvetil = false;
        tmp = "";
        tmp2 = "";
        tmp_otobrajenie = "";
        english_predlogenie = "";
        colors[0] = Color.parseColor("#ff9d9aff");
        text_russian = (TextView) findViewById(R.id.textrussian_gramatikamain_item);
        text_english = (TextView) findViewById(R.id.textenglish_gramatikamain_item);
        textenglish_pravelbnblu_otvet = (TextView) findViewById(R.id.textenglish_pravelbnblu_otvet);
        //Создаем обьект для данных
        linLayout = (LinearLayout) findViewById(R.id.gramatikamain_activity_lnlname);
        perv_zap_marker = loadText(perv_zap);
        if (perv_zap_marker.equals("")) {
            zagryzkavbazy();
        } else {
            GRAMATIKA_TIME_VBAZE = loadText(GRAMATIKA_ENGLISH_TIME);
            gramatika_time_funk(GRAMATIKA_TIME_VBAZE);
        }
    }

    public void zagryzkavbazy() {
        saveText(perv_zap, "0");
        saveText(GRAMATIKA_ENGLISH_TIME, "Present Simple");
        gramatika_time_funk("Present Simple");
    }
    public void saveText(String nidname, String nidname_text) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Editor ed = sp.edit();
        ed.putString(nidname, nidname_text);
        ed.commit();
    }
    public String loadText(String nidname) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Parser_DATA = sp.getString(nidname, "");
        return Parser_DATA;
    }

    public void gramatika_time_funk(String gramname) {
        id_predlojeniu = 0;
        String tabgramname = "[" + gramname + "]";
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        Cursor c = database.query(tabgramname, null, null, null, null, null, null);
        if (c != null) if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("_id");
                int englishColIndex = c.getColumnIndex("english");
                int russianColIndex = c.getColumnIndex("russian");
                int i = 0;
                do {
                    if (c.getString(englishColIndex).equals("")) {
                        break;
                    } else {
                        id_vbaze[i] = c.getInt(idColIndex);
                        english_ArrayList.add(c.getString(englishColIndex));
                        russian_ArrayList.add(c.getString(russianColIndex));
                        i++;
                    }
                } while (c.moveToNext());
            }
            text_russian.setText("gramatika_time_funk");
            sled_predloj_funk();
        } else text_russian.setText("Нет данных");
        dbOpenHelper.close();

    }
    public void sled_predloj_funk() {
        tmp2 = "";
        tmp = "";
        kol_slov = 0;
        int d = 0;
        english_predlogenie = "";
        try {
            XmlPullParser xpp = prepareXpp();
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        tmp_otobrajenie_arr[d] = xpp.getText();
                        english_predlogenie = english_predlogenie.concat(" ").concat(tmp_otobrajenie_arr[d]);
                        kol_slov++;
                        d++;
                        break;
                        default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int w = 0; w < kol_slov; w++) {
            mark[w] = 33 + w;
        }
        for (int i = 0; i < kol_slov; i++) {
            metka2 = 0;
            do {
                metka = 0;
                Random rand = new Random();
                mark_osn = rand.nextInt(kol_slov);
                for (int j = 0; j < kol_slov; j++) {
                    if (mark_osn == mark[j]) {
                        metka++;
                    }
                }
                if (metka == 0) {
                    mark[i] = mark_osn;
                    metka2++;
                }
            } while (metka2 == 0);

        }
        sled_slovo_funk();
    }
    public void english_text_obnovlenie(){
        tmp_otobrajenie = "";

        for (int m = 0; m < english_text_otobrajenie.size(); m++) {
            tmp_otobrajenie = tmp_otobrajenie.concat(" ").concat(english_text_otobrajenie.get(m));
        }
        text_english.setText(tmp_otobrajenie);
    }
    public void sled_slovo_funk(){
        text_russian.setText(russian_ArrayList.get(id_predlojeniu));
        gramatikamain_activity_lnlname = (LinearLayout) findViewById(R.id.gramatikamain_activity_lnlname);
        gramatikamain_activity_lnlname.removeAllViews();
        LayoutInflater ltInflayter_bdname = getLayoutInflater();


        for (int i = 0; i < kol_slov; i++) {
            double ostatok = Math.IEEEremainder(i, 2);
            View item = ltInflayter_bdname.inflate(R.layout.gramatikamain_item, gramatikamain_activity_lnlname, false);
            TextView text_slovary_item = (TextView) item.findViewById(R.id.text_gramatikamain_item);
            text_slovary_item.setText(tmp_otobrajenie_arr[mark[i]]);
            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            text_slovary_item.setId(mark[i]);
            item.setBackgroundColor(colors[0]);
            text_slovary_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    english_text_otobrajenie.add(tmp_otobrajenie_arr[v.getId()]);
                    english_text_obnovlenie();
                }
            });
            gramatikamain_activity_lnlname.addView(item);
        }

    }
     public XmlPullParser prepareXpp() throws XmlPullParserException {
        // получаем фабрику
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // включаем поддержку namespace (по умолчанию выключена)
        factory.setNamespaceAware(true);
        // создаем парсер
        XmlPullParser xpp = factory.newPullParser();
        // даем парсеру на вход Reader
        xpp.setInput(new StringReader(english_ArrayList.get(id_predlojeniu)));
        return xpp;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gramatika_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_steretb:
                if (english_text_otobrajenie.size()>0) {
                    english_text_otobrajenie.remove(english_text_otobrajenie.size() - 1);
                    english_text_obnovlenie();
                }
                // кнопка ОК
                //tvOut.setText("Нажата кнопка ОК");
                break;
            case R.id.button_otvetitb:
                if (english_ArrayList.size() >0) {
                    if (id_predlojeniu < english_ArrayList.size()) {
                        if (english_predlogenie.equals(text_english.getText())) {
                            text_russian.setText("Отлично");
                            gramatikamain_activity_lnlname.removeAllViews();
                            text_english.setText("");
                            english_text_otobrajenie.clear();
                            textenglish_pravelbnblu_otvet.setText("");
                            id_eshe_ne_otvetil = true;
                        } else if (text_english.getText().equals("")) {

                        } else {
                            text_russian.setText("Неправильно.");
                            gramatikamain_activity_lnlname.removeAllViews();
                            text_english.setText("Правильный ответ:");
                            textenglish_pravelbnblu_otvet.setText(english_predlogenie);
                            english_text_otobrajenie.clear();
                            id_eshe_ne_otvetil = true;
                        }

                    }
                }
                break;
            case R.id.button_dalshe:
                if (id_eshe_ne_otvetil) {
                    if (english_ArrayList.size() > 0) {
                        id_predlojeniu++;
                        if (id_predlojeniu >= english_ArrayList.size()) {
                            text_russian.setText("Начать с начала");
                            gramatikamain_activity_lnlname.removeAllViews();
                            text_english.setText("");
                            textenglish_pravelbnblu_otvet.setText("");
                            english_text_otobrajenie.clear();
                        } else {
                            english_text_otobrajenie.clear();
                            textenglish_pravelbnblu_otvet.setText("");
                            text_english.setText("");
                            id_eshe_ne_otvetil = false;
                            sled_predloj_funk();
                        }
                    }
                }
                break;
        }
    }
}
